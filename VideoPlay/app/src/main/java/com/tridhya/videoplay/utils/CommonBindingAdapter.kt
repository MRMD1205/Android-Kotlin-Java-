package com.tridhya.videoplay.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun loadImageGenericImage(view: ImageView, url: String) {
    Glide.with(view)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .into(view)
}

@BindingAdapter("setImageCenterInside")
fun loadImageCenterInside(view: ImageView, url: String) {
    Glide.with(view)
        .load(url)
        .apply(RequestOptions().centerInside())
        .into(view)
}

@BindingAdapter("setLinearVerticalAdapter")
fun bindRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>?
) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter("setLinearHorizontalAdapter")
fun bindRecyclerViewHorizontalAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>?
) {
    recyclerView.layoutManager =
        LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["setGridAdapter", "column"], requireAll = true)
fun bindRecyclerViewGridAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>?,
    spanCount: Int
) {
    recyclerView.layoutManager =
        GridLayoutManager(recyclerView.context, spanCount)
    recyclerView.adapter = adapter
}

@BindingAdapter("nestedScroll")
fun nestedScrollEnable(view: NestedScrollView, bool: Boolean) {
    view.isNestedScrollingEnabled = bool
}

@BindingAdapter("timestamp")
fun setTimestamp(view: TextView, value: Long) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = value * 1000L
    val output = SimpleDateFormat("hh:mm a, dd MMM yyyy")
    try {
        view.text = output.format(cal.timeInMillis)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}