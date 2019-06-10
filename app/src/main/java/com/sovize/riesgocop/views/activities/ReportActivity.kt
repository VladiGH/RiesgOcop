package com.sovize.riesgocop.views.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sovize.riesgocop.R
import com.sovize.riesgocop.firebase.ReportDao
import com.sovize.riesgocop.models.Report
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ResponseCodes
import com.sovize.riesgocop.utilities.system.fileManager.FileManager
import com.sovize.riesgocop.utilities.system.PermissionRequester

class ReportActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    private val permission = PermissionRequester()
    private val reportDao = ReportDao()
    private lateinit var view: View
    private lateinit var imageViewPic: ImageView
    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var currentPhotoPath: String
    private var coverPhoto: String? = null
    private val fileKeeper = FileManager()
    private var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        view = findViewById(R.id.takePicture)
        findViewById<Button>(R.id.takePicture).setOnClickListener {
            if (permission.hasExtStoragePermission(this)) {
                takeCoverPic(findViewById(R.id.picture_preview))
            } else {
                permission.askExtStoragePermission(this)
                Snackbar.make(view, "No se tienen permisos suficientes",Snackbar.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ResponseCodes.takeCoverPhotoRequest -> {
                    val imageBitmap = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(coverPhoto), 400, 300)
                    findViewById<ImageView>(R.id.picture_preview).setImageBitmap(imageBitmap)
                }
                ResponseCodes.takePhotoRequest -> {
//                    val imageBitmap = data?.extras?.get("data") as Bitmap
//                    findViewById<ImageView>(R.id.cover).setImageBitmap(imageBitmap)
                    Log.d(AppLogger.reportActivity, "takePhotoRequest")
                }
                else -> {
                    Log.d(AppLogger.reportActivity, "supermonkey")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun takeCoverPic(view: View) {
        if (permission.hasExtStoragePermission(this)) {
            val workingDir = fileKeeper.createImageFile(counter)
            if (workingDir != "") {
                coverPhoto = workingDir
                Log.d(AppLogger.reportActivity, "Directorio de trabajo de la foto: $coverPhoto")
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileKeeper.getUri(coverPhoto!!,this))
                        startActivityForResult(takePictureIntent,  ResponseCodes.takeCoverPhotoRequest )
                    }
                }
            } else {
                Log.d(AppLogger.reportActivity, "External media is not writable")
            }
        } else {
            permission.askExtStoragePermission(this)
        }
    }
}