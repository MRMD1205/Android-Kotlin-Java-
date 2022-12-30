package com.tridhya.videoplay.model

import java.io.Serializable

data class MediaModel(
    var userId: String? = null,
    var userName: String? = null,
    var userProfileImage: String? = null,
    var title: String? = null,
    var musicTitle: String? = null,
    var description: String? = null,
    var likes: Int? = 0,
    var comments: Int? = 0,
    var thumbnail: String? = null,
    var videoUrl: String? = null,
    var isLiked: Boolean = false,
) : Serializable
