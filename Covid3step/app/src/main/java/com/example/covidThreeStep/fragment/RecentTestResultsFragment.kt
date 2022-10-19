package com.onestopcovid.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.adapter.RecentTestResultAdapter
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.model.RecentTestResultModel
import kotlinx.android.synthetic.main.fragment_recent_test_results.*
import java.util.*

class RecentTestResultsFragment : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_recent_test_results
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        rvVaccine.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val data = ArrayList<RecentTestResultModel>()

        for (i in 0 until 15) {
            data.add(RecentTestResultModel("Covid-19 Test Result", "July 27, 2020 at 1:04 AM", R.drawable.ic_red_add, ""))
        }
        val adapter = RecentTestResultAdapter(data, true)
        rvVaccine.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setHeader(R.string.recent_test_results, false, true, false, false)
    }

    override fun setListeners() {

    }

}