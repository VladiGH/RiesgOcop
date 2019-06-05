package com.sovize.riesgocop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.fragments.IssuesList
import com.sovize.riesgocop.views.fragments.QuickBar
import com.sovize.riesgocop.views.activities.ReportActivity


class MainActivity : AppCompatActivity() {

    private val quickBar = QuickBar()
    private val issueFragment = IssuesList()
    private lateinit var vmMain: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        vmMain = ViewModelProviders.of(this).get(ViewModelMainActivity::class.java)
        supportFragmentManager.beginTransaction().replace(R.id.quickBar, quickBar).commit()
        supportFragmentManager.beginTransaction().replace(R.id.issue_list, issueFragment).commit()
        findViewById<FloatingActionButton>(R.id.plus).setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

}
