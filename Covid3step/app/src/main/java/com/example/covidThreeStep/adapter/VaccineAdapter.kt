package com.example.covidThreeStep.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidThreeStep.R
import com.onestopcovid.model.VaccineModel

class VaccineAdapter(private val vaccineList: ArrayList<VaccineModel>, private val viewAll: Boolean = false) : RecyclerView.Adapter<VaccineAdapter.VaccineHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_item, parent, false)
        return VaccineHolder(view)
    }

    override fun getItemCount(): Int {
        return when {
            viewAll -> {
                vaccineList.size
            }
            vaccineList.size < 4 -> {
                vaccineList.size
            }
            else -> {
                4
            }
        }
    }

    override fun onBindViewHolder(holder: VaccineHolder, position: Int) {
        val model: VaccineModel = vaccineList[position]
        holder.tvVaccineTitle.text = model.title
        holder.tvName.text = model.name
        holder.tvDate.text = model.date
        holder.tvAddress.text = model.address
    }

    class VaccineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVaccineTitle = itemView.findViewById(R.id.tvVaccineTitle) as TextView
        val tvName = itemView.findViewById(R.id.tvName) as TextView
        val tvDate = itemView.findViewById(R.id.tvDate) as TextView
        val tvAddress = itemView.findViewById(R.id.tvAddress) as TextView
    }
}
