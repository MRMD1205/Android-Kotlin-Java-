package com.app.validity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.ClsServiceType

class ServiceTypeListAdapter(val clsServiceType: ArrayList<ClsServiceType>,
                             val itemClickCallback: ItemClickCallback<ClsServiceType>?) :
    RecyclerView.Adapter<ServiceTypeListAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int =
        if (clsServiceType.isNullOrEmpty()) 0
        else clsServiceType.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_service_type, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(clsServiceType[position], itemClickCallback)
    }

    fun getServiceItemList(): ArrayList<ClsServiceType> {
        return clsServiceType
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(clsServiceType: ClsServiceType, itemClickCallback: ItemClickCallback<ClsServiceType>?) {
            val chkServiceType: CheckBox = itemView.findViewById(R.id.chkServiceType) as CheckBox

            chkServiceType.text = clsServiceType.serviceType
            chkServiceType.isChecked = clsServiceType.isChecked

            chkServiceType.setOnCheckedChangeListener { _, isChecked ->
                clsServiceType.isChecked = isChecked
            }
        }
    }


}