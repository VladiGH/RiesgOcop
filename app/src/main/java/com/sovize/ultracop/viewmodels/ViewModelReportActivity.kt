package com.sovize.ultracop.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovize.ultracop.controlers.network.retrofit.HttpRetroClient
import com.sovize.ultracop.controlers.network.retrofit.interfaces.drivers.Progressive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ViewModelReportActivity : ViewModel() {

    private val client = HttpRetroClient()
    var uploaded = 0
    val photoList = mutableListOf<String>()
    val photoUrlList = mutableListOf<String>()
    val progressed = ArrayList<MutableLiveData<Int>>()

    init {
        progressed.add(MutableLiveData<Int>().apply { value = 0 })
        progressed.add(MutableLiveData<Int>().apply { value = 0 })
        progressed.add(MutableLiveData<Int>().apply { value = 0 })
    }

    /**
     * this function is to handle the progress of the taken photos
     */
    fun uploadNewPhoto(photo: String, callback: (Boolean, String) -> Unit) {
        if (uploaded > 2) {
            callback(false, "")
            return
        }
        photoList.add(photo)
        val index = uploaded
        viewModelScope.launch(Dispatchers.IO) {
            val dir = client.uploadPhoto(
                File(photo),
                object : Progressive {

                    override fun onProgressUpdate(percentage: Int) {
                        progressed[index].postValue(100 - percentage)
                    }

                    override fun onError(e: Exception) {
                        progressed[index].postValue(100)
                        callback(false, e.message.toString())
                    }

                    override fun onFinish() {
                        progressed[index].postValue(0)
                    }
                })
            dir?.let { photoUrlList.add(it) }
        }
        uploaded++
    }
}