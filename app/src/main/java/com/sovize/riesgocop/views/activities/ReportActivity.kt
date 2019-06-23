package com.sovize.riesgocop.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R
import com.sovize.riesgocop.controlers.firebase.MasterCrud
import com.sovize.riesgocop.models.AccidentReport
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.Document
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.utilities.system.FileManager
import com.sovize.riesgocop.utilities.system.PermissionRequester
import com.sovize.riesgocop.viewmodels.ViewModelReportActivity
import com.sovize.riesgocop.views.adapters.ReportPhotoAdapter
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val permission = PermissionRequester()
    private val master = MasterCrud()
    private val fileKeeper = FileManager()
    //anchorView es solo una vista cualquiera que sirve para obtener contexto para las notificaciones
    private var anchorView: View? = null
    private lateinit var mvReport: ViewModelReportActivity
    private lateinit var progress: TextView
    private lateinit var severityValue: String
    private lateinit var genderValue: String
    private lateinit var occupationValue: String
    private lateinit var attentionValue: String
    private lateinit var ambulanceValue: String

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
                Snackbar.make(it, getString(R.string.permissiond), Snackbar.LENGTH_LONG).show()
            }
        }
        findViewById<Spinner>(R.id.spinner_severity).onItemSelectedListener = this
        findViewById<Spinner>(R.id.spinner_personInjuredGender).onItemSelectedListener = this
        findViewById<Spinner>(R.id.spinner_personInjuredType).onItemSelectedListener = this
        findViewById<Spinner>(R.id.spinner_attentionPlace).onItemSelectedListener = this
        findViewById<Spinner>(R.id.spinner_ambullance).onItemSelectedListener = this

        spinners()

        findViewById<Button>(R.id.upload).setOnClickListener {
            if (et_location_report.text.isNotEmpty() && et_descripcion_report.text.isNotEmpty()) {
                createReport()
            } else {
                Snackbar.make(findViewById(R.id.formTitle), "Campos incompletos", Snackbar.LENGTH_LONG).show()
            }
        }
        progress = findViewById(R.id.tv_uour_pics)
        rv_photos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ReportActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ReportPhotoAdapter(mvReport.photoList)
        }
        mvReport.progressed.forEachIndexed { index, data ->
            data.observe(this@ReportActivity, Observer<Int> { percentage ->
                rv_photos.getChildAt(index).findViewById<ProgressBar>(R.id.progress).progress = percentage
            })
        }
    }

    /**
     * this function is only use to assign the options to the spiners
     */
    private fun spinners() {
        val severitySpinner = findViewById<Spinner>(R.id.spinner_severity)
        val genderSpinner = findViewById<Spinner>(R.id.spinner_personInjuredGender)
        val occupationSpinner = findViewById<Spinner>(R.id.spinner_personInjuredType)
        val placeSpinner = findViewById<Spinner>(R.id.spinner_attentionPlace)
        val ambullanceSpinner = findViewById<Spinner>(R.id.spinner_ambullance)

        val adapterS =
            ArrayAdapter.createFromResource(this@ReportActivity, R.array.severity, android.R.layout.simple_spinner_item)
        adapterS.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        severitySpinner.adapter = adapterS

        val adapterG =
            ArrayAdapter.createFromResource(this@ReportActivity, R.array.gender, android.R.layout.simple_spinner_item)
        adapterG.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        genderSpinner.adapter = adapterG

        val adapterO = ArrayAdapter.createFromResource(
            this@ReportActivity,
            R.array.ocuppation,
            android.R.layout.simple_spinner_item
        )
        adapterG.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        occupationSpinner.adapter = adapterO

        val adapterP = ArrayAdapter.createFromResource(
            this@ReportActivity,
            R.array.placeOfAttention,
            android.R.layout.simple_spinner_item
        )
        adapterG.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        placeSpinner.adapter = adapterP


        val adapterA = ArrayAdapter.createFromResource(
            this@ReportActivity,
            R.array.ambullance,
            android.R.layout.simple_spinner_item
        )
        adapterG.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        ambullanceSpinner.adapter = adapterA
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_severity -> {
                severityValue = spinner_severity.selectedItem.toString()
            }
            R.id.spinner_personInjuredGender -> {
                genderValue = spinner_personInjuredGender.selectedItem.toString()
            }
            R.id.spinner_personInjuredType -> {
                occupationValue = spinner_personInjuredType.selectedItem.toString()
            }
            R.id.spinner_attentionPlace -> {
                attentionValue = spinner_attentionPlace.selectedItem.toString()
            }
            R.id.spinner_ambullance -> {
                ambulanceValue = spinner_ambullance.selectedItem.toString()
            }
        }
    }

    private fun createReport() {

        val location = findViewById<EditText>(R.id.et_location_report).text.toString()
        val personName = findViewById<EditText>(R.id.et_personInjured).text.toString()
        val descant = findViewById<EditText>(R.id.et_descripcion_report).text.toString()
        val report = AccidentReport(
            location = location,
            personInjuredName = personName,
            personInjuredGender = genderValue,
            accidentedPersonType = occupationValue,
            description = descant,
            severityLevel = severityValue,
            placeOfAttention = attentionValue,
            ambullance = ambulanceValue,
            pictures = mvReport.photoUrlList
        )
        master.insert(Document.accident, report) {
            Log.d(AppLogger.reportActivity, "se creo $it")
            Snackbar.make(
                anchorView!!,
                if (it) getString(R.string.succes1)
                else getString(R.string.failure1)
                , Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ResponseCodes.takeCoverPhotoRequest -> {
                    Log.d(AppLogger.reportActivity, mvReport.cPhoto)
                    mvReport.apply {
                        if (cPhoto != "") {
                            photoList.add(cPhoto)
                            rv_photos.adapter?.notifyDataSetChanged()
                            uploadNewPhoto(cPhoto).observe(this@ReportActivity, Observer<Int> { percentage ->
                                rv_photos.getChildAt(progressed.size - 1).findViewById<ProgressBar>(R.id.progress)
                                    .progress =
                                    percentage
                            })
                            cPhoto = ""
                        }
                    }
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
             * quemado pero se genera un report ID aleatorio basado en el momento que se crea el reporte
             */
            val workingDir = fileKeeper.createImageFile()
            if (workingDir != "") {
                Log.d(AppLogger.reportActivity, "Directorio de trabajo de la foto: $workingDir")
                mvReport.cPhoto = workingDir
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileKeeper.getUri(workingDir, this))
                        startActivityForResult(takePictureIntent, ResponseCodes.takeCoverPhotoRequest)
                    }
                }
            } else {
                Snackbar.make(anchorView!!, getString(R.string.failure2), Snackbar.LENGTH_SHORT).show()
            }
        } else {
            permission.askExtStoragePermission(this)
        }
    }
}