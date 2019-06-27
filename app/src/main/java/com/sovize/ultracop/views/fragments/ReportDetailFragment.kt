package com.sovize.ultracop.views.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.ServerInfo
import com.sovize.ultracop.views.activities.MapsActivity

class ReportDetailFragment : Fragment() {

    var report = AccidentReport()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.viewer_report, container, false)

        val arrayGender = resources.getStringArray(R.array.gender)
        val arrayPlace = resources.getStringArray(R.array.placeOfAttention)
        val arrayAmbullance = resources.getStringArray(R.array.ambullance)
        val arrayState = resources.getStringArray(R.array.state)

        val genderString = when(report.personInjuredGender){
            0-> arrayGender[0]
            1-> arrayGender[1]
            else -> "N/A"
        }

        val placeString = when(report.placeOfAttention){
            0-> arrayPlace[0]
            1-> arrayPlace[1]
            2-> arrayPlace[2]
            else->"N/A"
        }

        val ambulanceString = when(report.ambulance){
            0-> arrayAmbullance[0]
            1-> arrayAmbullance[1]
            2-> arrayAmbullance[2]
            else->"N/A"
        }
        val stateString = when(report.state){
            0-> arrayState[0]
            1-> arrayState[1]
            2-> arrayState[2]
            else->"N/A"
        }

        //this variables are created like this so Android Studio will not trigger a warning about android's translation service
        val name = "${resources.getString(R.string.person_injured_name)}: ${report.personInjuredName}"
        val gender = "${resources.getString(R.string.gender_of_the_person_injured)}: $genderString"
        val place = "${resources.getString(R.string.place_of_attention)}: $placeString"
        val ambulance = "${resources.getString(R.string.was_necessary_an_ambulance)}: $ambulanceString"
        val date = "${resources.getString(R.string.date)}: ${report.date}"
        val state = "${resources.getString(R.string.actual_state)}: $stateString"
        val loc = "${resources.getString(R.string.location)}: ${report.location}"
        val descrip = "${resources.getString(R.string.description)}: ${report.description}"

        v?.let {
            it.findViewById<TextView>(R.id.location).text = loc
            it.findViewById<TextView>(R.id.nameOfPerson).text = name
            it.findViewById<TextView>(R.id.genderOfPerson).text = gender
            it.findViewById<TextView>(R.id.description_report_viewer).text = descrip
            it.findViewById<TextView>(R.id.placeAttention).text = place
            it.findViewById<TextView>(R.id.ambulance).text = ambulance
            it.findViewById<TextView>(R.id.tv_date).text = date
            it.findViewById<TextView>(R.id.severity).text = state
            it.findViewById<Button>(R.id.map_btn).setOnClickListener {
                val mIntent = Intent(activity, MapsActivity::class.java)
                mIntent.putExtra(AppKey.latitude, report.latitude)
                mIntent.putExtra(AppKey.longitude, report.longitude)
                startActivity(mIntent)
            }

            if(report.pictures.isNotEmpty()){
                if(report.pictures.size == 1 ){
                    if(report.pictures[0].isNotEmpty()){
                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[0]}",
                            it.findViewById<RelativeLayout>(R.id.photo1V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                    }
                }

                if(report.pictures.size == 2){
                    if(report.pictures[1].isNotEmpty()){

                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[0]}",
                            it.findViewById<RelativeLayout>(R.id.photo1V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[1]}",
                            it.findViewById<RelativeLayout>(R.id.photo2V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                    }
                }
                if(report.pictures.size == 3){
                    if(report.pictures[2].isNotEmpty()){

                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[0]}",
                            it.findViewById<RelativeLayout>(R.id.photo1V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[1]}",
                            it.findViewById<RelativeLayout>(R.id.photo2V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                        Glider.load(
                            "${ServerInfo.baseURL}${report.pictures[2]}",
                            it.findViewById<RelativeLayout>(R.id.photo3V)
                                .findViewById<ImageView>(R.id.list_photo)
                        )
                    }
                }
            }


        }

        return v
    }

}