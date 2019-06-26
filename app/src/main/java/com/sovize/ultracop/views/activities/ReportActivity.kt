package com.sovize.ultracop.views.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.ultracop.R
import com.sovize.ultracop.controlers.firebase.MasterCrud
import com.sovize.ultracop.models.AccidentReport
import com.sovize.ultracop.models.User
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.Document
import com.sovize.ultracop.utilities.ResponseCodes
import com.sovize.ultracop.utilities.system.FileManager
import com.sovize.ultracop.utilities.system.PermissionRequester
import com.sovize.ultracop.viewmodels.ViewModelMainActivity
import com.sovize.ultracop.viewmodels.ViewModelReportActivity
import kotlinx.android.synthetic.main.activity_report.*
import java.text.SimpleDateFormat
import java.util.*


class ReportActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, LocationListener {


    private val permission = PermissionRequester()
    private val master = MasterCrud()
    private val fileKeeper = FileManager()
    //anchorView is just any random view that is use as anchor for the SnackBar to show up
    private var anchorView: View? = null
    private lateinit var mvReport: ViewModelReportActivity
    private lateinit var occupationValue: String
    private lateinit var attentionValue: String
    private lateinit var ambulanceValue: String
    var formatFecha = SimpleDateFormat("dd-MM-yy")
    private var cUser: User? = null
    private var uidUser: String? = ""
    private val locationRequestCode = 101
    private var longitudeM: Double = -89.054
    private var latitudeM: Double = 32.055
    private var cPhoto = ""
    private lateinit var location: LocationManager

    override fun onLocationChanged(location: Location) {
        longitudeM = location.longitude
        latitudeM = location.latitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
        Toast.makeText(this@ReportActivity, resources.getString(R.string.gps_provider_on), Toast.LENGTH_LONG).show()
    }

    override fun onProviderDisabled(provider: String?) {
        Toast.makeText(this@ReportActivity, resources.getString(R.string.gps_provider_off), Toast.LENGTH_LONG).show()
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
                Snackbar.make(findViewById(R.id.formTitle), getString(R.string.fields), Snackbar.LENGTH_LONG).show()
            }
        }

        mvReport.progressed[0]
            .observe(this@ReportActivity, Observer<Int> { percentage ->
                findViewById<RelativeLayout>(R.id.photo1)
                    .findViewById<ProgressBar>(R.id.progress)
                    .progress = percentage
            })

        mvReport.progressed[1]
            .observe(this@ReportActivity, Observer<Int> { percentage ->
                findViewById<RelativeLayout>(R.id.photo1)
                    .findViewById<ProgressBar>(R.id.progress)
                    .progress = percentage
            })

        mvReport.progressed[2]
            .observe(this@ReportActivity, Observer<Int> { percentage ->
                findViewById<RelativeLayout>(R.id.photo1)
                    .findViewById<ProgressBar>(R.id.progress)
                    .progress = percentage
            })

        findViewById<Button>(R.id.select_photos).setOnClickListener {
            startActivity(Intent(this@ReportActivity, MapsActivity::class.java))
        }
        val vmMain = ViewModelProviders.of(this).get(ViewModelMainActivity::class.java)

        val userObserver = Observer<User> { user ->
            cUser = user
            cUser.apply {
                if (this != null) {
                    uidUser = FirebaseAuth.getInstance().currentUser?.uid
                } else {
                    Snackbar.make(anchorView!!, getString(R.string.therno), Snackbar.LENGTH_LONG).show()
                }
            }
        }
        vmMain.getUserData().observe(this, userObserver)
        val switchGPS = findViewById<Switch>(R.id.gps_switch)

        switchGPS.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkPermission()
            }
        }

        mvReport.photoList.forEachIndexed { index, data ->
            val bitmap = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(data), 400, 300
            )
            when (index) {
                0 -> {
                    findViewById<RelativeLayout>(R.id.photo1)
                        .findViewById<ImageView>(R.id.list_photo)
                        .setImageBitmap(bitmap)
                }
                1 -> {
                    findViewById<RelativeLayout>(R.id.photo2)
                        .findViewById<ImageView>(R.id.list_photo)
                        .setImageBitmap(bitmap)
                }
                2 -> {
                    findViewById<RelativeLayout>(R.id.photo3)
                        .findViewById<ImageView>(R.id.list_photo)
                        .setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun getLocation() {
        try {
            location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationRequestCode
            )
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
            ArrayAdapter.createFromResource(
                this@ReportActivity,
                R.array.severity,
                android.R.layout.simple_spinner_item
            )
        adapterS.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        severitySpinner.adapter = adapterS

        val adapterG =
            ArrayAdapter.createFromResource(
                this@ReportActivity,
                R.array.gender,
                android.R.layout.simple_spinner_item
            )
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
        val date = formatFecha.format(Date()).toString()
        val report = AccidentReport(
            location = location,
            personInjuredName = personName,
            personInjuredGender = spinner_personInjuredGender.selectedItem.toString(),
            accidentedPersonType = occupationValue,
            description = descant,
            severityLevel = spinner_severity.selectedItem.toString(),
            placeOfAttention = attentionValue,
            ambullance = ambulanceValue,
            pictures = mvReport.photoUrlList,
            date = date,
            user = uidUser!!,
            longitude = longitudeM,
            latitude = latitudeM
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
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ResponseCodes.takeCoverPhotoRequest -> {
                    Log.d(AppLogger.reportActivity, cPhoto)
                    if (cPhoto != "") {
                        val bitmap = ThumbnailUtils.extractThumbnail(
                            BitmapFactory.decodeFile(cPhoto), 400, 300
                        )
                        when (mvReport.uploaded) {
                            0 -> {
                                findViewById<RelativeLayout>(R.id.photo1)
                                    .findViewById<ImageView>(R.id.list_photo)
                                    .setImageBitmap(bitmap)
                            }
                            1 -> {
                                findViewById<RelativeLayout>(R.id.photo2)
                                    .findViewById<ImageView>(R.id.list_photo)
                                    .setImageBitmap(bitmap)
                            }
                            2 -> {
                                findViewById<RelativeLayout>(R.id.photo3)
                                    .findViewById<ImageView>(R.id.list_photo)
                                    .setImageBitmap(bitmap)
                            }
                        }
                        mvReport.uploadNewPhoto(cPhoto)
                        cPhoto = ""
                    }
                }
                else -> {
                    Log.d(AppLogger.reportActivity, "No photo taken")
                    Snackbar.make(
                        anchorView!!,
                        getString(R.string.nophton),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun takeCoverPic() {
        if (permission.hasExtStoragePermission(this)) {
            val workingDir = fileKeeper.createImageFile()
            if (workingDir != "") {
                Log.d(AppLogger.reportActivity, "Directorio de trabajo de la foto: $workingDir")
                cPhoto = workingDir
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            fileKeeper.getUri(workingDir, this)
                        )
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