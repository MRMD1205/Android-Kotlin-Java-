package com.example.youtubeplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.youtubeplayer.models.MediaObject;
import com.example.youtubeplayer.utilities.Resources;
import com.example.youtubeplayer.utilities.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private VideoPlayerRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

        ArrayList<MediaObject> mediaObjects = new ArrayList<MediaObject>(Arrays.asList(Resources.MEDIA_OBJECTS));
        mRecyclerView.setMediaObjects(mediaObjects);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide());
        mRecyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onDestroy() {
        if(mRecyclerView!=null)
            mRecyclerView.releasePlayer();
        super.onDestroy();
    }
}