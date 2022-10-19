package com.dynasty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.databinding.ItemTagsBinding
import com.dynasty.model.TagListModel


class TagFilterListAdapter(val context: Context, private var mTagList: ArrayList<TagListModel>, @NonNull private var onItemCheckListener: OnItemCheckListener, private var selectedTagList: ArrayList<Int>) : RecyclerView.Adapter<TagFilterListAdapter.ViewHolder>() {

    private lateinit var mBinding: ItemTagsBinding

    interface OnItemCheckListener {
        fun onItemCheck(item: TagListModel)
        fun onItemUncheck(item: TagListModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_tags, parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //        holder.bind(context, mTagList, onItemCheckListener)
        holder.mBinder.checkbox.isClickable = true
        val currentItem: TagListModel = mTagList[position]

        if(!currentItem.tagName.isNullOrEmpty()) {
            holder.mBinder.tvTagName.text = currentItem.tagName
        } else {
            holder.mBinder.tvTagName.text = ""
        }
//        holder.mBinder.checkbox.setChecked(holder.mBinder.checkbox.isChecked())

        //        if(holder.mBinder.checkbox.isChecked()) {
        //            holder.itemView.setOnClickListener { onItemCheckListener.onItemUncheck(currentItem) }
        //        } else {
        //            holder.itemView.setOnClickListener { onItemCheckListener.onItemCheck(currentItem) }
        //        }
        holder.mBinder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                currentItem.isSelected = isChecked;
                mTagList.set(position, currentItem)
            }
        }
        holder.mBinder.checkbox.isChecked = selectedTagList.contains(currentItem.tagId)

    }

    fun getData(): ArrayList<TagListModel> {
        return mTagList
    }

    override fun getItemCount(): Int = if(mTagList.size > 0) mTagList.size else 0

    class ViewHolder(var mBinder: ItemTagsBinding) : RecyclerView.ViewHolder(mBinder.root) {

        /*        fun bind(context: Context, mTagList: ArrayList<TagListModel>, @NonNull onItemCheckListener: OnItemCheckListener) {


                }*/

        fun setOnClickListener(onClickListener: View.OnClickListener?) {
            itemView.setOnClickListener(onClickListener)
        }
    }

}