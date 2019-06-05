package com.sovize.riesgocop.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R
import com.sovize.riesgocop.firebase.ReportDao
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.system.PermissionRequester

class ReportActivity : AppCompatActivity() {

    private val permission = PermissionRequester()
    private val reportDao = ReportDao()
    private lateinit var view: View
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        view = findViewById(R.id.takePicture)
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
            pictures = listOf("xd", "no", "implement", "yet")
        )

        reportDao.insertReport(report) {
            Log.d(AppLogger.reportActivity, "se creo $it")
            Snackbar.make(
                view,
                if (it) "Reporte ingresado"
                else "Reporte no ingresado"
                , Snackbar.LENGTH_LONG
            ).show()

        }
    }
}