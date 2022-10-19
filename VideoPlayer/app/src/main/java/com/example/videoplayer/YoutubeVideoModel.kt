package com.example.videoplayer

class YoutubeVideoModel {
    var videoId: String? = null
    var title: String? = null
    var duration: String? = null

    override fun toString(): String {
        return "YoutubeVideoModel{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                '}'
    }
}