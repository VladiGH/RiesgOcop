package com.sovize.ultracop.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.sovize.ultracop.R
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import kotlinx.coroutines.launch

class QuickBar : Fragment() {

    private lateinit var vmMain: ViewModelMainActivity
    private val observer = Observer<MutableList<AccidentReport>> {
        // this observer allow to query for real time count changes on the snapshot
        view?.findViewById<TextView>(R.id.newIssueValue)?.text = it.count { e -> e.state == 0 }.toString()
        view?.findViewById<TextView>(R.id.in_process)?.text = it.count { e -> e.state == 1 }.toString()
        view?.findViewById<TextView>(R.id.close_issue_value)?.text =
            it.count { e -> e.state == 2 || e.state == 3 }.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmMain = ViewModelProviders.of(activity!!).get(ViewModelMainActivity::class.java)
        vmMain.viewModelScope.launch {
            vmMain.getReportsData()
        }
        vmMain.reportList.observe(this, observer)
        return inflater.inflate(R.layout.quickbar_layout, container, false)
    }
}
