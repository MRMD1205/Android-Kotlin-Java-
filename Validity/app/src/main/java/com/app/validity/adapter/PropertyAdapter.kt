package com.app.validity.adapter

import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.GetPropertyListResponse
import java.text.SimpleDateFormat
import java.util.*

class PropertyAdapter(
    val list: MutableList<GetPropertyListResponse.Data>,
    val itemClickCallback: ItemClickCallback<GetPropertyListResponse.Data>
) :
    RecyclerView.Adapter<PropertyAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 0 else list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_property, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(list[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(currentItem: GetPropertyListResponse.Data, itemClickCallback: ItemClickCallback<GetPropertyListResponse.Data>) {

            val txtRentAmount: TextView = itemView.findViewById(R.id.txtRentAmount) as TextView
            val txtDate: TextView = itemView.findViewById(R.id.txtDate) as TextView
            val txtDateLbl: TextView = itemView.findViewById(R.id.txtDateLbl) as TextView
            val txtMonth: TextView = itemView.findViewById(R.id.txtMonth) as TextView
            val txtDay: TextView = itemView.findViewById(R.id.txtDay) as TextView
            val txtYear: TextView = itemView.findViewById(R.id.txtYear) as TextView
            txtRentAmount.text = currentItem.rentAmount.toString()
            if (!TextUtils.isEmpty(currentItem.ownershipStatus) && currentItem.ownershipStatus == "Owner") {
                txtDateLbl.text = "Purchase Date"
                txtRentAmount.text = "Purchase amount : ${currentItem.rentAmount}"
                if (TextUtils.isEmpty(currentItem.purchaseDate)) {
                    txtMonth.text = " - "
                    txtDay.text = " - "
                    txtYear.text = " - "
                    txtDate.text = " - "
                } else {
                    val date: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentItem.purchaseDate)
                    val dd = "${DateFormat.format("dd", date)}"
                    val mmm = "${DateFormat.format("MMM", date)}"
                    val yyyy = "${DateFormat.format("yyyy", date)}"
                    txtMonth.text = mmm
                    txtDay.text = dd
                    txtYear.text = yyyy
                    txtDate.text = "Purchase Date : $dd $mmm $yyyy"
                }
            } else if (!TextUtils.isEmpty(currentItem.ownershipStatus) && currentItem.ownershipStatus == "Rent") {
                txtDateLbl.text = "Rent Date"
                txtRentAmount.text = "Rent amount : ${currentItem.rentAmount}"
                if (TextUtils.isEmpty(currentItem.rentCollectionDate)) {
                    txtMonth.text = " - "
                    txtDay.text = " - "
                    txtYear.text = " - "
                    txtDate.text = " - "
                } else {
                    val date: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentItem.rentCollectionDate)
                    val dd = "${DateFormat.format("dd", date)}"
                    val mmm = "${DateFormat.format("MMM", date)}"
                    val yyyy = "${DateFormat.format("yyyy", date)}"
                    txtMonth.text = mmm
                    txtDay.text = dd
                    txtYear.text = yyyy
                    txtDate.text = "Rent Collection Date : $dd $mmm $yyyy"
                }
            } else {
                txtDateLbl.text = "Date"
            }

            val txtTenantName: TextView = itemView.findViewById(R.id.txtTenantName) as TextView
            val txtTenantNo: TextView = itemView.findViewById(R.id.txtTenantNo) as TextView

            txtTenantName.text = "Ownership : ${currentItem.ownershipStatus}"
            txtTenantNo.text = "Property Type : ${currentItem.propertyType?.name}"

            itemView.setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), currentItem, adapterPosition)
            }

            itemView.findViewById<View>(R.id.rlOperation).setOnClickListener {
                //itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
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

            itemView.setOnLongClickListener {
                //itemClickCallback.onItemLongClick(it, item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
                true
            }
        }
    }
}