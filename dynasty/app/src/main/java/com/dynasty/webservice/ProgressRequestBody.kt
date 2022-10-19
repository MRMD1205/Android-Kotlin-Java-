package com.dynasty.webservice

import android.os.Handler
import android.os.Looper

import java.io.File
import java.io.FileInputStream
import java.io.IOException

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class ProgressRequestBody internal constructor(private val mFile: File, private val content_type: String, private val mListener: UploadCallbacks?) : RequestBody() {
    private val mPath: String? = null

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
    }


    override fun contentType(): MediaType? {
        return "$content_type/*".toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        FileInputStream(mFile).use { `in` ->
            var uploaded: Long = 0
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            /*while ((read = `in`.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }*/

            `in`.reader().forEachLine {
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += it.toLong()
                sink.write(buffer, 0, it.toInt())
            }
        }
    }

    private inner class ProgressUpdater internal constructor(private val mUploaded: Long, private val mTotal: Long) : Runnable {
        internal var previous = 0
        internal var present: Int = 0

        override fun run() {
            present = (100 * mUploaded / mTotal).toInt()
            if (mListener != null && present > previous) {
                previous = present
                mListener.onProgressUpdate(present)
            }
        }
    }

    companion object {

        private val DEFAULT_BUFFER_SIZE = 2048
    }
}
