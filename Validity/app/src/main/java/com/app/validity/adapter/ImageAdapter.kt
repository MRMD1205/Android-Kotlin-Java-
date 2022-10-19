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

class ImageAdapter(
    val list: ArrayList<Uri?>,
    val itemClickCallback: ItemClickCallback<Uri?>
) :
    RecyclerView.Adapter<ImageAdapter.ObjectHolder>() {


    override fun getItemCount(): Int =
        if (list.isNullOrEmpty()) 0
        else list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        return ObjectHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_selected_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(list[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(item: Uri?, itemClickCallback: ItemClickCallback<Uri?>) {
            val ivImage: AppCompatImageView = itemView.findViewById(R.id.ivImage) as AppCompatImageView
            val rlOperation: View = itemView.findViewById(R.id.rlOperation) as View
            val llDelete: View = itemView.findViewById(R.id.llDelete) as View
            val llCancel: View = itemView.findViewById(R.id.llCancel) as View
            if (item == null) {
                Glide.with(itemView.context).load(R.drawable.ic_add_image).into(ivImage)
                itemView.setOnClickListener {
                    itemClickCallback.onItemClick(ivImage, item, adapterPosition)
                }
            } else {
                itemView.setOnClickListener {
                    rlOperation.visibility = View.VISIBLE
                }
                rlOperation.setOnClickListener {
                    //                itemClickCallback.onItemClick(it, item, adapterPosition)
                }
                llDelete.setOnClickListener {
                    itemClickCallback.onItemClick(it, item, adapterPosition)
                    rlOperation.visibility = View.GONE
                }
                llCancel.setOnClickListener {
                    itemClickCallback.onItemClick(it, item, adapterPosition)
                    rlOperation.visibility = View.GONE
                }
                Glide.with(itemView.context).load(item).into(ivImage)
            }
        }
    }
}