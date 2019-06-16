package com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers

interface Progressive {
    fun onProgressUpdate(percentage: Int)
    fun onError()
    fun onFinish()
}