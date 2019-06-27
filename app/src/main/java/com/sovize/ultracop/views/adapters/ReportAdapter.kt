package com.sovize.ultracop.views.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.ServerInfo
import kotlinx.android.synthetic.main.item_list_report.view.*


class ReportAdapter(val items: MutableList<AccidentReport>, private val clickListener: (AccidentReport)-> Unit)
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
        if((position % 2)==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"))
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#C5CAE9"))
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        
        fun bind(item: AccidentReport, clickListener: (AccidentReport) -> Unit)= with(itemView){
            val occupation = "${resources.getString(R.string.occupation_of_the_person)}: ${item.personType}"
            val severity = "${ resources.getString(R.string.accident_s_severity)}: ${item.severityLevel}"

            tv_report_date.text = item.date
            tv_report_title.text = occupation
            tv_report_danger.text = severity
            this.setOnClickListener { clickListener(item) }

            if(item.pictures.isNotEmpty()){
                Glider.load("${ServerInfo.baseURL}${item.pictures[0]}",
                    findViewById(R.id.report_state))
            } else{
                Glide.with(this.context)
                    .load(R.drawable.ic_broken_image_black_48dp)
                    .into(findViewById(R.id.report_state))
            }
            if(item.state==1){
                Glide.with(this.context)
                    .load(R.drawable.ic_active_on)
                    .into(findViewById(R.id.estado))
            }
            else{
                Glide.with(this.context)
                    .load(R.drawable.ic_active_pause)
                    .into(findViewById(R.id.estado))
            }

        }
    }
}