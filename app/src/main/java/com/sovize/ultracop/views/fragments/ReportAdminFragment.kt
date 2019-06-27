package com.sovize.ultracop.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.models.AccidentReport

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
        val v = inflater.inflate(R.layout.fragment_report_admin, container, false)
        v.findViewById<TextView>(R.id.nameOfPerson).text = param1?.personInjuredName
        return v
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: AccidentReport) =
            ReportAdminFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}
