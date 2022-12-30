package com.tridhya.videoplay.ui.view.adapters.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.videoplay.databinding.ItemFilterTypeBinding
import com.tridhya.videoplay.enums.FilterType

class FilterTypeAdapter(private val filterList: List<FilterType>, val listener: onClicked) :
    RecyclerView.Adapter<FilterTypeAdapter.FilterTypeViewHolder>() {
    class FilterTypeViewHolder(val binding: ItemFilterTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filterType: FilterType) {
            binding.tvFilter.text = filterType.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTypeViewHolder {
        val binding =
            ItemFilterTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterTypeViewHolder, position: Int) {
        holder.bind(filterList[position])

        holder.itemView.setOnClickListener {
            listener.filterSelected(filterList[position])
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    interface onClicked {
        fun filterSelected(filterType: FilterType)
    }
}