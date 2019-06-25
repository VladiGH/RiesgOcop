package com.sovize.ultracop.models

data class ServerResponse(
    val error: String = "N/A",
    val errorCode: Int = 0,
    val mesage: String = "N/A",
    val success: Boolean = false,
    val fileDir: String = ""
)