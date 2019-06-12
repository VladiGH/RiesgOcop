package com.sovize.riesgocop.controlers.network.retrofit

import android.util.Log
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.PhotoReport
import com.sovize.riesgocop.models.ServerResponse
import com.sovize.riesgocop.utilities.AppLogger
import com.sovize.riesgocop.utilities.ServerInfo
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class HttpRetroClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerInfo.baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun uploadPhoto(photoFile: File) {
        val data = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            photoFile
        )
        val partData = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            data
        )
        Log.d(AppLogger.retrofit, "llegax ${photoFile.absoluteFile}")
        Log.d(AppLogger.retrofit, "llegax ${photoFile.exists()}")
        retrofit.create(PhotoReport::class.java).sentPicture(partData).enqueue(
            object : Callback<ServerResponse> {

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Log.e(AppLogger.retrofit, "${t.message}", t)
                }

                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    when (response.code()) {
                        200 -> {
                            Log.d(AppLogger.retrofit, response.message())
                            Log.d(AppLogger.retrofit, response.body()?.fileDir)
                            //
                        }

                        else -> {
                            val httpCode = response.code().toString()
                            val httpsMessage = response.message().toString()
                            Log.e(AppLogger.retrofit, "$httpCode: $httpsMessage")
                        }
                    }
                }
            }
        )

    }

}