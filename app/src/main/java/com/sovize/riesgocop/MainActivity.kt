package com.sovize.riesgocop

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.activities.Login
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
        //If horizonrtal caragar tu fragmento,
        //raplace for report deatisl
        //
        findViewById<FloatingActionButton>(R.id.plus).setOnClickListener {
            if (user == null) {
                onPLus()
            } else {
                FirebaseAuth.getInstance().signOut()
            }

        }
        issueFragment.callbackMethod = { report ->
            //myfragment.setFata(report)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {

            }

        }
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
