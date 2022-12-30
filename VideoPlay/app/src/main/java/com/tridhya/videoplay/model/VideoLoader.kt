package com.tridhya.videoplay.model

import android.content.Context
import android.os.Handler
import com.tridhya.videoplay.model.VideoLoader.VideoLoadRunnable
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import com.tridhya.videoplay.interfaces.VideoLoadListener
import com.tridhya.videoplay.model.VideoLoader
import java.io.File
import java.lang.NullPointerException
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoLoader(private val context: Context) {
    private var executorService: ExecutorService? = null
    fun loadDeviceVideos(listener: VideoLoadListener) {
        getExecutorService()!!.execute(VideoLoadRunnable(listener, context))
    }

    fun abortLoadVideos() {
        if (executorService != null) {
            executorService!!.shutdown()
            executorService = null
        }
    }

    private fun getExecutorService(): ExecutorService? {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor()
        }
        return executorService
    }

    private class VideoLoadRunnable(listener: VideoLoadListener, context: Context) : Runnable {
        private val listener: VideoLoadListener
        private val context: Context
        private val handler = Handler(Looper.getMainLooper())
        private val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT
        )

        init {
            this.listener = listener
            this.context = context
        }

        override fun run() {
            val cursor = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Video.Media.DATE_MODIFIED
            )
            if (cursor == null) {
                handler.post { listener.onFailed(NullPointerException()) }
                return
            }
            val temp: MutableList<VideoItem> = ArrayList<VideoItem>(cursor.count)
            if (cursor.moveToLast()) {
                do {
                    val path = cursor.getString(cursor.getColumnIndex(projection[0])) ?: continue
                    if (!path.endsWith(".mp4") && !path.endsWith(".MOV") && !path.endsWith(".mov")) {
                        continue
                    }
                    Log.d(TAG, "pick video from device path = $path")
                    var duration = cursor.getString(cursor.getColumnIndex(projection[1]))
                    if (duration == null) duration = "0"
                    Log.d(TAG, "pick video from device duration = $duration")
                    var width = cursor.getString(cursor.getColumnIndex(projection[2]))
                    if (width == null) width = "0"
                    Log.d(TAG, "pick video from device width = $width")
                    var height = cursor.getString(cursor.getColumnIndex(projection[3]))
                    if (height == null) height = "0"
                    Log.d(TAG, "pick video from device height = $height")
                    var file: File? = File(path)
                    if (file!!.exists()) {
                        temp.add(
                            VideoItem(
                                path,
                                Integer.valueOf(duration),
                                Integer.valueOf(width),
                                Integer.valueOf(height)
                            )
                        )
                    }
                    file = null
                } while (cursor.moveToPrevious())
            }
            cursor.close()
            handler.post { listener.onVideoLoaded(temp) }
        }
    }

    companion object {
        private const val TAG = "VideoLoader"
    }
}