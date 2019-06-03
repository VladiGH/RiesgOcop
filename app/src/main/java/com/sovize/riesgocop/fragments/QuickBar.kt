package com.sovize.riesgocop.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sovize.riesgocop.R
import com.sovize.riesgocop.utilities.AppKey

class QuickBar : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(AppKey.newIt)
            param2 = it.getString(AppKey.onIt)
            param3 = it.getString(AppKey.offIt)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.quickbar_layout, container, false)
        view.findViewById<TextView>(R.id.newIssueValue).text = param1
        view.findViewById<TextView>(R.id.in_process).text = param2
        view.findViewById<TextView>(R.id.close_issue_value).text = param3
        return view
    }


    companion object {

        @JvmStatic
        fun newInstance(newIssue: String, oldIssue: String, closeIssue: String) =
            QuickBar().apply {
                arguments = Bundle().apply {
                    putString(AppKey.newIt, newIssue)
                    putString(AppKey.onIt, oldIssue)
                    putString(AppKey.offIt, closeIssue)
                }
            }
    }
}
