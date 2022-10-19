package com.app.validity.adapter

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
import com.app.validity.model.GetHomeAppliancesListResponse
import com.app.validity.model.RemindersItem

class RemindersAdapter(
    val list: MutableList<RemindersItem>,
    val itemClickCallback: ItemClickCallback<RemindersItem>
) :
    RecyclerView.Adapter<RemindersAdapter.ObjectHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 0 else list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        context = parent.context
        return ObjectHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_remiders, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bindItems(list[position], itemClickCallback)
    }

    class ObjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(
            currentItem: RemindersItem,
            itemClickCallback: ItemClickCallback<RemindersItem>
        ) {

            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtDate: TextView = itemView.findViewById(R.id.txtDate)
            val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)

            txtName.text = if (TextUtils.isEmpty(currentItem.name)) "" else currentItem.name
            txtDate.text = if (TextUtils.isEmpty(currentItem.date)) "" else "For Date : ${currentItem.date}"
            txtDescription.text = if (TextUtils.isEmpty(currentItem.description)) "" else currentItem.description

//            itemView.setOnClickListener {
//                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), currentItem, adapterPosition)
//            }

//            itemView.findViewById<View>(R.id.rlOperation).setOnClickListener {
            //itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
//            }

//            itemView.findViewById<View>(R.id.llCancel).setOnClickListener {
//                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
//            }

//            itemView.findViewById<View>(R.id.llView).setOnClickListener {
//                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llView), currentItem, adapterPosition)
//                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
//            }

//            itemView.findViewById<View>(R.id.llEdit).setOnClickListener {
//                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llEdit), currentItem, adapterPosition)
//                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
//            }

//            itemView.findViewById<View>(R.id.llDelete).setOnClickListener {
//                itemClickCallback.onItemClick(itemView.findViewById<View>(R.id.llDelete), currentItem, adapterPosition)
//                itemView.findViewById<View>(R.id.rlOperation).visibility = View.GONE
//            }

//            itemView.setOnLongClickListener {
            //itemClickCallback.onItemLongClick(it, item, adapterPosition)
//                itemView.findViewById<View>(R.id.rlOperation).visibility = View.VISIBLE
//                true
//            }
        }
    }
}