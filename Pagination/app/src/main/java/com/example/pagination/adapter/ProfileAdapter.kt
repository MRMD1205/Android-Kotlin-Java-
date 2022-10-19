package com.example.pagination.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pagination.model.Profile
import com.example.pagination.R

class ProfileAdapter(private val profileList: ArrayList<Profile>, private val activity: Activity) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profileImage: ImageView = itemView.findViewById(R.id.ivProfileImage)
        val profileName: TextView = itemView.findViewById(R.id.tvProfileName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_items,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profileData = profileList[position]

        Glide.with(activity)
            .load(profileData.image)
            .into(holder.profileImage)

        holder.profileName.text = profileData.name
    }

    override fun getItemCount(): Int {
        return profileList.size
    }
}