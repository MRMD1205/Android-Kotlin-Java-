package com.example.videoplayer

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import java.util.*

class YoutubeVideoAdapter(
    private val context: Context,
    youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>?
) : RecyclerView.Adapter<YoutubeViewHolder?>() {
    private val youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View =
            layoutInflater.inflate(R.layout.youtube_video_custom_layout, parent, false)
        return YoutubeViewHolder(view)
    }

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        val youtubeVideoModel: YoutubeVideoModel = youtubeVideoModelArrayList!![position]
        holder.videoTitle.setText(youtubeVideoModel.title)
        holder.videoDuration.setText(youtubeVideoModel.duration)
        holder.itemView.setOnClickListener(View.OnClickListener {
            context.startActivity(
                Intent(context, YoutubePlayerActivity::class.java)
                    .putExtra("video_id", youtubeVideoModelArrayList[position].videoId)
            )
        })

        holder.videoThumbnailImageView.initialize(
            Constants.DEVLOPER_KEY,
            object : YouTubePlayer.OnInitializedListener,
                YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(
                    youTubeThumbnailView: YouTubeThumbnailView?,
                    youTubeThumbnailLoader: YouTubeThumbnailLoader
                ) {
                    youTubeThumbnailLoader.setVideo(youtubeVideoModel.videoId)
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                        YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(
                            youTubeThumbnailView: YouTubeThumbnailView?,
                            s: String?
                        ) {
                            youTubeThumbnailLoader.release()
                        }

                        override fun onThumbnailError(
                            youTubeThumbnailView: YouTubeThumbnailView?,
                            errorReason: YouTubeThumbnailLoader.ErrorReason?
                        ) {
                            Log.e(
                                TAG,
                                "Youtube Thumbnail Error"
                            )
                        }
                    })
                }

                override fun onInitializationFailure(
                    youTubeThumbnailView: YouTubeThumbnailView?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) {
                    Log.e(
                        TAG,
                        "Youtube Initialization Failure"
                    )
                }

                override fun onInitializationSuccess(
                    youTubePlayerProvider: YouTubePlayer.Provider?,
                    youTubePlayer: YouTubePlayer?,
                    bol: Boolean
                ) {
                }

                override fun onInitializationFailure(
                    youTubePlayerProvider: YouTubePlayer.Provider?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) {
                }
            })
    }

    private val itemCount: Int = youtubeVideoModelArrayList?.size ?: 0
    override fun getItemCount(): Int {

        return youtubeVideoModelArrayList!!.size
    }

    init {
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList
    }
}