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
import com.app.validity.model.VehicalExpense
import com.app.validity.interfaces.ItemClickCallback
import java.text.SimpleDateFormat
import java.util.*

class VehicalExpenseListAdapter(
    val dashboardList: ArrayList<VehicalExpense>,
    val itemClickCallback: ItemClickCallback<VehicalExpense>
) :
    RecyclerView.Adapter<VehicalExpenseListAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int =
        if (dashboardList.isNullOrEmpty()) 0
        else dashboardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_expence, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(dashboardList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(
            item: VehicalExpense,
            itemClickCallback: ItemClickCallback<VehicalExpense>
        ) {
            val txtMonth: TextView = itemView.findViewById(R.id.txtMonth) as TextView
            val txtDay: TextView = itemView.findViewById(R.id.txtDay) as TextView
            val txtYear: TextView = itemView.findViewById(R.id.txtYear) as TextView
            val txtQtyValue: TextView = itemView.findViewById(R.id.txtQtyValue) as TextView
            val txtAmountValue: TextView = itemView.findViewById(R.id.txtAmountValue) as TextView

            txtQtyValue.text = if (TextUtils.isEmpty(item.expenseType)) " - " else item.expenseType
            txtAmountValue.text = if (TextUtils.isEmpty(item.amount)) " - " else item.amount

            val strDate = if (TextUtils.isEmpty(item.expenseDate)) " - " else item.expenseDate
            val date: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate)
            val dd = "${DateFormat.format("dd", date)}"
            val mmm = "${DateFormat.format("MMM", date)}"
            val yyyy = "${DateFormat.format("yyyy", date)}"

            txtMonth.text = mmm
            txtDay.text = dd
            txtYear.text = yyyy

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