package com.sovize.ultracop.views.activities

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.ServerInfo
import com.sovize.ultracop.views.fragments.ReportDetailFragment


class ReportDetail : AppCompatActivity() {


    private var report = AccidentReport()
    private val carousel: CarouselPicker? = null
    private val itemsImages: ArrayList<CarouselPicker.PickerItem>? = null
    private lateinit var viewerFragment: ReportDetailFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_report)

        intent?.let {
            report = it.extras?.getParcelable(AppKey.reportInfo) ?: AccidentReport()
        }

        savedInstanceState?.let {
            report = it.getParcelable("KEY") ?: AccidentReport()
        }

        carousel?.findViewById<CarouselPicker>(R.id.rv_report_photos)
        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))
        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))
        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))

        findViewById<ImageView>(R.id.app_bar_report_image_viewer)?.apply {
            if (report.pictures.isNotEmpty()) {
                Glider.load(
                    "${ServerInfo.baseURL}${report.pictures[0]}",
                    this
                )
            }
        }
        val arraySev = resources.getStringArray(R.array.severity)
        val arrayPersonT = resources.getStringArray(R.array.ocuppation)
        val severityString = when(report.severityLevel){
            0-> arraySev[0]
            1-> arraySev[1]
            2-> arraySev[2]
            else -> "N/A"
        }

        val sev = "${resources.getString(R.string.severity)}: $severityString"
       val personTypeString = when(report.personType){
           0-> arrayPersonT[0]
           1-> arrayPersonT[1]
           2-> arrayPersonT[2]
           else->"N/A"
        }
        findViewById<TextView>(R.id.app_bar_rating_danger_viewer).text = sev
        findViewById<CollapsingToolbarLayout>(R.id.collapsing_report_name).title = personTypeString


        viewerFragment = ReportDetailFragment()
        viewerFragment.report = report

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_holder,
            viewerFragment
        ).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("KEY", report)
    }

}