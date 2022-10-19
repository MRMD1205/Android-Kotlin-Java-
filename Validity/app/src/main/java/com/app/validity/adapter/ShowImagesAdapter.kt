package com.app.validity.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.interfaces.ItemClickCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ShowImagesAdapter(val list: MutableList<String?>, val itemClickCallback: ItemClickCallback<String?>) :
    RecyclerView.Adapter<ShowImagesAdapter.ObjectHolder>() {

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 0 else list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        return ObjectHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_selected_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(list[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(item: String?, itemClickCallback: ItemClickCallback<String?>) {
            val ivImage: AppCompatImageView = itemView.findViewById(R.id.ivImage) as AppCompatImageView
            if (item == null) {
                Glide.with(itemView.context).load(R.drawable.ic_image_unavailable).into(ivImage)
            } else {
                Glide.with(itemView.context).load(item)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_loading)
                            .error(R.drawable.ic_image_unavailable)
                    )
                    .into(ivImage)
            }
        }
    }
}