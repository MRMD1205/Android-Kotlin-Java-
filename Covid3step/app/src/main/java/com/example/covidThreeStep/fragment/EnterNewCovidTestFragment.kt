package com.onestopcovid.fragment

import android.os.Bundle
import android.view.View
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.util.COVID_ANTI_BODY_FRAGMENT
import kotlinx.android.synthetic.main.fragment_enter_new_covid_test.*

class EnterNewCovidTestFragment : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_enter_new_covid_test
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setHeader(R.string.new_covid19_test_vaccine, false, false, false, true)
    }

    override fun setListeners() {
        btnSave.setOnClickListener {
            (activity as HomeActivity).navigateToFragment(CovidAntiBodyTestFragment(), true, COVID_ANTI_BODY_FRAGMENT, true)
        }
    }
}