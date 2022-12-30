package com.tridhya.videoplay.model

import java.io.Serializable

data class User(
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
) : Serializable
