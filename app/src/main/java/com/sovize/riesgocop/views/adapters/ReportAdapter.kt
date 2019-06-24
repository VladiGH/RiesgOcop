package com.sovize.riesgocop.views.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.utilities.ServerInfo
import kotlinx.android.synthetic.main.activity_report.view.*
import kotlinx.android.synthetic.main.item_list_report.view.*
import java.text.SimpleDateFormat
import java.util.*


class ReportAdapter(val items: MutableList<AccidentReport>, private val clickListener: (AccidentReport)-> Unit)
    : RecyclerView.Adapter<ReportAdapter.ViewHolder> (){

    //val ArrayList: MutableList<AccidentReport>? = null


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
        }/*
        if(items[position].state==0) {
            //holder.itemView.load(R.drawable.ic_active_done).into(findViewById(R.id.estado))


        }*/
        /*
                fun sortArrayList() {
                    val sortedList = ArrayList!!.run { sortWith(compareBy { it.accidentedPersonType }) }
                    return sortedList
                }*/


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        
        fun bind(item: AccidentReport, clickListener: (AccidentReport) -> Unit)= with(itemView){
            val occupation = "${resources.getString(R.string.occupation_of_the_person)}: ${item.accidentedPersonType}"
            val severity = "${ resources.getString(R.string.accident_s_severity)}: ${item.severityLevel}"
            val state = "${resources.getString(R.string.actual_state)}:${item.state}"

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
            /*if(item.state==1){
                Glide.with(this.context)
                    .load(R.drawable.ic_active_on)
                    .into(findViewById(R.id.estado))
            }
            else{
                Glide.with(this.context)
                    .load(R.drawable.ic_active_pause)
                    .into(findViewById(R.id.estado))
            }*/

        }
    }
}