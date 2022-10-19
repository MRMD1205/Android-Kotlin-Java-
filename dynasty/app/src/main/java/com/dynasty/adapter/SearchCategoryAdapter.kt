package com.dynasty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.databinding.ItemSearchCategoryListBinding
import com.dynasty.fragment.BusinessFragment
import com.dynasty.model.CategoryListModel
import com.dynasty.util.BUSINESS_FRAGMENT
import com.dynasty.util.Utils

class SearchCategoryAdapter(val context: Context, var mCategoryList: ArrayList<CategoryListModel>) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>(), Filterable {

    private lateinit var mBinding: ItemSearchCategoryListBinding
    private var filteredNameList: ArrayList<CategoryListModel>? = ArrayList()
    private var mCategoryNameList: ArrayList<CategoryListModel>? = ArrayList()


    init {
        filteredNameList!!.addAll(mCategoryList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_category_list, parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: CategoryListModel = filteredNameList?.get(position)!!

        if(!model.name.isNullOrEmpty()) {
            holder.mBinder.tvCategoryTitle.text = model.name
        } else {
            holder.mBinder.tvCategoryTitle.setText("")
        }

        holder.itemView.setOnClickListener {
            try {
                Utils.hideKeyboard(context as BaseActivity)
                if(!model.businessLink.isNullOrEmpty() && model.businessLink!!.toInt() > 0) {
                    (context as BaseActivity).navigateToFragment(BusinessFragment(model.id!!), true, BUSINESS_FRAGMENT, true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /* if(mCategoryList.size > 0) {
             for (i in mCategoryList.indices) {
                 mCategoryList[i].name?.let { mCategoryNameList?.add(it) }
             }
         }
         Log.e("Search Names", "onSuccess: $mCategoryNameList")*/
    }

    override fun getItemCount(): Int = if(filteredNameList?.size!! > 0) filteredNameList?.size!! else 0

    class ViewHolder(var mBinder: ItemSearchCategoryListBinding) : RecyclerView.ViewHolder(mBinder.root) {}

    fun filterList(filteredNames: ArrayList<CategoryListModel>) {
        mCategoryList = filteredNames
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()) {
                    filteredNameList = mCategoryList
                } else {
                    val filteredList: ArrayList<CategoryListModel> = ArrayList()
                    for (row in mCategoryList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if(row.name?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    filteredNameList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredNameList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                try {
                    if(filteredNameList?.size!! > 0) {
                        filteredNameList = filterResults.values as ArrayList<CategoryListModel>
                        notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}