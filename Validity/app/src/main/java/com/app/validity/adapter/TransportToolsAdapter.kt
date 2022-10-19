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
import com.app.validity.model.TransportToolsItem
import java.text.SimpleDateFormat
import java.util.*

class TransportToolsAdapter(val dashboardList: ArrayList<TransportToolsItem>, val itemClickCallback: ItemClickCallback<TransportToolsItem>) :
    RecyclerView.Adapter<TransportToolsAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int = if (dashboardList.isNullOrEmpty()) 0 else dashboardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(LayoutInflater.from(context).inflate(R.layout.list_item_transport_tools, parent, false))
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(dashboardList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(item: TransportToolsItem, itemClickCallback: ItemClickCallback<TransportToolsItem>) {

            val llStartDate: View = itemView.findViewById(R.id.llStartDate) as View
            val llData: View = itemView.findViewById(R.id.llData) as View
            val llEndDate: View = itemView.findViewById(R.id.llEndDate) as View
            val llAdd: View = itemView.findViewById(R.id.llAdd) as View

            if (TextUtils.isEmpty(item.startDate) || TextUtils.isEmpty(item.endDate)) {
                llStartDate.visibility = View.GONE
                llData.visibility = View.GONE
                llEndDate.visibility = View.GONE
                llAdd.visibility = View.VISIBLE
            } else {
                llStartDate.visibility = View.VISIBLE
                llData.visibility = View.VISIBLE
                llEndDate.visibility = View.VISIBLE
                llAdd.visibility = View.GONE
            }

            val txtName: TextView = itemView.findViewById(R.id.txtName) as TextView
            val txtNameInner: TextView = itemView.findViewById(R.id.txtNameInner) as TextView
            val txtSubName: TextView = itemView.findViewById(R.id.txtSubName) as TextView

            txtName.text = item.name
            txtNameInner.text = "Add ${item.name}"
            txtSubName.text = item.permitType
            if (item.pos == 3) txtSubName.visibility = View.VISIBLE else txtSubName.visibility = View.GONE

            val txtMonthStart: TextView = itemView.findViewById(R.id.txtMonthStart) as TextView
            val txtDayStart: TextView = itemView.findViewById(R.id.txtDayStart) as TextView
            val txtYearStart: TextView = itemView.findViewById(R.id.txtYearStart) as TextView

            if (TextUtils.isEmpty(item.startDate)) {
                txtMonthStart.text = " - "
                txtDayStart.text = " - "
                txtYearStart.text = " - "
            } else {
                val date1: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.startDate)
                val dd1 = "${DateFormat.format("dd", date1)}"
                val mmm1 = "${DateFormat.format("MMM", date1)}"
                val yyyy1 = "${DateFormat.format("yyyy", date1)}"
                txtMonthStart.text = dd1
                txtDayStart.text = mmm1
                txtYearStart.text = yyyy1
            }

            val txtMonthEnd: TextView = itemView.findViewById(R.id.txtMonthEnd) as TextView
            val txtDayEnd: TextView = itemView.findViewById(R.id.txtDayEnd) as TextView
            val txtYearEnd: TextView = itemView.findViewById(R.id.txtYearEnd) as TextView

            if (TextUtils.isEmpty(item.endDate)) {
                txtMonthEnd.text = " - "
                txtDayEnd.text = " - "
                txtYearEnd.text = " - "
            } else {
                val date2: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.endDate)
                val dd2 = "${DateFormat.format("dd", date2)}"
                val mmm2 = "${DateFormat.format("MMM", date2)}"
                val yyyy2 = "${DateFormat.format("yyyy", date2)}"

                txtMonthEnd.text = dd2
                txtDayEnd.text = mmm2
                txtYearEnd.text = yyyy2
            }

            itemView.setOnClickListener {
                if (llAdd.visibility == View.VISIBLE) {
                    itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llAdd), item, adapterPosition)
                } else {
//                    itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llData), item, adapterPosition)
                    itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
                }
            }

            itemView.findViewById<View>(R.id.llView).setOnClickListener {
                itemClickCallback.onItemLongClick(itemView.findViewById<View>(R.id.llView), item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llEdit).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llData), item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llCancel).setOnClickListener {
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.fabAdd).setOnClickListener {
                if (llAdd.visibility == View.VISIBLE) {
                    itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llAdd), item, adapterPosition)
                } else {
                    itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llData), item, adapterPosition)
                }
            }
        }
    }
}