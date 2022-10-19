package com.app.validity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ShowImagesForDialogAdapter(val list: MutableList<String>) : RecyclerView.Adapter<ShowImagesForDialogAdapter.ObjectHolder>() {

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 0 else list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        return ObjectHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_selected_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(list[position])
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(item: String) {
            val ivImage: AppCompatImageView = itemView.findViewById(R.id.ivImage) as AppCompatImageView
                Glide.with(itemView.context).load(item)
                    .apply(
                        RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.ic_image_unavailable)
                    ).into(ivImage)
            }
    }
}