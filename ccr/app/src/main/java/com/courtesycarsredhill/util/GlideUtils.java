package com.courtesycarsredhill.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.courtesycarsredhill.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.courtesycarsredhill.webservice.APIs.BASE_IMAGE_PATH;

public class GlideUtils {

    private Context context;

    public GlideUtils(Context context) {
        this.context = context;
    }

    public void loadImageSimple(String url, ImageView view) {
        if (url != null && !url.equals("null") && !url.equals("")) {
            Glide.with(context)
                    .load(BASE_IMAGE_PATH + url)
                    .apply(new RequestOptions()
                            .placeholder(R.color.transparent)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop())
                    .transition(withCrossFade(300))
                    .into(view);
        }
    }

    public void loadCircleImageFromLocal(String url, ImageView view) {
        if (url != null && !url.equals("null") && !url.equals("")) {
            Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(view);
        }
    }

    public void loadCircleImageFromURL(String url, ImageView view) {
        if (url != null && !url.equals("null") && !url.equals("")) {
            Glide.with(context)
                    .load(BASE_IMAGE_PATH + url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(view);
        }
    }

    public void loadCircleImageFromURL(String url, ImageView view, boolean isSetTransition) {
        if (isSetTransition) {
            loadCircleImageFromURL(url, view);
        } else {
            if (url != null && !url.equals("null") && !url.equals("")) {
                Glide.with(context)
                        .load(BASE_IMAGE_PATH + url)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.shape_circle_grey)
                                .error(R.drawable.shape_circle_grey)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .circleCrop())
                        .into(view);
            }
        }

    }
}
