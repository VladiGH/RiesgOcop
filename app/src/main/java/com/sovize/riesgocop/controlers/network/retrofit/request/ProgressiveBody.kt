package com.sovize.riesgocop.controlers.network.retrofit.request

import android.util.Log
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers.Progressive
import com.sovize.riesgocop.utilities.AppLogger
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class ProgressiveBody(
    private val callBack: Progressive,
    private val mFile: File,
    private var contentType: String
) : RequestBody() {

    private val defaultBufferSize = 2048

    override fun contentType(): MediaType? {
        return MediaType.parse("$contentType/*")
    }

    override fun contentLength(): Long {
        return mFile.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileSize = contentLength()
        val buffer = ByteArray(defaultBufferSize)
        val witter = FileInputStream(mFile)
        var uploaded: Long = 0
        try {
            var read = witter.read(buffer)
            while (read != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
                callBack.onProgressUpdate((100 * uploaded / fileSize).toInt())
                read = witter.read(buffer)
            }
            callBack.onFinish()
        } catch (e: Exception) {
            Log.e(AppLogger.retrofit, "error de subida", e)
            callBack.onError()
        } finally {
            witter.close()
        }
    }
}
