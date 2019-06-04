package com.sovize.riesgocop.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuickBar : Fragment() {

    private lateinit var vmMain: ViewModelMainActivity
    private val observer = Observer<MutableList<Report>> {
        view?.findViewById<TextView>(R.id.newIssueValue)?.text = (it.size - 2).toString()
        view?.findViewById<TextView>(R.id.in_process)?.text = (it.size - 1).toString()
        view?.findViewById<TextView>(R.id.close_issue_value)?.text = (it.size).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmMain = ViewModelProviders.of(activity!!).get(ViewModelMainActivity::class.java)
        vmMain.viewModelScope.launch {
            delay(1500)
            vmMain.getReportsData()
        }
        vmMain.reportList.observe(this, observer)
        return inflater.inflate(R.layout.quickbar_layout, container, false)
    }
}
