package com.sovize.riesgocop.controlers.network.retrofit.interfaces

import com.sovize.riesgocop.models.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PhotoReport {

    @Multipart
    @POST("/upload3.php")
    fun sentPicture(
        @Part file: MultipartBody.Part
    ): Call<ServerResponse>

}