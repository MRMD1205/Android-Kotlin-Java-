package com.app.validity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.TransportVehicleItem

private lateinit var mContext: Context

class TransportVehicalListAdapter(
    val itemList: ArrayList<TransportVehicleItem>,
    val itemClickCallback: ItemClickCallback<TransportVehicleItem>
) : RecyclerView.Adapter<TransportVehicalListAdapter.ObjectHolder>() {

    var context: Context? = null
    override fun getItemCount(): Int = if (itemList.isNullOrEmpty()) 0 else itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        mContext = parent.context
        return ObjectHolder(LayoutInflater.from(context).inflate(R.layout.list_item_transport_vehical, parent, false))
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(itemList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(currentItem: TransportVehicleItem, itemClickCallback: ItemClickCallback<TransportVehicleItem>) {
            val txtItem1: AppCompatTextView = itemView.findViewById(R.id.txtItem1)
            val txtItem2: AppCompatTextView = itemView.findViewById(R.id.txtItem2)
            val txtItem3: AppCompatTextView = itemView.findViewById(R.id.txtItem3)
            val txtItem4: AppCompatTextView = itemView.findViewById(R.id.txtItem4)
            val txtItem5: AppCompatTextView = itemView.findViewById(R.id.txtItem5)
            val txtItem6: AppCompatTextView = itemView.findViewById(R.id.txtItem6)

            val vehicleType = if (currentItem.transportVehicleType == null) ""
            else if (currentItem.transportVehicleType?.name == null) ""
            else currentItem.transportVehicleType?.name

            val vehicleCategory = if (currentItem.transportVehicleCategory == null) ""
            else if (currentItem.transportVehicleCategory?.name == null) ""
            else currentItem.transportVehicleCategory?.name

            txtItem1.text = "Registration No. : ${currentItem.registerNo}"
            txtItem2.text = "Driver Name : ${currentItem.driverName}"
            txtItem3.text = "Phone No. : ${currentItem.driverPhoneNo}"
            txtItem4.text = "License No. : ${currentItem.driverLicenseNo}"
            txtItem5.text = "Vehicle Type : $vehicleType"
            txtItem6.text = "Vehicle Category : $vehicleCategory"

            itemView.findViewById<View>(R.id.llOpen).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llOpen), currentItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llCancel).setOnClickListener {
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llView).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), currentItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llEdit).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llEdit), currentItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llDelete).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llDelete), currentItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.setOnClickListener {
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
            }

            itemView.setOnLongClickListener {
                //itemClickCallback.onItemLongClick(it, item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
                true
            }
        }
    }
}