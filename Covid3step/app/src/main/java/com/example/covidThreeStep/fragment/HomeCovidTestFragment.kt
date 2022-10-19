package com.onestopcovid.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.adapter.RecentTestResultAdapter
import com.onestopcovid.adapter.VaccineAdapter
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.model.RecentTestResultModel
import com.onestopcovid.model.VaccineModel
import com.onestopcovid.util.ENTER_NEW_COVID_TEST_FRAGMENT
import com.onestopcovid.util.RECENT_TEST_RESULTS_FRAGMENT
import com.onestopcovid.util.VACCINE_FRAGMENT
import kotlinx.android.synthetic.main.fragment_home_covid_test.*
import java.util.*

class HomeCovidTestFragment : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_home_covid_test
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        rvRecentTestResults.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvVaccine.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvMyCovidLog.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val data = ArrayList<RecentTestResultModel>()
        for (i in 0 until 15) {
            data.add(RecentTestResultModel("Covid-19 Test Result", "July 27, 2020 at 1:04 AM", R.drawable.ic_red_add, ""))
        }
        val adapter = RecentTestResultAdapter(data)
        rvRecentTestResults.adapter = adapter
        rvMyCovidLog.adapter = adapter

        val vacData = ArrayList<VaccineModel>()
        for (i in 0 until 15) {
            vacData.add(VaccineModel("Covid-19 Vaccine", "July 27, 2020 at 1:04 AM", "Astra Genica", "HG1-1234"))
        }
        val vacAdapter = VaccineAdapter(vacData)
        rvVaccine.adapter = vacAdapter
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setHeader(R.string.app_name, false, false, false, false)
    }

    override fun setListeners() {
        tvMyCovidLogViewAll.setOnClickListener {
            (activity as HomeActivity).navigateToFragment(RecentTestResultsFragment(), true, RECENT_TEST_RESULTS_FRAGMENT, true)
        }

        tvRecentTestResultsViewAll.setOnClickListener {
            (activity as HomeActivity).navigateToFragment(RecentTestResultsFragment(), true, RECENT_TEST_RESULTS_FRAGMENT, true)
        }

        tvVaccineViewAll.setOnClickListener {
            (activity as HomeActivity).navigateToFragment(VaccineFragment(), true, VACCINE_FRAGMENT, true)
        }

        btnEnterNewTestVaccine.setOnClickListener {
            (activity as HomeActivity).navigateToFragment(EnterNewCovidTestFragment(), true, ENTER_NEW_COVID_TEST_FRAGMENT, true)
        }
    }
}