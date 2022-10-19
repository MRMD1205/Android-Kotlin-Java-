package com.dynasty.util

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.dynasty.R
import com.dynasty.webservice.APIs.BASE_IMAGE_PATH

class GlideUtils(private val context: Context) {

    fun loadImageSimple(url: String?, view: ImageView) {
        if (url != null && url != "null" && url != "") {
            Glide.with(context)
                    .load(BASE_IMAGE_PATH + url)
                    .apply(RequestOptions()
                            .placeholder(R.color.colorAccent)
                            .error(R.color.colorAccent)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop())
                    .transition(withCrossFade(300))
                    .into(view)
        }
    }

    fun loadCircleImageFromLocal(url: String?, view: ImageView) {
        if (url != null && url != "null" && url != "") {
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions()
                            .placeholder(R.color.colorAccent)
                            .error(R.color.colorAccent)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(view)
        }
    }

    fun loadCircleImageFromURL(url: String?, view: ImageView) {
        if (url != null && url != "null" && url != "") {
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions()
                            .placeholder(R.color.colorAccent)
                            .error(R.color.colorAccent)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(view)
        }
    }

    fun loadCircleImageFromURL(url: String?, view: ImageView, isSetTransition: Boolean) {
        if (isSetTransition) {
            loadCircleImageFromURL(url, view)
        } else {
            if (url != null && url != "null" && url != "") {
                Glide.with(context)
                        .load(BASE_IMAGE_PATH + url)
                        .apply(RequestOptions()
                                .placeholder(R.color.colorAccent)
                                .error(R.color.colorAccent)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .circleCrop())
                        .into(view)
            }
        }

    }
}
