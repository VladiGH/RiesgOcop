package com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers

interface Progressable {
    fun onProgressUpdate(percentage: Int)
    fun onError()
    fun onFinish()
}