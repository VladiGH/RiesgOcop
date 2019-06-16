package com.sovize.riesgocop.views.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppKey
import com.sovize.riesgocop.utilities.ServerInfo
import com.sovize.riesgocop.views.fragments.ReportDetailFragment
import java.text.SimpleDateFormat
import java.util.*

class ReportDetail: AppCompatActivity() {
    var report: Report? = Report()
    var fecha = Date()
    var formatFecha = SimpleDateFormat("dd-MM-yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("INFO ", "esto si llega")
        setContentView(R.layout.viewer_report)
        Log.d("INFO ", "esto si llegax")
        val reportInfo = intent?.extras?.getParcelable<Report>(AppKey.reportInfo)
        val report = Report(reportInfo!!.id,reportInfo.title,reportInfo.danger, reportInfo.description,reportInfo.location,reportInfo.pictures)
        bindData(findViewById(R.id.viewer_id), report)

    }
    fun bindData(view: View, report: Report){
        view.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarviewer_reportname).title = report.title
        view.findViewById<TextView>(R.id.app_bar_rating_danger_viewer).text = report.danger.toString()
        view.findViewById<TextView>(R.id.location).text = report.location
        view.findViewById<TextView>(R.id.description_report_viewer).text = report.description
        view.findViewById<TextView>(R.id.et_date).text = formatFecha.format(fecha).toString()

        Glider.load("${ServerInfo.baseURL}${report.pictures[0]}",
            findViewById(R.id.app_bar_report_image_viewer))
    }
}