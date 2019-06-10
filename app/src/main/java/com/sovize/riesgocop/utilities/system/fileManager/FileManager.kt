package com.sovize.riesgocop.utilities.system.fileManager

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FileManager {

    private val tag = "FileManager"

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    private fun getPublicStorageDir(secuense: Int): File? {
        // Get the directory for the user's public pictures directory.
        val date = Date()
        val formatDate = SimpleDateFormat("yyyy/MM/dd")
        val file = File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "sovize/${formatDate.format(date)}/casa_$secuense")
        file.mkdirs()
        if (!file.mkdirs()) {
            Log.w(tag, "Directory not created")
        }
        Log.d(tag, "this is the path: ${file.absolutePath}")
        return file
    }

    /**
     * @copied_from
     * @URL: https://medium.com/@rodrigolmti/android-get-camera-thumbnail-and-full-image-1bddfdc5347e
     */

    fun createImageFile(myNumber: Int): String {

        var tempDir = ""

        if(isExternalStorageWritable()){
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val file = File.createTempFile(timeStamp,".jpg", getPublicStorageDir(myNumber))

            tempDir = file.absolutePath
        }
        return tempDir
    }

    fun getUri(path: String, context: Context): Uri {
        Log.d("aca toy, prro", "antes de morir")
        return FileProvider.getUriForFile(context, "com.sovize.riesgocop.utilities.system.fileManager.FileManager", File(path))
    }

}