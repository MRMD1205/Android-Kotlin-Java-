package com.dynasty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.databinding.ItemCategoriesBinding
import com.dynasty.model.CategoryListModel


class CategoryFilterListAdapter(val context: Context, private var mCategoryList: ArrayList<CategoryListModel>, @NonNull private var onItemCheckListener: OnItemCheckListener, private var selectedCategoryList: ArrayList<Int>) : RecyclerView.Adapter<CategoryFilterListAdapter.ViewHolder>() {

    private lateinit var mBinding: ItemCategoriesBinding

    interface OnItemCheckListener {
        fun onItemCheck(item: CategoryListModel)
        fun onItemUncheck(item: CategoryListModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_categories, parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //        holder.bind(context, mCategoryList, onItemCheckListener)
        holder.mBinder.checkbox.isClickable = true
        val currentItem: CategoryListModel = mCategoryList[position]

        if(!currentItem.name.isNullOrEmpty()) {
            holder.mBinder.tvCategoryName.text = currentItem.name
        } else {
            holder.mBinder.tvCategoryName.text = ""
        }

//        holder.mBinder.checkbox.setChecked(holder.mBinder.checkbox.isChecked())
        holder.mBinder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                currentItem.isSelected = isChecked;
                mCategoryList.set(position, currentItem)
            }
        }
        holder.mBinder.checkbox.isChecked = selectedCategoryList.contains(currentItem.id!!.toInt())
    }

    fun getData(): ArrayList<CategoryListModel> {
        return mCategoryList
    }

    override fun getItemCount(): Int = if(mCategoryList.size > 0) mCategoryList.size else 0

    class ViewHolder(var mBinder: ItemCategoriesBinding) : RecyclerView.ViewHolder(mBinder.root) {

        /*        fun bind(context: Context, mCategoryList: ArrayList<CategoryListModel>, @NonNull onItemCheckListener: OnItemCheckListener) {

                }*/

        fun setOnClickListener(onClickListener: View.OnClickListener?) {
            itemView.setOnClickListener(onClickListener)
        }
    }

}