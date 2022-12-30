package com.tridhya.videoplay.interfaces;

import com.tridhya.videoplay.model.VideoItem;

import java.util.List;

public interface VideoLoadListener {

    void onVideoLoaded(List<VideoItem> videoItems);

    void onFailed(Exception e);
}
