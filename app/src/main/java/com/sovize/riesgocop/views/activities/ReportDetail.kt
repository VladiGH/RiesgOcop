package com.sovize.riesgocop.views.activities

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.firebase.MasterCrud
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.models.User
import com.sovize.riesgocop.utilities.AppKey
import com.sovize.riesgocop.utilities.ServerInfo
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import kotlinx.android.synthetic.main.viewer_report.*


class ReportDetail : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var report: AccidentReport? = AccidentReport()
    private lateinit var stateValue: String
    val carousel: CarouselPicker? = null
    val itemsImages: ArrayList<CarouselPicker.PickerItem>? = null
    val masterCrud = MasterCrud()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("INFO ", "esto si llega")
        setContentView(R.layout.viewer_report)
        Log.d("INFO ", "esto si llegax")
        val reportInfo = intent?.extras?.getParcelable<AccidentReport>(AppKey.reportInfo)
        val report = AccidentReport(
            reportInfo!!.id,
            reportInfo.location,
            reportInfo.personInjuredName,
            reportInfo.personInjuredGender,
            reportInfo.accidentedPersonType,
            reportInfo.description,
            reportInfo.severityLevel,
            reportInfo.placeOfAttention,
            reportInfo.ambullance,
            reportInfo.pictures,
            reportInfo.date,
            reportInfo.state
        )

        bindData(findViewById(R.id.viewer_id), report)
        spinnerState()
        carousel?.findViewById<CarouselPicker>(R.id.rv_report_photos)

        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))
        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))
        itemsImages?.add(CarouselPicker.DrawableItem(R.drawable.ic_launcher_background))
        findViewById<Spinner>(R.id.spinner_state).onItemSelectedListener = this

        val viewModel = ViewModelProviders.of(this).get(ViewModelMainActivity::class.java)

        viewModel.getUserData().observe(this, Observer<User> {
            it?.let {
                if (report.state == 1 && it.permission.contains("g")) {
                    masterCrud.updateReport(report, 2)
                }
            }
        })
    }

    private fun bindData(view: View, report: AccidentReport) {

        val severity = "${resources.getString(R.string.severity)}: ${report.severityLevel}"
        val location = "${resources.getString(R.string.location)}: ${report.location}"
        val descrip = "${resources.getString(R.string.description)}: ${report.description}"
        val name = "${resources.getString(R.string.person_injured_name)}: ${report.personInjuredName}"
        val gender = "${resources.getString(R.string.gender_of_the_person_injured)}: ${report.personInjuredGender}"
        val place = "${resources.getString(R.string.place_of_attention)}: ${report.placeOfAttention}"
        val ambullance = "${resources.getString(R.string.was_necessary_an_ambulance)}: ${report.ambullance}"
        val date = "${resources.getString(R.string.date)}: ${report.date}"

        view.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarviewer_reportname).title =
            report.accidentedPersonType
        view.findViewById<TextView>(R.id.app_bar_rating_danger_viewer).text = severity
        view.findViewById<TextView>(R.id.location).text = location
        view.findViewById<TextView>(R.id.description_report_viewer).text = descrip
        view.findViewById<TextView>(R.id.nameOfPerson).text = name
        view.findViewById<TextView>(R.id.genderOfPerson).text = gender
        view.findViewById<TextView>(R.id.placeAttention).text = place
        view.findViewById<TextView>(R.id.ambullanceNec).text = ambullance

        view.findViewById<TextView>(R.id.tv_date).text = date

        val imageAdapter = CarouselPicker.CarouselViewAdapter(this, itemsImages, 3)
        carousel?.adapter = imageAdapter

        /*Glider.load("${ServerInfo.baseURL}${report.pictures[0]}",
            findViewById(R.id.app_bar_report_image_viewer))*/
        if (report.pictures.isNotEmpty()) {
            Glider.load(
                "${ServerInfo.baseURL}${report.pictures[0]}",
                findViewById(R.id.app_bar_report_image_viewer)
            )
        } else {
            Snackbar.make(
                findViewById(R.id.app_bar_report_image_viewer),
                resources.getString(R.string.noPics), Snackbar.LENGTH_LONG
            ).show()
            Glide.with(this@ReportDetail)
                .load(R.drawable.ic_broken_image_black_48dp)
                .into(findViewById(R.id.app_bar_report_image_viewer))
        }

    }

    private fun spinnerState() {
        val stateSpinner = findViewById<Spinner>(R.id.spinner_state)
        val adapterS =
            ArrayAdapter.createFromResource(this@ReportDetail, R.array.state, android.R.layout.simple_spinner_item)
        adapterS.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        stateSpinner.adapter = adapterS
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        stateValue = spinner_state.selectedItem.toString()
        Snackbar.make(
            findViewById<Spinner>(R.id.spinner_state),
            "${resources.getString(R.string.actual_state)}: ${stateValue}", Snackbar.LENGTH_LONG
        ).show()
    }

}