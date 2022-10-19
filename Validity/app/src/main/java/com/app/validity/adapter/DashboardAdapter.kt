package com.app.validity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.model.DashboardList
import com.app.validity.interfaces.ItemClickCallback

class DashboardAdapter(
    val dashboardList: ArrayList<DashboardList>,
    val itemClickCallback: ItemClickCallback<DashboardList>
) :
    RecyclerView.Adapter<DashboardAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int =
        if (dashboardList.isNullOrEmpty()) 0
        else dashboardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(dashboardList[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(dashboardItem: DashboardList, itemClickCallback: ItemClickCallback<DashboardList>) {
            val txtUserName: TextView = itemView.findViewById(R.id.txtUserName) as TextView
            val imgProfile: AppCompatImageView = itemView.findViewById(R.id.imgProfile) as AppCompatImageView

            txtUserName.text = dashboardItem.menuTitle!!
            imgProfile.setImageResource(dashboardItem.image)

            itemView.setOnClickListener {
                itemClickCallback.onItemClick(it, dashboardItem, adapterPosition)
            }
        }
    }
}