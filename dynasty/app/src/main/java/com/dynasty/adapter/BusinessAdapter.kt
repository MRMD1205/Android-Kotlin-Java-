package com.dynasty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.databinding.RowItemBusinessBinding
import com.dynasty.fragment.BusinessDetailsFragment
import com.dynasty.fragment.BusinessFragment
import com.dynasty.model.BusinessListModel
import com.dynasty.util.BUSINESS_DETAIL_FRAGMENT
import com.dynasty.util.PAGE_SIZE

class BusinessAdapter(val context: Context, private var mBusinessList: ArrayList<BusinessListModel.Data>, var isFromActivity: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mBinding: RowItemBusinessBinding
    private val TYPE_ITEM = 0
    private val TYPE_PROG = 1
    private var showLoader = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_ITEM) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_item_business, parent, false)
            return MainItem(mBinding)
        } else {
            FooterItem(LayoutInflater.from(context).inflate(R.layout.item_load, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (mBusinessList.size == 0) {
            0
        } else if (BusinessFragment.totalItemCount <= (mBusinessList.size + PAGE_SIZE) &&
            BusinessFragment.totalItemCount <= mBusinessList.size
        ) {
            mBusinessList.size
        } else {
            mBusinessList.size + 1
        }
    }

    fun showLoading(isShowLoading: Boolean) {
        showLoader = isShowLoading
    }

    override fun getItemViewType(position: Int): Int {
        return if(isHeader(position)) TYPE_PROG else TYPE_ITEM
    }

    private fun isHeader(position: Int): Boolean {
        return position == mBusinessList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is FooterItem) {
            holder.showProgressLoader(showLoader)
        } else {
            (holder as MainItem).bind(context, mBusinessList)
        }
    }

    fun updateData(newList: java.util.ArrayList<BusinessListModel.Data>) {
       /* mBusinessList.clear()
        mBusinessList.addAll(newList)*/
        mBusinessList = newList
        notifyDataSetChanged()
    }

    internal class MainItem(var mBinder: RowItemBusinessBinding) : RecyclerView.ViewHolder(mBinder.root) {
        @SuppressLint("SetTextI18n")
        fun bind(context: Context, mBusinessList: ArrayList<BusinessListModel.Data>) {
            val item = mBusinessList[adapterPosition]
            mBinder.tvBusinessName.text = if(!item.businessName.isNullOrEmpty()) item.businessName else ""
            mBinder.tvBusinessLocation.text = if(!item.city.isNullOrEmpty()) item.city else ""

            if(!item.businessLogo.isNullOrEmpty()) {
                Glide.with(context).load(item.businessLogo).error(R.drawable.rect_placeholder).placeholder(R.drawable.rect_placeholder).into(mBinder.ivCategory)
            } else {
                Glide.with(context).load(R.drawable.rect_placeholder).error(R.drawable.rect_placeholder).placeholder(R.drawable.rect_placeholder).into(mBinder.ivCategory)
            }

            if(adapterPosition % 2 == 0) {
                val params = mBinder.ivImgOverlay.layoutParams as ConstraintLayout.LayoutParams
                params.startToStart = mBinder.gdStart.id
                params.endToEnd = ConstraintSet.PARENT_ID
                mBinder.ivImgOverlay.requestLayout()
            } else {
                val params = mBinder.ivImgOverlay.layoutParams as ConstraintLayout.LayoutParams
                params.startToStart = ConstraintSet.PARENT_ID
                params.endToEnd = mBinder.gdStart.id
                mBinder.ivImgOverlay.requestLayout()
            }

            itemView.setOnClickListener {
                (context as BaseActivity).navigateToFragment(BusinessDetailsFragment(item.businessId!!), true, BUSINESS_DETAIL_FRAGMENT)
            }

        }
    }

    internal class FooterItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun showProgressLoader(showLoader: Boolean) {
            if(showLoader) {
                itemView.visibility = View.VISIBLE
            } else {
                itemView.visibility = View.GONE
            }
        }
    }

}