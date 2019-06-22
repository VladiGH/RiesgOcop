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
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppKey
import com.sovize.riesgocop.utilities.ServerInfo
import com.sovize.riesgocop.views.fragments.ReportDetailFragment
import java.text.SimpleDateFormat
import java.util.*

class ReportDetail: AppCompatActivity() {
    var report: AccidentReport? = AccidentReport()
    var fecha = Date()
    var formatFecha = SimpleDateFormat("dd-MM-yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("INFO ", "esto si llega")
        setContentView(R.layout.viewer_report)
        Log.d("INFO ", "esto si llegax")
        val reportInfo = intent?.extras?.getParcelable<AccidentReport>(AppKey.reportInfo)
        val report = AccidentReport(reportInfo!!.id,reportInfo.location,reportInfo.personInjuredName,reportInfo.personInjuredGender,
            reportInfo.accidentedPersonType, reportInfo.description, reportInfo.severityLevel, reportInfo.placeOfAttention,
            reportInfo.ambullance, reportInfo.pictures)

        bindData(findViewById(R.id.viewer_id), report)

    }
    fun bindData(view: View, report: AccidentReport){
        view.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarviewer_reportname).title = report.accidentedPersonType
        view.findViewById<TextView>(R.id.app_bar_rating_danger_viewer).text = report.severityLevel
        view.findViewById<TextView>(R.id.location).text = report.location
        view.findViewById<TextView>(R.id.description_report_viewer).text = report.description
        view.findViewById<TextView>(R.id.nameOfPerson).text = report.personInjuredName
        view.findViewById<TextView>(R.id.genderOfPerson).text = report.personInjuredGender
        view.findViewById<TextView>(R.id.placeAttention).text = report.placeOfAttention
        view.findViewById<TextView>(R.id.ambullanceNec).text = report.ambullance

        view.findViewById<TextView>(R.id.et_date).text = formatFecha.format(fecha).toString()

        /*Glider.load("${ServerInfo.baseURL}${report.pictures[0]}",
            findViewById(R.id.app_bar_report_image_viewer))*/
    }
}