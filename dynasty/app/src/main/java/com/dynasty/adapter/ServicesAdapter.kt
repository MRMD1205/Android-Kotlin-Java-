package com.dynasty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.databinding.ItemServicesBinding
import com.dynasty.model.BusinessDetailModel

class ServicesAdapter(val context: Context, var mServiceList: ArrayList<BusinessDetailModel.Service>) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private lateinit var mBinding: ItemServicesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_services, parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, mServiceList)
    }

    override fun getItemCount(): Int = if(mServiceList.size > 0) mServiceList.size else 0

    class ViewHolder(var mBinder: ItemServicesBinding) : RecyclerView.ViewHolder(mBinder.root) {

        fun bind(context: Context, mServiceList: ArrayList<BusinessDetailModel.Service>) {

            if(!mServiceList[adapterPosition].service.isNullOrEmpty()) {
                mBinder.tvServiceTitle.text = mServiceList[adapterPosition].service
            } else {
                mBinder.tvServiceTitle.text = ""
            }

            if(!mServiceList[adapterPosition].description.isNullOrEmpty()) {
                mBinder.tvServiceDescription.text = mServiceList[adapterPosition].description
            } else {
                mBinder.tvServiceDescription.text = ""
            }
        }
    }
}