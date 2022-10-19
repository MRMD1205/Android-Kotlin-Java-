package com.app.validity.fragment


import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.DashboardAdapter
import com.app.validity.model.DashboardList
import com.app.validity.base.BaseFragment
import com.app.validity.util.Utility
import com.app.validity.util.Utility.Companion.showToast
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.util.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment(), ItemClickCallback<DashboardList> {

    override fun setContentView(): Int = R.layout.fragment_dashboard

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        val dashboardList: ArrayList<DashboardList> = ArrayList()
        dashboardList.add(DashboardList(1, R.drawable.ic_vect_vehicles, Utility.getMyString(R.string.menu_vehicles)))
        dashboardList.add(DashboardList(2, R.drawable.ic_vect_personal__document, Utility.getMyString(R.string.menu_personal_documents)))
        dashboardList.add(DashboardList(3, R.drawable.ic_vect_home_apploances, Utility.getMyString(R.string.menu_home_appliances)))
        dashboardList.add(DashboardList(4, R.drawable.ic_vect_property, Utility.getMyString(R.string.menu_property)))
        dashboardList.add(DashboardList(5, R.drawable.ic_vect_industrial, Utility.getMyString(R.string.menu_industrial)))
        dashboardList.add(DashboardList(6, R.drawable.ic_vect_transport_vehicles, Utility.getMyString(R.string.menu_transport_vehicles)))
        dashboardList.add(DashboardList(7, R.drawable.ic_vect_others, Utility.getMyString(R.string.menu_others)))

        val dashboardAdapter: DashboardAdapter = DashboardAdapter(dashboardList, this)
        rvDashboard.adapter = dashboardAdapter
    }

    override fun onResume() {
        super.onResume()
        isListEmpty(false)
        (requireActivity() as DashboardActivity).setHeader(R.string.app_name, true)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: DashboardList, position: Int) {
        when (selectedItem.pos) {
            1 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    VehicalListFragment(), true, VEHICAL_LIST_FRAGMENT)
            }
            2 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    PersonalDocumentsFragment(), true, PERSONAL_DOCUMENT_FRAGMENT)
            }
            3 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    HomeAppliancesFragment(), true, HOME_APPLIANCES_FRAGMENT)
            }
            4 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    PropertyFragment(), true, PROPERTY_FRAGMENT)
            }
            5 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    IndustrialFragment(), true, INDUSTRIAL_FRAGMENT)
            }
            6 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    TransportVehicalListFragment(), true, TRANSPORT_VEHICLE_FRAGMENT)
            }
            7 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    OthersFragment(), true, OTHERS_FRAGMENT)
            }
            else -> {
                showToast(requireActivity(), "Coming soon")
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: DashboardList, position: Int) {

    }
}
