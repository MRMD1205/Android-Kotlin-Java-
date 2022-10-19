package com.app.validity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.VehicleItem
import com.app.validity.util.Utility

private lateinit var mContext: Context

class VehicalListAdapter(
    val vehicleItem: ArrayList<VehicleItem>,
    val itemClickCallback: ItemClickCallback<VehicleItem>
) :
    RecyclerView.Adapter<VehicalListAdapter.ObjectHolder>() {

    var context: Context? = null
    override fun getItemCount(): Int =
        if (vehicleItem.isNullOrEmpty()) 0
        else vehicleItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        mContext = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_vehical_list_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(vehicleItem[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(vehicleItem: VehicleItem, itemClickCallback: ItemClickCallback<VehicleItem>) {
            val imgVehicalType: AppCompatImageView = itemView.findViewById(R.id.imgVehicalType)
            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtDisplayName: TextView = itemView.findViewById(R.id.txtDisplayName)
            txtName.text = if (TextUtils.isEmpty(vehicleItem.name)) "" else vehicleItem.name
            val year = if (TextUtils.isEmpty(vehicleItem.buildYear)) "" else vehicleItem.buildYear
            val regNo = if (TextUtils.isEmpty(vehicleItem.registrationNumber)) "" else vehicleItem.registrationNumber
            @SuppressLint("SetTextI18n")
            txtDisplayName.text = "$year - $regNo".trim()
            Utility.setImageUsingGlide(itemView.context, vehicleItem.iconUrl, imgVehicalType)

            //if (vehicleItem.vehicleType != null && !TextUtils.isEmpty(vehicleItem.vehicleType!!.name)) {
            //    if (vehicleItem.vehicleType!!.name.toString().toLowerCase() == "car") {
            //        imgVehicalType.setImageResource(R.drawable.ic_car)
            //    } else if (vehicleItem.vehicleType!!.name.toString().toLowerCase() == "bike") {
            //        imgVehicalType.setImageResource(R.drawable.ic_bike)
            //    } else {
            //        imgVehicalType.setImageResource(R.mipmap.ic_launcher)
            //    }
            //} else {
            //    imgVehicalType.setImageResource(R.mipmap.ic_launcher)
            //}

            itemView.setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llOpen), vehicleItem, adapterPosition)
            }

            itemView.findViewById<View>(R.id.rlOperation).setOnClickListener {
                //itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llCancel).setOnClickListener {
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llOpen).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llOpen), vehicleItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llView).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), vehicleItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llEdit).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llEdit), vehicleItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llDelete).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llDelete), vehicleItem, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.setOnLongClickListener {
                //itemClickCallback.onItemLongClick(it, item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
                true
            }
        }
    }
}