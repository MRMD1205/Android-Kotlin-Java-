package com.example.mapsdemo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsdemo.databinding.ItemPlacesBinding
import com.example.mapsdemo.localStorage.PlaceModel

class PlacesListAdapter(val listener: OnOptionSelected) :
    RecyclerView.Adapter<PlacesListAdapter.ViewHolder>() {

    val list: ArrayList<PlaceModel> = arrayListOf()

    fun setList(places: ArrayList<PlaceModel>) {
        list.clear()
        list.addAll(places)
        notifyDataSetChanged()
    }

    class ViewHolder(val mBinding: ItemPlacesBinding) : RecyclerView.ViewHolder(mBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.tvAddress.text = list[position].address
        holder.mBinding.ivEdit.setOnClickListener {
            listener.onEdit(list[holder.adapterPosition])
        }
        holder.mBinding.ivDelete.setOnClickListener {
            listener.onDelete(list[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnOptionSelected {
        fun onEdit(place: PlaceModel)
        fun onDelete(place: PlaceModel)
    }

}