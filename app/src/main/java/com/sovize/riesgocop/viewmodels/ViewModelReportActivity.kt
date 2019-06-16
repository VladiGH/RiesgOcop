package com.sovize.riesgocop.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovize.riesgocop.controlers.network.retrofit.HttpRetroClient
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers.Progressive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ViewModelReportActivity : ViewModel() {

    private val client = HttpRetroClient()
    private val photoList = mutableListOf<String>()
    val photoUrlList = mutableListOf<String>()
    var tempPhoto = ""

    fun uploadNewPhoto(): LiveData<Int> {
        photoList.add(tempPhoto)
        val dataLive = MutableLiveData<Int>().apply { value = 0 }
        viewModelScope.launch(Dispatchers.IO) {
            val dir = client.uploadPhoto(File(tempPhoto),
                object : Progressive {

                    override fun onProgressUpdate(percentage: Int) {
                        dataLive.postValue(percentage)
                    }

                    override fun onError() {
                        dataLive.postValue(-1)
                    }

                    override fun onFinish() {
                        dataLive.postValue(100)
                    }
                })
            dir?.apply {
                photoUrlList.add(this)
            }
        }
        return dataLive
    }

    fun getAllPhotos(): MutableList<String> {
        return photoList
    }
}