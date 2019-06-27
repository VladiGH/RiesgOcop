package com.sovize.ultracop.controlers.network.retrofit.interfaces.drivers

/**
 * Progressive is use to handle event from a network background thread ..
 * towards the UI components
 */
interface Progressive {
    fun onProgressUpdate(percentage: Int)
    fun onError(e: Exception)
    fun onFinish()
}