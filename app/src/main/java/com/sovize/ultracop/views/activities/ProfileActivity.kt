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
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import com.sovize.ultracop.views.adapters.ReportAdapter
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {

    private val user = MutableLiveData<FirebaseUser>()
    var cont: Int = 0
    private var ArrayList: MutableList<AccidentReport>? = null
    private val masterCrud = MasterCrud()
    private lateinit var vmMain: ViewModelMainActivity
    private var viewAdapter: ReportAdapter? = null
    private val viewManager = LinearLayoutManager(this@ProfileActivity)
    private var currentUser: String = "N/A"


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

            } else {
                findViewById<Button>(R.id.logout).text = getString(R.string.log_out)
                Glider.loadCircle(
                    fire.photoUrl.toString(),
                    findViewById(R.id.user_profile_photo),
                    R.drawable.profile
                )
                findViewById<TextView>(R.id.user_profile_name).text = fire.email
                currentUser = fire.uid
                masterCrud.readWhereEq(Document.accident, "user", currentUser) {
                    it?.forEach {
                        ArrayList?.add(it.toObject(AccidentReport::class.java))
                        cont++
                        Snackbar.make(findViewById<TextView>(R.id.user_profile_name),"Numero de reportes al user: ${cont}"
                        , Snackbar.LENGTH_LONG).show()
                    }
/*            if (viewAdapter == null) {
                it?.documents?.size
                //initRecycler(it)
            } else {
                viewAdapter = ReportAdapter(it){reportItem -> reportItemClicked(reportItem)}
                rv_reports_profile.swapAdapter(viewAdapter,true)

            }*/
                }
            }
        })


    }
    private fun initRecycler(report: MutableList<AccidentReport>?) {
        viewAdapter = report?.let { ReportAdapter(it) { reportItem -> reportItemClicked(reportItem) } }
        ArrayList?.add(AccidentReport())
        rv_reports_profile.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


    private fun reportItemClicked(item: AccidentReport) {
        Log.d(AppLogger.issuesFragment, "${item.accidentedPersonType} + ${item.description}")
        val intent = Intent(this@ProfileActivity, ReportDetail::class.java)
        intent.putExtra(AppKey.reportInfo,item)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener {
        }
    }
}
