package com.sovize.riesgocop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.Report
import kotlinx.android.synthetic.main.item_list_report.view.*


class ReportAdapter(val items: List<Report>, val clickListener: (Report)-> Unit)
    : RecyclerView.Adapter<ReportAdapter.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_report, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(item: Report, clickListener: (Report) -> Unit)= with(itemView){
            tv_report_title.text = item.title
            tv_report_danger.text = item.danger.toString()
            this.setOnClickListener { clickListener(item) }

        }
    }
}