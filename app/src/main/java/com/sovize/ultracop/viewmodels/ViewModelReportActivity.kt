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
    val photoList = mutableListOf<String>()
    val photoUrlList = mutableListOf<String>()
    val progressed = ArrayList<MutableLiveData<Int>>()
    var cPhoto = ""

    fun uploadNewPhoto(photo: String): MutableLiveData<Int> {
        val newData = MutableLiveData<Int>()
        progressed.add(newData)
        viewModelScope.launch(Dispatchers.IO) {
            val dir = client.uploadPhoto(
                File(photo),
                object : Progressive {

                    val index = progressed.size - 1

                    override fun onProgressUpdate(percentage: Int) {
                        progressed[index].postValue(100 - percentage)
                    }

                    override fun onError() {
                        progressed[index].postValue(100)
                    }

                    override fun onFinish() {
                        progressed[index].postValue(0)
                    }
                })
            dir?.let { photoUrlList.add(it) }
        }
        return newData
    }

}