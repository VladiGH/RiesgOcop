package com.sovize.riesgocop.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.firebase.ReportDao
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.utilities.system.FileManager
import com.sovize.riesgocop.utilities.system.PermissionRequester
import com.sovize.riesgocop.viewmodels.ViewModelReportActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.views.adapters.ReportPhotoAdapter
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {

    private val permission = PermissionRequester()
    private val reportDao = ReportDao()
    private val fileKeeper = FileManager()
    private val counter = 0
    private var anchorView: View? = null
    private var viewPhotoAdapter: ReportPhotoAdapter? = null
    private val viewManager = LinearLayoutManager(this)
    private lateinit var mvReport: ViewModelReportActivity
    private lateinit var progress: TextView
    private val observer = Observer<Int>{
        progress.text = it?.run {
            if (this < 0) "ya se jodio"
            else "$it%"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            finish()
        }
        setContentView(R.layout.activity_report)
        mvReport = ViewModelProviders.of(this).get(ViewModelReportActivity::class.java)
        anchorView = findViewById<Button>(R.id.takePicture)
        anchorView?.setOnClickListener {
            if (permission.hasExtStoragePermission(this)) {
                takeCoverPic()
            } else {
                permission.askExtStoragePermission(this)
                Snackbar.make(it, "No se tienen permisos suficientes", Snackbar.LENGTH_LONG).show()
            }
        }
        findViewById<Button>(R.id.upload).setOnClickListener {
            if(et_location_report.text.isNotEmpty() && et_descripcion_report.text.isNotEmpty() &&
                et_peligro_report.text.isNotEmpty()){
                createReport()
            }
            else{
                Snackbar.make(findViewById(R.id.formTitle), "Campos incompletos", Snackbar.LENGTH_LONG).show()
            }
        }
        progress = findViewById(R.id.tv_uour_pics)
    }

    private fun createReport() {

        val location = findViewById<EditText>(R.id.et_location_report).text.toString()
        val personName = findViewById<EditText>(R.id.et_personInjured).text.toString()
        val personGender = findViewById<EditText>(R.id.et_personInjuredGender).text.toString()
        val personType = findViewById<EditText>(R.id.et_personInjuredType).text.toString()
        val descant = findViewById<EditText>(R.id.et_descripcion_report).text.toString()
        val danger = findViewById<EditText>(R.id.et_peligro_report).text.toString()
        val placeAttention = findViewById<EditText>(R.id.et_attentionPlace).text.toString()
        val ambullance = findViewById<EditText>(R.id.et_ambullance).text.toString()

        val report = AccidentReport(
            location = location,
            personInjuredName = personName,
            personInjuredGender = personGender,
            accidentedPersonType = personType,
            description = descant,
            SeverityLevel = danger,
            placeOfAttention = placeAttention,
            ambullance = ambullance,
            pictures = mvReport.photoUrlList
        )

        reportDao.insertReport(report) {
            Log.d(AppLogger.reportActivity, "se creo $it")
            Snackbar.make(
                anchorView!!,
                if (it) "Reporte ingresado"
                else "Reporte no ingresado"
                , Snackbar.LENGTH_LONG
            ).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ResponseCodes.takeCoverPhotoRequest -> {
                    mvReport.uploadNewPhoto().observe(this, observer)
                    initRecycler(mvReport.getAllPhotos())
                }
                else -> {
                    Log.d(AppLogger.reportActivity, "No photo taken")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun takeCoverPic() {
        if (permission.hasExtStoragePermission(this)) {
            /**
             * @counter es una variable para indicar el id del reporte, de momento es un 0
             *quemado pero se genera un report ID aleatorio basado en el momento que se crea el reporte
             */
            val workingDir = fileKeeper.createImageFile(counter)
            if (workingDir != "") {
                mvReport.tempPhoto = workingDir
                Log.d(AppLogger.reportActivity, "Directorio de trabajo de la foto: $workingDir")
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileKeeper.getUri(workingDir, this))
                        startActivityForResult(takePictureIntent, ResponseCodes.takeCoverPhotoRequest)
                    }
                }
            } else {
                Log.d(AppLogger.reportActivity, "External media is not writable")
            }
        } else {
            permission.askExtStoragePermission(this)
        }
    }
    private fun initRecycler(photo: MutableList<String>) {
        viewPhotoAdapter = ReportPhotoAdapter(photo)

        rv_photos.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewPhotoAdapter
        }
    }
}