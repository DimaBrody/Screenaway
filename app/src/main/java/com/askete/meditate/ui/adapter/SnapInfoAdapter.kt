package com.askete.meditate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askete.meditate.R
import com.askete.meditate.data.model.SnapData
import kotlinx.android.synthetic.main.snap_item.view.*

class SnapInfoAdapter(
    var items: List<SnapData>
) : RecyclerView.Adapter<SnapInfoAdapter.SnapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapViewHolder =
        SnapViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.snap_item, parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SnapViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class SnapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val item = items[position]
            with(itemView) {
                snap_item_text_data.text = item.desc
                snap_item_text_header.text = item.header
            }
        }
    }
}