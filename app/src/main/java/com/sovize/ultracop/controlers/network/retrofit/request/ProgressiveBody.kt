package com.sovize.ultracop.controlers.network.retrofit.request

import com.sovize.ultracop.controlers.network.retrofit.interfaces.drivers.Progressive
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressiveBody(
    private val callBack: Progressive,
    private val mFile: File,
    private var contentType: String
) : RequestBody() {

    private val defaultBufferSize = 1024

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
            callBack.onError(e)
        } finally {
            witter.close()
        }
    }
}
