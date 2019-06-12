package com.sovize.riesgocop.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import com.sovize.riesgocop.controlers.network.retrofit.HttpRetroClient
import com.sovize.riesgocop.utilities.AppLogger
import java.io.File

class ViewModelReportActivity : ViewModel() {

    val client = HttpRetroClient()
    val photoList = mutableListOf<String>()
    val photoUrlList = mutableListOf<String>()
    var tempPhoto = ""

    fun uploadNewPhoto() {
        photoList.add(tempPhoto)
        Log.d(AppLogger.reportActivity, "la data es $tempPhoto")
        client.uploadPhoto(File(tempPhoto))
    }

    fun getAllPhotos(): MutableList<String>{
        return photoList
    }
}