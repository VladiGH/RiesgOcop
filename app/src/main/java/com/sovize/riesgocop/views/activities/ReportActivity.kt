package com.sovize.riesgocop.views.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
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
        findViewById<Button>(R.id.upload).setOnClickListener { createReport() }
    }

    private fun createReport() {
        val title = findViewById<EditText>(R.id.et_name_report).text.toString()
        val descant = findViewById<EditText>(R.id.et_descripcion_report).text.toString()
        val location = findViewById<EditText>(R.id.et_ubicacion_report).text.toString()
        val danger = findViewById<EditText>(R.id.et_peligro_report).text.toString().toLong()
        val report = Report(
            id = "",
            title = title,
            danger = danger,
            description = descant,
            location = location,
            pictures = arrayOf("xd", "no", "implement", "yet")
        )
        Log.d(AppLogger.reportActivity, "se creo $report")
    }
}