package com.sovize.riesgocop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.sovize.riesgocop.viewmodels.ViewModelMainActivity
import com.sovize.riesgocop.views.fragments.QuickBar


class MainActivity : AppCompatActivity() {

    private val quickBar = QuickBar()
    private lateinit var vmMain: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        vmMain = ViewModelProviders.of(this).get(ViewModelMainActivity::class.java)
        supportFragmentManager.beginTransaction().replace(R.id.quickBar, quickBar).commit()
    }

}
