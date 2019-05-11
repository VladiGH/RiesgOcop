package com.sovize.riesgocop

import android.content.ContentValues
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sovize.riesgocop.activities.Login
import com.sovize.riesgocop.adapters.ReportAdapter
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.network.Glider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private var user = FirebaseAuth.getInstance().currentUser
    private var reportsDB = FirebaseFirestore.getInstance()
    private var reportList = mutableListOf<Report>()

    private lateinit var viewAdapter: ReportAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            startActivity(Intent(this@MainActivity, Login::class.java))

        }
        setContentView(R.layout.activity_main)

        setUserData()
        getReportsData()
        findViewById<Button>(R.id.btn_log_out).setOnClickListener{
            FirebaseAuth.getInstance().signOut()
        }

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

                    }

                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)

                }
                initRecycler(reportList)
            }
    }

    fun initRecycler(report: MutableList<Report>){
        viewManager = LinearLayoutManager(this)
        viewAdapter = ReportAdapter(report, {reportItem: Report -> reportItemClicked(reportItem)})

        rv_report_container.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
    fun reportItemClicked(item: Report){
        Log.d("Reporte: ", item.title)
    }


    private fun setUserData(){
        findViewById<TextView>(R.id.tv_user_name).text = user?.displayName?:getString(R.string.user_not_found)
        findViewById<TextView>(R.id.tv_user_email).text = user?.email?:getString(R.string.user_not_found)
        findViewById<TextView>(R.id.tv_user_key).text = user?.uid?:getString(R.string.user_not_found)
        Glider.load(this.baseContext, user?.photoUrl?.toString()?:"", findViewById(R.id.iv_user_picture))
    }
}
