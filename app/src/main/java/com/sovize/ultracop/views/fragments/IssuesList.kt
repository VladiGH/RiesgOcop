package com.sovize.ultracop.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovize.ultracop.R.layout.report_dash_list
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import com.sovize.ultracop.views.activities.ReportDetail
import com.sovize.ultracop.views.adapters.ReportAdapter
import kotlinx.android.synthetic.main.report_dash_list.*


@Suppress("UNREACHABLE_CODE")
class IssuesList : Fragment() {

    //private var ArrayList: MutableList<AccidentReport>? = null

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

        val view  =  inflater.inflate(report_dash_list, container, false)

        return view
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun myFun() {
        context?.toast("Funka")
    }

    fun sortArrayList(ArrayList: MutableList<AccidentReport>) {
        //var ArrayList: MutableList<AccidentReport>? = null
        val sortedList = ArrayList.sortWith(compareBy { it.accidentedPersonType })
        return sortedList
    }

    private fun sortArrayList2(Array: MutableList<AccidentReport>) {
        Array.sortWith(Comparator { o1, o2 -> o1.accidentedPersonType.compareTo(o2.accidentedPersonType) })

    }

    private fun initRecycler(report: MutableList<AccidentReport>) {
        viewAdapter = ReportAdapter(report) { reportItem -> reportItemClicked(reportItem) }

        rv_list_issues.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        tv_title_filter.setOnClickListener {

        }
    }

    private fun reportItemClicked(item: AccidentReport) {
        Log.d(AppLogger.issuesFragment, "${item.accidentedPersonType} + ${item.description}")
        val intent = Intent(activity, ReportDetail::class.java)
        intent.putExtra(AppKey.reportInfo,item)
        startActivity(intent)
    }

}