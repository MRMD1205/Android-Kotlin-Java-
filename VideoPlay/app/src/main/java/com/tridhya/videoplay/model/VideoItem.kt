package com.tridhya.videoplay.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class VideoItem : java.io.Serializable {
    val path: String?
    val duration: Int
    val width: Int
    val height: Int

    constructor(path: String?, duration: Int, width: Int, height: Int) {
        this.path = path
        this.duration = duration
        this.width = width
        this.height = height
    }
}