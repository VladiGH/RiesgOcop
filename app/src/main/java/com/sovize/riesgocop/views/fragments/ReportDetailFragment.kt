package com.sovize.riesgocop.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.Report
import java.text.SimpleDateFormat
import java.util.*

class ReportDetailFragment: Fragment() {

    var report: Report? = Report()
    var fecha = Date()
    var formatFecha = SimpleDateFormat("dd-MM-yy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.viewer_report, container, false)
        bindData(view)
        return view

    }

    fun bindData(view: View){
        view.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarviewer_reportname).title = report?.title
        view.findViewById<TextView>(R.id.app_bar_rating_danger_viewer).text = report?.danger.toString()
        view.findViewById<TextView>(R.id.location).text = report?.location
        view.findViewById<TextView>(R.id.description_report_viewer).text = report?.description
        view.findViewById<TextView>(R.id.et_date).text = formatFecha.format(fecha).toString()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(newReport: Report?):ReportDetailFragment{
            val newFragment= ReportDetailFragment()
            newFragment.report = newReport
            return newFragment

        }
    }
}