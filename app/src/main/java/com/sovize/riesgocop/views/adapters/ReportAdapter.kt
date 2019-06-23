package com.sovize.riesgocop.views.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.AccidentReport
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
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#D8D8D8"))
        }



/*
        fun sortArrayList() {
            val sortedList = ArrayList!!.run { sortWith(compareBy { it.accidentedPersonType }) }
            return sortedList
        }*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var fecha = Date()
        var formatFecha = SimpleDateFormat("dd-MM-yy")

        fun bind(item: AccidentReport, clickListener: (AccidentReport) -> Unit)= with(itemView){
            val occupation = "${resources.getString(R.string.occupation_of_the_person)}: ${item.accidentedPersonType}"
            val severity = "${ resources.getString(R.string.accident_s_severity)}: ${item.severityLevel}"

            tv_report_date.text = formatFecha.format(fecha).toString()
            tv_report_title.text = occupation
            tv_report_danger.text = severity
            this.setOnClickListener { clickListener(item) }

        }
    }
}