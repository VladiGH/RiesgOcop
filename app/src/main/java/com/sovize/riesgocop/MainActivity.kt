package com.sovize.riesgocop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.activities.Login
import com.sovize.riesgocop.views.activities.ProfileActivity
import com.sovize.riesgocop.views.fragments.IssuesList
import com.sovize.riesgocop.views.fragments.QuickBar
import com.sovize.riesgocop.views.activities.ReportActivity


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val quickBar = QuickBar()
    private val issueFragment = IssuesList()
    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var vmMain: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        vmMain = ViewModelProviders.of(this).get(ViewModelMainActivity::class.java)
        supportFragmentManager.beginTransaction().replace(R.id.quickBar, quickBar).commit()
        supportFragmentManager.beginTransaction().replace(R.id.issue_list, issueFragment).commit()
        setSupportActionBar(findViewById(R.id.mainBar))

        findViewById<FloatingActionButton>(R.id.plus).setOnClickListener {
            onPLus()
        }
        findViewById<ImageView>(R.id.app_bar_pic).setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
        if(user != null){
            Glider.loadCircle(user.photoUrl.toString(), findViewById(R.id.app_bar_pic), R.drawable.profile)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {

            }

        }
        Log.i(AppLogger.mainActivity, item.itemId.toString())
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ResponseCodes.login -> {
                if (resultCode == RESULT_OK) {
                    startActivity(Intent(this, ReportActivity::class.java))
                }
            }
        }
    }

    private fun onPLus() {
        if (user == null) {
            startActivityForResult(Intent(this, Login::class.java), ResponseCodes.login)
        } else {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

}
