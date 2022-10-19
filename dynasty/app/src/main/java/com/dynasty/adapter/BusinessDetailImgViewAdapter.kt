package com.dynasty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dynasty.R
import com.dynasty.databinding.ItemDetailBinding
import com.dynasty.model.BusinessDetailModel

class BusinessDetailImgViewAdapter(val context: Context, var mImgList: ArrayList<String>) : RecyclerView.Adapter<BusinessDetailImgViewAdapter.ViewHolder>() {

    private lateinit var mBinding: ItemDetailBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessDetailImgViewAdapter.ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail, parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, mImgList)
    }

    override fun getItemCount(): Int = if(mImgList.size > 0) mImgList.size else 0

    class ViewHolder(var mBinder: ItemDetailBinding) : RecyclerView.ViewHolder(mBinder.root) {

        fun bind(context: Context, mImgList: ArrayList<String>) {

            if(mImgList[adapterPosition].isNotEmpty()) {
                Glide.with(context).load(mImgList[adapterPosition]).error(R.drawable.small_icon_placeholder).placeholder(R.drawable.small_icon_placeholder).into(mBinder.ivRecyclerItem)
            } else {
                Glide.with(context).load(R.drawable.small_icon_placeholder).placeholder(R.drawable.small_icon_placeholder).into(mBinder.ivRecyclerItem)
            }
        }
    }
}