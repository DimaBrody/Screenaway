package com.askete.meditate.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askete.meditate.R
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.utils.*
import kotlinx.android.synthetic.main.item_date_timeline.view.*
import kotlinx.android.synthetic.main.item_desc_timeline.view.*
import java.util.*

class TimeLineAdapter(
    private val context: Context,
     val items: List<DataSnapshot>
) : RecyclerView.Adapter<TimeLineAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val isDescType = viewType == DataType.descType
        val view = LayoutInflater.from(context)
            .inflate(
                if (isDescType) R.layout.item_desc_timeline else R.layout.item_date_timeline,
                parent,
                false
            )
        return if (isDescType) DescViewHolder(view) else DateViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DescViewHolder(view: View) : CustomViewHolder(view) {
        override fun bind(position: Int) {
            itemView.item_desc_text.text = items[position].desc
        }
    }

    //April 20, 3.38 AM - 3.58 AM

    inner class DateViewHolder(view: View) : CustomViewHolder(view) {
        @SuppressLint("SetTextI18n")
        override fun bind(position: Int) {
            val item = items[position]
            val calendar = instantiateCalendar(item.date)
            val nextCalendar = instantiateCalendar(if(!item.isLeft) item.date + getTimeById(item.timeId) else item.date + (item.leftTime - item.date))

            val month =
                context.resources.getStringArray(R.array.months)[calendar.get(Calendar.MONTH)]
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val timeStart = getNormalTimeFromCalendar(calendar)

            val timeEnd = getNormalTimeFromCalendar(nextCalendar)

            itemView.item_date_text.text = "$month $day, $timeStart - $timeEnd ${if(item.isLeft) "*" else ""}"
        }
    }

    abstract inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(position: Int)
    }

    override fun getItemViewType(position: Int): Int =
        if (items[position].desc.isNotEmpty())
            DataType.descType else DataType.dataType

    private object DataType {
        const val descType = 0
        const val dataType = 1
    }

}