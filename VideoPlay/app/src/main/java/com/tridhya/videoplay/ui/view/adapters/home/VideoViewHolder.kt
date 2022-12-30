package com.tridhya.videoplay.ui.view.adapters.home

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.TimeBar
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.ItemHomeBinding
import com.tridhya.videoplay.extentions.gone
import com.tridhya.videoplay.extentions.visible
import com.tridhya.videoplay.model.MediaModel
import com.tridhya.videoplay.utils.GlideUtils


class VideoViewHolder(
    val binding: ItemHomeBinding,
    val context: Context,
    private val itemVisiblePosition: MutableLiveData<Int?> = MutableLiveData()
) :
    RecyclerView.ViewHolder(binding.root) {

    private var player: SimpleExoPlayer? = null
    private var timeBar: DefaultTimeBar? = null
    private var isDescriptionShowing = false

    init {
        player = SimpleExoPlayer.Builder(context).build()
        binding.videoView.player = player
        binding.videoView.useController = true

        timeBar = binding.videoView.findViewById(R.id.exo_progress)
        timeBar?.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                timeBar.setEnabled(false)
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                timeBar.setEnabled(false)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                timeBar.setEnabled(false)
            }

        })
    }

    fun bind(videoModel: MediaModel) {

        binding.ivLikes.isSelected = videoModel.isLiked

        binding.ivShare.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, videoModel.videoUrl);
            context.startActivity(Intent.createChooser(shareIntent, "Select App"))
        }

        binding.ivLikes.setOnClickListener {
            videoModel.isLiked = !videoModel.isLiked
            binding.ivLikes.isSelected = videoModel.isLiked
            if (videoModel.isLiked) {
                binding.tvLikesCount.text = "${videoModel.likes!! + 1}"
                videoModel.likes = videoModel.likes!! + 1
            } else {
                binding.tvLikesCount.text = "${videoModel.likes!! - 1}"
                videoModel.likes = videoModel.likes!!.toInt() - 1
            }
        }

        itemVisiblePosition.observeForever {
            if (it == bindingAdapterPosition) {
                binding.thumbnail.gone()
                binding.videoView.visible()
                playPlayer()
            } else {
                binding.thumbnail.visible()
                binding.videoView.gone()
                pausePlayer()
            }

            binding.tvMusicInfo.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.tvMusicInfo.isSingleLine = true
            binding.tvMusicInfo.marqueeRepeatLimit = 10
            binding.tvMusicInfo.isSelected = true
        }

        GlideUtils(context).loadImage(videoModel.videoUrl, binding.thumbnail)

        val mediaItem = videoModel.videoUrl?.let { MediaItem.fromUri(it) }
        mediaItem?.let { player?.setMediaItem(it) }
        player?.prepare()
        player?.play()
        playPlayer()

        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    player!!.seekTo(0)
                    player!!.playWhenReady = true
                }
            }
        })

        binding.thumbnail.gone()
        binding.videoView.visible()
        binding.tvUserName.text = videoModel.userName
        GlideUtils(binding.ivProfileImage.context).loadImage(
            videoModel.userProfileImage,
            binding.ivProfileImage
        )
        binding.tvDescription.maxLines = 2
        if (videoModel.description.toString().length > 60) {
            val text = videoModel.description.toString().substring(0, 60) + "..."
            binding.tvDescription.text =
                (Html.fromHtml("$text <font><u><b>View More</b></u></font>"))
            binding.tvDescription.setOnClickListener {
                if (isDescriptionShowing) {
                    binding.tvDescription.maxLines = 10
                    binding.tvDescription.text =
                        (Html.fromHtml("${videoModel.description.toString()} <font><u><b>View Less</b></u></font>"))
                    isDescriptionShowing = false
                } else {
                    val text = videoModel.description.toString().substring(0, 60) + "..."
                    binding.tvDescription.text =
                        (Html.fromHtml("$text <font><u><b>View More</b></u></font>"))
                    isDescriptionShowing = true
                }
            }
        } else {
            binding.tvDescription.text = videoModel.description
        }
        binding.tvLikesCount.text = videoModel.likes.toString()
        binding.tvCommentsCount.text = videoModel.comments.toString()
        binding.tvMusicInfo.text = videoModel.musicTitle
    }

    private fun playPlayer() {
        if (player != null) {
            player!!.playWhenReady = true
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
        }
    }

    private fun seekTo(positionInMS: Long) {
        if (player != null) {
            player!!.seekTo(positionInMS)
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
        }
    }
}