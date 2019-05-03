package com.proyecto.riesgocop.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.riesgocop.R
import com.proyecto.riesgocop.system.PermissionRequester

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