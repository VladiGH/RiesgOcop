package com.sovize.riesgocop.controlers.network.retrofit.request

import android.os.Handler
import android.os.Looper
import com.sovize.riesgocop.controlers.network.retrofit.interfaces.drivers.Progressable
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressiveBody(
    val callBack: Progressable,
    private val mFile: File,
    private var contentType: String
) : RequestBody() {

    private val defaultBufferSize = 1028

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
        witter.use {
            var read =it.read(buffer)
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
                handler.post(ProgressUpdater(uploaded, fileSize))
                read = it.read(buffer)
            }
        }
    }

    private inner class ProgressUpdater(
        private val mUploaded: Long,
        private val mTotal: Long
    ) : Runnable {

        override fun run() {
            callBack.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }
}
