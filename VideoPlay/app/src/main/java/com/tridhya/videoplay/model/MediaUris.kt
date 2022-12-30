package com.tridhya.videoplay.model

import android.net.Uri

data class MediaUris(
    val uri: Uri? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null,
    var duration: String? = null,
)