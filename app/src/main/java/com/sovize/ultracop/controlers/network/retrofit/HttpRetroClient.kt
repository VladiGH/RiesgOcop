package com.sovize.ultracop.controlers.network.retrofit

import android.util.Log
import com.sovize.ultracop.controlers.network.retrofit.interfaces.PhotoReport
import com.sovize.ultracop.controlers.network.retrofit.interfaces.drivers.Progressive
import com.sovize.ultracop.controlers.network.retrofit.request.ProgressiveBody
import com.sovize.ultracop.utilities.AppLogger
import com.sovize.ultracop.utilities.ServerInfo
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class HttpRetroClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerInfo.baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun uploadPhoto(photoFile: File, caller: Progressive): String? {
        val data = ProgressiveBody(caller, photoFile, "image")
        //part wrap need it in order to sent the data in measurable output streams in hops or data chunks
        val partData = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            data
        )
        val call = retrofit.create(PhotoReport::class.java)
            .sentPicture(partData)
        return try {
            val result = call.execute()
            if (result.isSuccessful && result.code() == 200) {
                Log.d(AppLogger.retrofit, result.message())
                Log.d(AppLogger.retrofit, result.code().toString())
                Log.d(AppLogger.retrofit, result.body()?.mesage)
                result.body()?.fileDir!!.substring(1)
            } else {
                caller.onError(java.lang.Exception(result.message() + result.body()?.error + result.code().toString()))
                null
            }
        } catch (e: Exception) {
            caller.onError(e)
            null
        }
    }

}