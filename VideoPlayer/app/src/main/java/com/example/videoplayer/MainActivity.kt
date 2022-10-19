package com.example.videoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    public var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        populateRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.setLayoutManager(linearLayoutManager)
    }

    private fun populateRecyclerView() {
        val youtubeVideoModelArrayList =
            generateDummyVideoList()
        val adapter = YoutubeVideoAdapter(this, youtubeVideoModelArrayList)
        recyclerView?.setAdapter(adapter)
    }

    private fun generateDummyVideoList(): ArrayList<YoutubeVideoModel> {
        val youtubeVideoModelArrayList =
            ArrayList<YoutubeVideoModel>()

        val videoIDArray =
            resources.getStringArray(R.array.video_id_array)
        val videoTitleArray =
            resources.getStringArray(R.array.video_title_array)
        val videoDurationArray =
            resources.getStringArray(R.array.video_duration_array)

        for (i in videoIDArray.indices) {
            val youtubeVideoModel = YoutubeVideoModel()
            youtubeVideoModel.videoId = videoIDArray[i]
            youtubeVideoModel.title = videoTitleArray[i]
            youtubeVideoModel.duration = videoDurationArray[i]
            youtubeVideoModelArrayList.add(youtubeVideoModel)
        }
        return youtubeVideoModelArrayList
    }
}