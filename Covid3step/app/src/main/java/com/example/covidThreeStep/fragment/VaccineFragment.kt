package com.onestopcovid.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.adapter.VaccineAdapter
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.model.VaccineModel
import kotlinx.android.synthetic.main.fragment_vaccine.*

class VaccineFragment : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_vaccine

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        rvVaccine.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val vacData = ArrayList<VaccineModel>()

        for (i in 0 until 15) {
            vacData.add(VaccineModel("Covid-19 Vaccine", "July 27, 2020 at 1:04 AM", "Astra Genica", "HG1-1234"))
        }
        val vacAdapter = VaccineAdapter(vacData, true)
        rvVaccine.adapter = vacAdapter
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setHeader(R.string.vaccination, false, true, true, false)
    }

    override fun setListeners() {

    }
}