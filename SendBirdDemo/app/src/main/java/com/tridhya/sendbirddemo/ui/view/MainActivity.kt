package com.tridhya.sendbirddemo.ui.view

import android.os.Bundle
import com.sendbird.live.uikit.activities.LiveEventListActivity
import com.tridhya.sendbirddemo.databinding.ActivityMainBinding

class MainActivity : LiveEventListActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        liveEvent.enterAsHost(MediaOptions(videoDevice = VIDEO_DEVICE, audioDevice = AUDIO_DEVICE.value)) {
//            if (it != null) {
//                return@enterAsHost
//            }
//        }


    }

}