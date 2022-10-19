package com.example.covidThreeStep.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidThreeStep.R
import com.onestopcovid.model.RecentTestResultModel

class RecentTestResultAdapter(
    private val recentTestList: ArrayList<RecentTestResultModel>, private val viewAll: Boolean = false) : RecyclerView.Adapter<RecentTestResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_test_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return when {
            viewAll -> {
                recentTestList.size
            }
            recentTestList.size < 4 -> {
                recentTestList.size
            }
            else -> {
                4
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: RecentTestResultModel = recentTestList[position]
        holder.tvTitle.text = model.title
        holder.tvDate.text = model.date
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById(R.id.tvVaccineTitle) as TextView
        val tvDate = itemView.findViewById(R.id.tvDate) as TextView
    }
}