package com.sovize.ultracop.controlers.network.retrofit.interfaces

import com.sovize.ultracop.models.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PhotoReport {

    @Multipart
    @POST("/uploadx.php")
    fun sentPicture(
        @Part file: MultipartBody.Part
    ): Call<ServerResponse>

}