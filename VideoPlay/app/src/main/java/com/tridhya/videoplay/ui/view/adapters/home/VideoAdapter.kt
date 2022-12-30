package com.tridhya.videoplay.ui.view.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.videoplay.databinding.ItemHomeBinding
import com.tridhya.videoplay.model.MediaModel

class VideoAdapter(private val list: List<MediaModel>) :
    RecyclerView.Adapter<VideoViewHolder>() {

    var lastValue: Int = 0
    var visiblePosition: MutableLiveData<Int?> = MutableLiveData()

    init {
        visiblePosition.value = 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val pos =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                visiblePosition.value = pos
                lastValue = pos
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding, parent.context, visiblePosition)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun stopPlayers() {
        visiblePosition.value = null
    }

    fun pausePlayers() {
        visiblePosition.value = lastValue
    }

}