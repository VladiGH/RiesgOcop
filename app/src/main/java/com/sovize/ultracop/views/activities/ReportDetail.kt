package com.sovize.ultracop.views.activities

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.ServerInfo
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import kotlinx.android.synthetic.main.viewer_report.*


class ReportDetail : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private var report: AccidentReport = AccidentReport()
    private lateinit var stateValue: String
    private val carousel: CarouselPicker? = null
    private val itemsImages: ArrayList<CarouselPicker.PickerItem>? = null
    private val masterCrud = MasterCrud()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_report)


        intent?.let{
           report = it.extras!!.getParcelable<AccidentReport>(AppKey.reportInfo)
        }

        savedInstanceState?.let{
            report = it.getParcelable("KEY")
        }


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

        findViewById<Button>(R.id.map_btn).setOnClickListener {
            val coordinates = Intent(this@ReportDetail, MapsActivity::class.java)
            coordinates.putExtra(AppKey.longitude, report.longitude)
            coordinates.putExtra(AppKey.latitude, report.latitude)
            Log.d(AppLogger.reportDetail, "${report.longitude} ${report.latitude}")
            startActivity(coordinates)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val report2 = outState
        report2.putParcelable("KEY", report)
    }

    private fun bindData(view: View, report: AccidentReport?) {

        val severity = "${resources.getString(R.string.severity)}: ${report?.severityLevel}"
        val location = "${resources.getString(R.string.location)}: ${report?.location}"
        val descrip = "${resources.getString(R.string.description)}: ${report?.description}"
        val name = "${resources.getString(R.string.person_injured_name)}: ${report?.personInjuredName}"
        val gender = "${resources.getString(R.string.gender_of_the_person_injured)}: ${report?.personInjuredGender}"
        val place = "${resources.getString(R.string.place_of_attention)}: ${report?.placeOfAttention}"
        val ambullance = "${resources.getString(R.string.was_necessary_an_ambulance)}: ${report?.ambullance}"
        val date = "${resources.getString(R.string.date)}: ${report?.date}"

        view.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarviewer_reportname).title =
            report?.accidentedPersonType
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
        if (report?.pictures != null && report.pictures.isNotEmpty()) {
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
            "${resources.getString(R.string.actual_state)}: $stateValue",
            Snackbar.LENGTH_LONG
        ).show()
    }

}