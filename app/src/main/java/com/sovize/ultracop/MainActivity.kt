package com.sovize.ultracop

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.sovize.ultracop.controlers.network.Glider
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.ResponseCodes
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import com.sovize.ultracop.views.activities.Login
import com.sovize.ultracop.views.activities.ProfileActivity
import com.sovize.ultracop.views.activities.ReportActivity
import com.sovize.ultracop.views.fragments.IssuesList
import com.sovize.ultracop.views.fragments.QuickBar
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val quickBar = QuickBar()
    private val issueFragment = IssuesList()
    private lateinit var vmMain: ViewModelMainActivity
    private var cUser: User? = null
    private val userObserver = Observer<User> { user ->
        cUser = user
        cUser.apply {
            if (this != null) {
                Glider.loadCircle(
                    user.firebaseUser?.photoUrl.toString(),
                    findViewById(R.id.app_bar_pic),
                    R.drawable.profile
                )
                user.permission.forEach {
                    Log.d(AppLogger.mainActivity, "Permission : $it")
                }
            } else {
                findViewById<ImageView>(R.id.app_bar_pic).setImageResource(R.drawable.profile)
            }
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
            onPLus(it)
        }
        findViewById<ImageView>(R.id.app_bar_pic).setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
        vmMain.getUserData().observe(this, userObserver)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            findViewById(R.id.mainBar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //Creating notification channel for android 8 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notyName)
            val descriptionText = getString(R.string.notyDesc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(getString(R.string.chnenlsid), name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
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
        when (requestCode) {
            ResponseCodes.login -> {
                if (resultCode == RESULT_OK) {
                    startActivity(Intent(this, ReportActivity::class.java))
                }
            }
        }
    }

    private fun onPLus(ref: View) {
        if (cUser == null) {
            startActivityForResult(Intent(this, Login::class.java), ResponseCodes.login)
        } else {
            if (cUser?.permission?.contains("w")!!) {
                startActivity(Intent(this, ReportActivity::class.java))
            } else {
                AlertDialog.Builder(ref.context)
                    .setTitle(getString(R.string.errortypeacct1))
                    .setMessage(getString(R.string.errortypeacct2))
                    .setNeutralButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }.create().show()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    //Funcion que recibe el ID del elemento tocado
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
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
