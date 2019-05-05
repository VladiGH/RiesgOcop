package com.sovize.riesgocop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sovize.riesgocop.activities.ReportActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_login)?.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, ReportActivity::class.java)
            )
        }
    }
}
