package com.dynasty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.databinding.RowItemCategoryBinding
import com.dynasty.fragment.BusinessFragment
import com.dynasty.model.CategoryListModel
import com.dynasty.util.BUSINESS_FRAGMENT


class CategoryAdapter(val context: Context, private var mCategoryList: ArrayList<CategoryListModel>, var isFromActivity: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mBinding: RowItemCategoryBinding
    private val TYPE_ITEM = 0
    private val TYPE_PROG = 1
    private var showLoader = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_item_category, parent, false)
        return MainItem(mBinding)

    }

    override fun getItemCount(): Int {

        return mCategoryList.size

    }

    fun showLoading(isShowLoading: Boolean) {
        showLoader = isShowLoading
    }

    override fun getItemViewType(position: Int): Int {
        return if(isHeader(position)) TYPE_PROG else TYPE_ITEM
    }

    private fun isHeader(position: Int): Boolean {
        return position == mCategoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MainItem).bind(context, mCategoryList)
    }

    fun updateData(mCategoryList: java.util.ArrayList<CategoryListModel>) {
        this.mCategoryList = mCategoryList
        notifyDataSetChanged()
    }

    internal class MainItem(var mBinder: RowItemCategoryBinding) : RecyclerView.ViewHolder(mBinder.root) {
        @SuppressLint("SetTextI18n")
        fun bind(context: Context, mCategoryList: ArrayList<CategoryListModel>) {
            val item = mCategoryList[adapterPosition]

            mBinder.tvCategoryName.text = if(!item.name.isNullOrEmpty()) item.name else ""
            mBinder.tvCategoryNoValue.text = if(!item.businessLink.isNullOrEmpty()) item.businessLink else ""

            if(!item.logo.isNullOrEmpty()) {
                Glide.with(context).load(item.logo).error(R.drawable.square_placeholder).placeholder(R.drawable.square_placeholder).into(mBinder.ivCategory)
            } else {
                Glide.with(context).load(R.drawable.square_placeholder).error(R.drawable.square_placeholder).placeholder(R.drawable.square_placeholder).into(mBinder.ivCategory)
            }

            itemView.setOnClickListener {
                try {
                    if(!item.businessLink.isNullOrEmpty() && item.businessLink!!.toInt() > 0)
                    {
                        (context as BaseActivity).navigateToFragment(BusinessFragment(item.id!!), true, BUSINESS_FRAGMENT, true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
