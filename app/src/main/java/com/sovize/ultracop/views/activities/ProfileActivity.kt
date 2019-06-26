package com.sovize.ultracop.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.utilities.AppKey
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document
import com.sovize.ultracop.views.adapters.ReportAdapter
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {

    private val user = MutableLiveData<FirebaseUser>()
    private val masterCrud = MasterCrud()
    private var viewAdapter: ReportAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        findViewById<Button>(R.id.logout).setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this, Login::class.java))
            } else {
                GoogleSignIn.getClient(
                    this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build()
                ).signOut()
                FirebaseAuth.getInstance()?.signOut()
                finish()
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener {
            user.value = it.currentUser
        }
        user.observe(this, Observer<FirebaseUser> { fire ->
            if (fire == null) {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_in)
                rv_reports_profile.swapAdapter(null, true)

            } else {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_out)
                Glider.loadCircle(
                    fire.photoUrl.toString(),
                    findViewById(R.id.user_profile_photo),
                    R.drawable.profile
                )
                findViewById<TextView>(R.id.user_profile_name).text = fire.email
                masterCrud.readWhereEq(Document.accident, "user", fire.uid) { snap ->
                    val myReportList = mutableListOf<AccidentReport>()
                    snap?.forEach {
                        myReportList.add(it.toObject(AccidentReport::class.java))
                    }
                    Snackbar.make(
                        findViewById<TextView>(R.id.user_profile_name),
                        "Numero de reportes al user: ${myReportList.size}"
                        ,
                        Snackbar.LENGTH_LONG
                    ).show()
                    initRecycler(myReportList)
                }
            }
        })
    }

    private fun initRecycler(accidents: MutableList<AccidentReport>) {
        viewAdapter = ReportAdapter(accidents) { reportItem -> reportItemClicked(reportItem) }
        rv_reports_profile.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = viewAdapter
        }
        viewAdapter?.notifyDataSetChanged()
    }


    private fun reportItemClicked(item: AccidentReport) {
        Log.d(AppLogger.issuesFragment, "${item.accidentedPersonType} + ${item.description}")
        val intent = Intent(this@ProfileActivity, ReportDetail::class.java)
        intent.putExtra(AppKey.reportInfo, item)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener {
        }
    }
}
