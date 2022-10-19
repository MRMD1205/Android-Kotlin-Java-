package com.example.videoplayer

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YoutubePlayerActivity : YouTubeBaseActivity() {
    private var videoID: String? = null
    private var youTubePlayerView: YouTubePlayerView? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.youtube_player_activity)
        videoID = getIntent().getStringExtra("video_id")
        youTubePlayerView = findViewById(R.id.youtube_player_view)
        initializeYoutubePlayer()
    }

    private fun initializeYoutubePlayer() {
        youTubePlayerView?.initialize(
            Constants.DEVLOPER_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer,
                    wasRestored: Boolean
                ) {

                    if (!wasRestored) {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        youTubePlayer.loadVideo(videoID)
                    }
                }

                override fun onInitializationFailure(
                    arg0: YouTubePlayer.Provider?,
                    arg1: YouTubeInitializationResult?
                ) {
                    Log.e(
                        TAG,
                        "Youtube Player View initialization failed"
                    )
                }
            })
    }
}