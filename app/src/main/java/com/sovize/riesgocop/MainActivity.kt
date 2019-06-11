package com.sovize.riesgocop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.sovize.riesgocop.controlers.network.Glider
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.activities.Login
import com.sovize.riesgocop.views.activities.ProfileActivity
import com.sovize.riesgocop.views.activities.ReportActivity
import com.sovize.riesgocop.views.fragments.IssuesList
import com.sovize.riesgocop.views.fragments.QuickBar
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val quickBar = QuickBar()
    private val issueFragment = IssuesList()
    private lateinit var vmMain: ViewModelMainActivity
    private var user: FirebaseUser? = null
    private val userObserver = Observer<FirebaseUser> { fireBaseUser ->
        if (fireBaseUser != null) {
            user = fireBaseUser
            Glider.loadCircle(fireBaseUser.photoUrl.toString(), findViewById(R.id.app_bar_pic), R.drawable.profile)
        } else {
            user = null
            findViewById<ImageView>(R.id.app_bar_pic).setImageResource(R.drawable.profile)
        }
    }

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
        vmMain.getUserData().observe(this, userObserver)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, findViewById(R.id.mainBar), R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }
/*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {

            }

        }
        Log.i(AppLogger.mainActivity, item.itemId.toString())
        return true
    }
*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vmMain.setUserState()
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

    override fun onResume() {
        super.onResume()
        vmMain.setUserState()
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    // TODO (14.2) Funcion que recibe el ID del elemento tocado
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {

            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_logOut -> {

            }
        }

        // TODO (15) Cuando se da click a un opcion del menu se cierra de manera automatica
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
