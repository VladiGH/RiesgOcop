package com.sovize.riesgocop.views.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sovize.riesgocop.R
import com.sovize.riesgocop.utilities.system.PermissionRequester

class ReportActivity : AppCompatActivity() {

    private val permission = PermissionRequester()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        findViewById<Button>(R.id.takePicture).setOnClickListener {
            if (permission.hasExtStoragePermission(this)) {

            } else {
                permission.askExtStoragePermission(this)
            }
        }
    }
}