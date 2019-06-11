package com.sovize.riesgocop.utilities.system

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.sovize.riesgocop.utilities.AppLogger
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FileManager {

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
            Environment.DIRECTORY_PICTURES), "riesgocop/${formatDate.format(date)}/report_$secuense")
        file.mkdirs()
        if (!file.mkdirs()) {
            Log.w(AppLogger.fileManager, "Directory not created")
        }
        Log.d(AppLogger.fileManager, "this is the path: ${file.absolutePath}")
        return file
    }


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
        Log.d(AppLogger.fileManager, "antes de morir")
        return FileProvider.getUriForFile(context, "com.sovize.riesgocop.utilities.system.FileManager", File(path))
    }

}