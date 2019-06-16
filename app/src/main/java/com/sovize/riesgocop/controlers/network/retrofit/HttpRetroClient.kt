package com.sovize.riesgocop.controlers.network.retrofit

import android.util.Log
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.PhotoReport
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers.Progressive
import com.sovize.riesgocop.controlers.network.retrofit.request.ProgressiveBody
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ServerInfo
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.SocketTimeoutException

class HttpRetroClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerInfo.baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun uploadPhoto(photoFile: File, caller: Progressive) : String? {
        val data = ProgressiveBody(caller, photoFile, "image")
        val partData = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            data
        )
        val call = retrofit.create(PhotoReport::class.java)
            .sentPicture(partData)
        try {
            val result = call.execute()
            return if (result.isSuccessful && result.code() == 200){
                Log.d(AppLogger.retrofit, result.message())
                Log.d(AppLogger.retrofit, result.code().toString())
                Log.d(AppLogger.retrofit, result.body()?.mesage)
                result.body()?.fileDir!!.substring(1)
            }
            else {
                Log.e(AppLogger.retrofit, result.message())
                Log.e(AppLogger.retrofit, result.body()?.error)
                Log.e(AppLogger.retrofit, result.code().toString())
                null
            }
        }catch ( e: SocketTimeoutException){
            Log.d(AppLogger.retrofit, "se jodie en subida", e)
            return  null
        }
    }

}