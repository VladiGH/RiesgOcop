package com.sovize.ultracop.views.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.ServerInfo
import com.sovize.ultracop.views.activities.MapsActivity

private const val ARG_PARAM1 = "param1"


class ReportAdminFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: AccidentReport? = null
    private val masterCrud = MasterCrud()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arrayGender = resources.getStringArray(R.array.gender)
        val arrayPlace = resources.getStringArray(R.array.placeOfAttention)
        val arrayAmbullance = resources.getStringArray(R.array.ambullance)
        val arrayState = resources.getStringArray(R.array.state)

        val genderString = when(param1?.personInjuredGender){
            0-> arrayGender[0]
            1-> arrayGender[1]
            else -> "N/A"
        }

        val placeString = when(param1?.placeOfAttention){
            0-> arrayPlace[0]
            1-> arrayPlace[1]
            2-> arrayPlace[2]
            else->"N/A"
        }

        val ambulanceString = when(param1?.ambulance){
            0-> arrayAmbullance[0]
            1-> arrayAmbullance[1]
            2-> arrayAmbullance[2]
            else->"N/A"
        }
        val stateString = when(param1?.state){
            0-> arrayState[0]
            1-> arrayState[1]
            2-> arrayState[2]
            3-> arrayState[3]
            else->"N/A"
        }

        val name = "${resources.getString(R.string.person_injured_name)}: ${param1?.personInjuredName}"
        val gender = "${resources.getString(R.string.gender_of_the_person_injured)}: $genderString"
        val place = "${resources.getString(R.string.place_of_attention)}: $placeString"
        val ambulance = "${resources.getString(R.string.was_necessary_an_ambulance)}: $ambulanceString"
        val loc = "${resources.getString(R.string.location)}: ${param1?.location}"
        val descrip = "${resources.getString(R.string.description)}: ${param1?.description}"

        val date = "${resources.getString(R.string.date)}: ${param1?.date}"
        val state = "${resources.getString(R.string.actual_state)}: $stateString"

        val v = inflater.inflate(R.layout.fragment_report_admin, container, false)
        v.findViewById<TextView>(R.id.nameOfPerson).text = name
        v.findViewById<TextView>(R.id.location).text = loc
        v.findViewById<TextView>(R.id.genderOfPerson).text = gender
        v.findViewById<TextView>(R.id.description_report_viewer).text = descrip
        v.findViewById<TextView>(R.id.placeAttention).text = place
        v.findViewById<TextView>(R.id.ambulance).text = ambulance
        v.findViewById<TextView>(R.id.tv_date).text = date
        v.findViewById<TextView>(R.id.tv_state).text = state

        val spinnerState = v.findViewById<Spinner>(R.id.spinner_state)
        val adapterA = ArrayAdapter.createFromResource(
            v.context,
            R.array.state,
            android.R.layout.simple_spinner_item
        )
        adapterA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerState.adapter = adapterA

        if(param1!!.pictures.isNotEmpty()){
            if(param1!!.pictures.size == 1 ){
                if(param1!!.pictures[0].isNotEmpty()){
                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[0]}",
                        v.findViewById<RelativeLayout>(R.id.photo1F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                }
            }

            if(param1!!.pictures.size == 2){
                if(param1!!.pictures[1].isNotEmpty()){

                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[0]}",
                        v.findViewById<RelativeLayout>(R.id.photo1F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[1]}",
                        v.findViewById<RelativeLayout>(R.id.photo2F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                }
            }
            if(param1!!.pictures.size == 3){
                if(param1!!.pictures[2].isNotEmpty()){

                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[0]}",
                        v.findViewById<RelativeLayout>(R.id.photo1F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[1]}",
                        v.findViewById<RelativeLayout>(R.id.photo2F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                    Glider.load(
                        "${ServerInfo.baseURL}${param1!!.pictures[2]}",
                        v.findViewById<RelativeLayout>(R.id.photo3F)
                            .findViewById<ImageView>(R.id.list_photo)
                    )
                }
            }
        }
        val spi = v.findViewById<Spinner>(R.id.spinner_state)
        v.findViewById<Button>(R.id.btn_update_state).setOnClickListener {
            masterCrud.updateReport(param1!!, spi.selectedItemPosition).addOnSuccessListener {
                Snackbar.make(v.findViewById<Button>(R.id.map_btn),resources.getString(R.string.update), Snackbar.LENGTH_LONG).show()
            }.addOnFailureListener {
                Snackbar.make(v.findViewById<Button>(R.id.map_btn),resources.getString(R.string.failure4), Snackbar.LENGTH_LONG).show()
            }

        }
        v.findViewById<Button>(R.id.map_btn).setOnClickListener {
            val mIntent = Intent(activity, MapsActivity::class.java)
            mIntent.putExtra(AppKey.latitude, param1!!.latitude)
            mIntent.putExtra(AppKey.longitude, param1!!.longitude)
            startActivity(mIntent)
        }
        return v
    }


    companion object {

        //factory method to past in a new AccidentReport as a parcelable
        @JvmStatic
        fun newInstance(param1: AccidentReport) =
            ReportAdminFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}
