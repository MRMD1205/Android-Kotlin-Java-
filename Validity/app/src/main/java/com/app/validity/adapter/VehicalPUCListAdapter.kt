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
import com.app.validity.model.VehicalPUC
import com.app.validity.interfaces.ItemClickCallback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VehicalPUCListAdapter(
    val dashboardList: ArrayList<VehicalPUC>,
    val itemClickCallback: ItemClickCallback<VehicalPUC>
) :
    RecyclerView.Adapter<VehicalPUCListAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int =
        if (dashboardList.isNullOrEmpty()) 0
        else dashboardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_puc, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(dashboardList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(
            item: VehicalPUC,
            itemClickCallback: ItemClickCallback<VehicalPUC>
        ) {

            val txtPolicyValue: TextView = itemView.findViewById(R.id.txtPolicyValue) as TextView
            txtPolicyValue.text = if (TextUtils.isEmpty(item.pucNumber)) " - " else item.pucNumber

            val txtMonthStart: TextView = itemView.findViewById(R.id.txtMonthStart) as TextView
            val txtDayStart: TextView = itemView.findViewById(R.id.txtDayStart) as TextView
            val txtYearStart: TextView = itemView.findViewById(R.id.txtYearStart) as TextView

            val strDate = if (TextUtils.isEmpty(item.issueDate)) " - " else item.issueDate
            val date1: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate)
            val dd1 = "${DateFormat.format("dd", date1)}"
            val mmm1 = "${DateFormat.format("MMM", date1)}"
            val yyyy1 = "${DateFormat.format("yyyy", date1)}"

            txtMonthStart.text = dd1
            txtDayStart.text = mmm1
            txtYearStart.text = yyyy1

            val txtMonthEnd: TextView = itemView.findViewById(R.id.txtMonthEnd) as TextView
            val txtDayEnd: TextView = itemView.findViewById(R.id.txtDayEnd) as TextView
            val txtYearEnd: TextView = itemView.findViewById(R.id.txtYearEnd) as TextView


            val endDate = if (TextUtils.isEmpty(item.expiryDate)) " - " else item.expiryDate
            val date2: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate)
            val dd2 = "${DateFormat.format("dd", date2)}"
            val mmm2 = "${DateFormat.format("MMM", date2)}"
            val yyyy2 = "${DateFormat.format("yyyy", date2)}"

            txtMonthEnd.text = dd2
            txtDayEnd.text = mmm2
            txtYearEnd.text = yyyy2
            itemView.setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), item, adapterPosition)
            }

            itemView.findViewById<View>(R.id.rlOperation).setOnClickListener {
                //itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llCancel).setOnClickListener {
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llView).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llEdit).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llEdit), item, adapterPosition)
                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
            }

            itemView.findViewById<View>(R.id.llDelete).setOnClickListener {
                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llDelete), item, adapterPosition)
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