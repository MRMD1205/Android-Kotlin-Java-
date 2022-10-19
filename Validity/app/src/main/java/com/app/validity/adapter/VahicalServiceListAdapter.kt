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
import com.app.validity.model.VehicalServiceItem
import com.app.validity.interfaces.ItemClickCallback
import java.text.SimpleDateFormat
import java.util.*

class VahicalServiceListAdapter(
    val dashboardList: ArrayList<VehicalServiceItem>,
    val itemClickCallback: ItemClickCallback<VehicalServiceItem>
) :
    RecyclerView.Adapter<VahicalServiceListAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int =
        if (dashboardList.isNullOrEmpty()) 0
        else dashboardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_service, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(dashboardList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(item: VehicalServiceItem, itemClickCallback: ItemClickCallback<VehicalServiceItem>) {
            val txtMonth: TextView = itemView.findViewById(R.id.txtMonth) as TextView
            val txtDay: TextView = itemView.findViewById(R.id.txtDay) as TextView
            val txtYear: TextView = itemView.findViewById(R.id.txtYear) as TextView
            val txtServiceType: TextView = itemView.findViewById(R.id.txtServiceType) as TextView
            val txtAmount: TextView = itemView.findViewById(R.id.txtAmount) as TextView

            txtServiceType.text = if (TextUtils.isEmpty(item.description)) " - " else item.description
            txtAmount.text = if (TextUtils.isEmpty(item.amount.toString())) " - " else item.amount.toString()

            val strDate = if (TextUtils.isEmpty(item.date)) " - " else item.date
            val date: Date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate)
            val dd = "${DateFormat.format("dd", date)}"
            val mmm = "${DateFormat.format("MMM", date)}"
            val yyyy = "${DateFormat.format("yyyy", date)}"

            txtMonth.text = mmm
            txtDay.text = dd
            txtYear.text = yyyy
            //            val imgProfile: AppCompatImageView = itemView.findViewById(R.id.imgProfile) as AppCompatImageView
            //
            //            txtUserName.text = dashboardItem.menuTitle!!
            //            imgProfile.setImageResource(dashboardItem.image)
            //
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