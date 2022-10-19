package com.dynasty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.databinding.ItemSearchBusinessListBinding
import com.dynasty.fragment.BusinessDetailsFragment
import com.dynasty.fragment.SearchBusinessFragment
import com.dynasty.model.BusinessListModel
import com.dynasty.util.BUSINESS_DETAIL_FRAGMENT
import com.dynasty.util.SEARCH_PAGE_SIZE

class SearchBusinessAdapter(val context: Context, var mBusinessList: ArrayList<BusinessListModel.Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mBinding: ItemSearchBusinessListBinding
    private val TYPE_ITEM = 0
    private val TYPE_PROG = 1
    private var showLoader = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_ITEM) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_business_list, parent, false)
            return MainItem(mBinding)
        } else {
            FooterItem(LayoutInflater.from(context).inflate(R.layout.item_load, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if(mBusinessList.size == 0) {
            0
        } else if(SearchBusinessFragment.totalItemCount <= (mBusinessList.size + SEARCH_PAGE_SIZE) &&
            SearchBusinessFragment.totalItemCount <= mBusinessList.size
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
        mBusinessList = newList
        notifyDataSetChanged()
    }

    internal class MainItem(var mBinder: ItemSearchBusinessListBinding) : RecyclerView.ViewHolder(mBinder.root) {
        @SuppressLint("SetTextI18n")
        fun bind(context: Context, mBusinessList: ArrayList<BusinessListModel.Data>) {
            val item = mBusinessList[adapterPosition]
            mBinder.tvCategoryTitle.text = if(!item.businessName.isNullOrEmpty()) item.businessName else ""
            mBinder.tvCity.text = if(!item.city.isNullOrEmpty()) item.city else ""

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