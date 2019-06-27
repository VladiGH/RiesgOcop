package com.sovize.ultracop.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sovize.ultracop.R
import com.sovize.ultracop.models.AccidentReport

class ReportDetailFragment : Fragment() {

    var report = AccidentReport()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.viewer_report, container, false)

        val name = "${resources.getString(R.string.person_injured_name)}: ${report.personInjuredName}"
        val gender = "${resources.getString(R.string.gender_of_the_person_injured)}: ${report.personInjuredGender}"
        val place = "${resources.getString(R.string.place_of_attention)}: ${report.placeOfAttention}"
        val ambulance = "${resources.getString(R.string.was_necessary_an_ambulance)}: ${report.ambullance}"
        val date = "${resources.getString(R.string.date)}: ${report.date}"
        val severity = "${resources.getString(R.string.severity)}: ${report.severityLevel}"

        v?.let {
            it.findViewById<TextView>(R.id.nameOfPerson).text = name
            it.findViewById<TextView>(R.id.genderOfPerson).text = gender
            it.findViewById<TextView>(R.id.description_report_viewer).text = resources.getString(R.string.description)
            it.findViewById<TextView>(R.id.description_content).text = report.description
            it.findViewById<TextView>(R.id.placeAttention).text = place
            it.findViewById<TextView>(R.id.ambulance).text = ambulance
            it.findViewById<TextView>(R.id.tv_date).text = date
            it.findViewById<TextView>(R.id.severity).text = severity
        }
        return v
    }


}