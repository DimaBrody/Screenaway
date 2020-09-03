package com.askete.meditate.ui.charts

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.data.model.SnapData
import com.askete.meditate.ui.adapter.SnapInfoAdapter
import com.askete.meditate.ui.adapter.TimeLineAdapter
import com.askete.meditate.ui.adapter.snappers.SnapHelper
import com.askete.meditate.ui.base.BaseFragment
import com.askete.meditate.utils.clearLightStatusBar
import com.askete.meditate.utils.createTimelineArray
import com.askete.meditate.utils.getTimeById
import com.askete.meditate.utils.setLightStatusBar
import com.askete.meditate.utils.thread.main
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_charts.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

class ChartsFragment : BaseFragment<ChartsViewModel>(
    ChartsViewModel::class
) {

    private lateinit var mHorizontalAdapter: SnapInfoAdapter

    private lateinit var mTimelineAdapter: TimeLineAdapter

    private lateinit var mDataTimelineList: List<DataSnapshot>

    private lateinit var mSnapList: List<SnapData>

    override fun View.createViews() {
        setLightStatusBar(requireActivity())

        charts_back.setOnClickListener {
            clearLightStatusBar(requireActivity())
            navigateMain()
        }

//        YandexMetrica.reportEvent("ChartsFragment","onCreate()")

        mHorizontalAdapter = SnapInfoAdapter(SnapData.createEmptyData())

        charts_recycler_data.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mHorizontalAdapter
            onFlingListener = null
            SnapHelper.create().attachToRecyclerView(this)
        }

        mViewModel.fetchData(lifecycleScope) { data ->
            counterTotalDays = 0
            totalTimes = 0
            totalLeaves = 0
            currentDaysInARow = 0
            bestDaysInARow = 0


            mDataTimelineList = data
            val totalTime = TimeUnit.MILLISECONDS.toMinutes(
                mDataTimelineList.sumBy {
                    if (!it.isLeft) getTimeById(it.timeId).toInt()
                    else (it.leftTime - it.date).toInt()
                }.toLong()
            )

            mTimelineAdapter = TimeLineAdapter(
                requireContext(),
                createTimelineArray(requireContext(), mDataTimelineList, iterateSnapInfo)
            )

            mSnapList = listOf(
                SnapData(SnapData.Desc.TOTAL_DAYS, counterTotalDays.toString()),
                SnapData(SnapData.Desc.COMPLETED_TIMES, totalTimes.toString()),
                SnapData(SnapData.Desc.LEAVES, totalLeaves.toString()),
                SnapData(SnapData.Desc.CURRENT_ROW, currentDaysInARow.toString()),
                SnapData(
                    SnapData.Desc.BEST_ROW,
                    max(bestDaysInARow, counterBestDaysInARow).toString()
                )
            )

            mHorizontalAdapter.apply {
                items = mSnapList
                main {
                    notifyDataSetChanged()
                }
            }

            main {
                charts_global_time.text = "$totalTime min"

                charts_recycler_timeline.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mTimelineAdapter
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newLayoutParams = view.charts_recycler_timeline.layoutParams as ConstraintLayout.LayoutParams
        newLayoutParams.bottomMargin = newLayoutParams.bottomMargin + getNavigationHeight()
        view.charts_recycler_timeline.layoutParams = newLayoutParams
    }

    private fun getNavigationHeight() : Int {
        val resources: Resources = requireContext().resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private var lastCurrentTimeInMillis = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }.apply {
        set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE), 0, 0, 0)
    }.timeInMillis

    private val hour24InMillis = TimeUnit.HOURS.toMillis(24)

    private var counterTotalDays = 0
    private var counterBestDaysInARow = 0

    private var currentDaysInARow = 0
    private var bestDaysInARow = 0

    private var totalTimes = 0
    private var totalLeaves = 0

    private var isSameDay = false
    private var isCurrentDayAvailable = true

    private val iterateSnapInfo: (DataSnapshot, Calendar?, Calendar?) -> Unit =
        { item, prevCalendar, currentCalendar ->

            if (!item.isLeft) {
                totalTimes++

                if (prevCalendar == null) {
                    counterTotalDays++
                    counterBestDaysInARow++
                    if (lastCurrentTimeInMillis - currentCalendar!!.timeInMillis <= hour24InMillis)
                        currentDaysInARow++ else isCurrentDayAvailable = false

                    isSameDay = true
                    lastCurrentTimeInMillis = setupCalendarForTimeMillis(currentCalendar)
                } else {
                    isSameDay = compareDays(prevCalendar, currentCalendar)

                    if (!isSameDay) {
                        counterTotalDays++

                        if (lastCurrentTimeInMillis - currentCalendar!!.timeInMillis <= hour24InMillis) {
                            counterBestDaysInARow++
                            if (isCurrentDayAvailable)
                                currentDaysInARow++
                        } else {
                            isCurrentDayAvailable = false
                            if (counterBestDaysInARow > bestDaysInARow)
                                bestDaysInARow = counterBestDaysInARow
                            counterBestDaysInARow = 0
                        }

                        lastCurrentTimeInMillis = setupCalendarForTimeMillis(currentCalendar)
                    }
                }
            } else {
                totalLeaves++
                if(prevCalendar == null){
                    counterTotalDays++
                } else if(!compareDays(prevCalendar,currentCalendar)){
                    counterTotalDays++
                }
            }

        }

    private val compareDays: (Calendar?, Calendar?) -> Boolean
        get() = { prevCalendar, currentCalendar ->
            prevCalendar!!.get(Calendar.YEAR) == currentCalendar!!.get(Calendar.YEAR) &&
                    prevCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
        }


    private fun setupCalendarForTimeMillis(calendar: Calendar): Long =
        calendar.apply {
            set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE), 0, 0, 0)
        }.timeInMillis
}