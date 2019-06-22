package com.sovize.riesgocop.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.utilities.AppKey
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.activities.ReportDetail
import com.sovize.riesgocop.views.adapters.ReportAdapter
import kotlinx.android.synthetic.main.report_dash_list.*

class IssuesList : Fragment() {

    private lateinit var vmMain: ViewModelMainActivity
    private var viewAdapter: ReportAdapter? = null
    private val viewManager = LinearLayoutManager(this.context)
    private val observer = Observer<MutableList<AccidentReport>> {
        if (viewAdapter == null) {
            initRecycler(it)
        } else {
            viewAdapter = ReportAdapter(it){reportItem -> reportItemClicked(reportItem)}
            rv_list_issues.swapAdapter(viewAdapter,true)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vmMain = ViewModelProviders.of(activity!!).get(ViewModelMainActivity::class.java)
        vmMain.reportList.observe(this, observer)

        val view  =  inflater.inflate(R.layout.report_dash_list, container, false)

        return view
    }

    private fun initRecycler(report: MutableList<AccidentReport>) {
        viewAdapter = ReportAdapter(report) { reportItem -> reportItemClicked(reportItem) }

        rv_list_issues.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun reportItemClicked(item: AccidentReport) {
        Log.d(AppLogger.issuesFragment, "${item.accidentedPersonType} + ${item.description}")
        val intent = Intent(activity, ReportDetail::class.java)
        intent.putExtra(AppKey.reportInfo,item)
        startActivity(intent)
    }

}