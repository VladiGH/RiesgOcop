package com.sovize.riesgocop

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sovize.riesgocop.adapters.ReportAdapter
import com.sovize.riesgocop.fragments.QuickBar
import com.sovize.riesgocop.models.Report
import kotlinx.android.synthetic.main.user_profile.*


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private var reportsDB = FirebaseFirestore.getInstance()
    private var reportList = mutableListOf<Report>()
    private lateinit var viewAdapter: ReportAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        getReportsData()
    }

    fun getReportsData() {
        reportsDB.collection("Reports")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reportList.clear()
                    for (document in task.result!!) {
                        Log.d(ContentValues.TAG, "id: " + document.id + " => " + document.data)
                        reportList.add(
                            Report(
                                document.get("id").toString(), document.get("title").toString(),
                                document.get("danger").toString()
                            )
                        )
                        //initRecycler(reportList)
                        reportList.forEach {
                            reportItemClicked(it)
                        }
                    }

                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)

                }
                val quickBar = QuickBar.newInstance((reportList.size-3).toString(), (reportList.size-1).toString(), reportList.size.toString())
                supportFragmentManager.beginTransaction().replace(R.id.quickBar, quickBar).commit()
            }
    }

    private fun initRecycler(report: MutableList<Report>) {
        viewManager = LinearLayoutManager(this)
        viewAdapter = ReportAdapter(report) { reportItem: Report -> reportItemClicked(reportItem) }

        rv_report_container.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun reportItemClicked(item: Report) {
        Log.d("Reporte: ", item.title)
    }
}
