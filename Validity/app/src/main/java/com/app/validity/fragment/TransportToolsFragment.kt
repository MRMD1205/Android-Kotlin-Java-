package com.app.validity.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesForDialogAdapter
import com.app.validity.adapter.TransportToolsAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.ClsTransportCertificateResponse
import com.app.validity.model.TransportToolsItem
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_TRANSPORT_TOOLS_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.RecyclerTouchListener
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_transport_tools.*
import org.json.JSONObject

class TransportToolsFragment(var vehicalId: String) : BaseFragment(), ItemClickCallback<TransportToolsItem> {

    override fun setContentView(): Int = R.layout.fragment_transport_tools

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {

    }

    private fun callGetDetailsAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getTransportCertificate(vehicalId, object : OnApiCallCompleted<ClsTransportCertificateResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: ClsTransportCertificateResponse = obj as ClsTransportCertificateResponse
                val toolsList: ArrayList<TransportToolsItem> = ArrayList()

                /** Fitness Certificate */
                val fitnessCertificateImages: MutableList<String> = ArrayList()
                var startDateFitnessCertificate = ""
                var endDateFitnessCertificate = ""

                /** Road Tax */
                val roadTaxImages: MutableList<String> = ArrayList()
                var startDateRoadTax = ""
                var endDateRoadTax = ""

                /** Permit */
                val permitImages: MutableList<String> = ArrayList()
                var startDatePermit = ""
                var endDatePermit = ""
                var permitType = ""

                /** Speed Governer */
                val speedGovernerImages: MutableList<String> = ArrayList()
                var startDateSpeedGoverner = ""
                var endDateSpeedGoverner = ""

                if (response.data != null) {

                    /** Fitness Certificate */
                    if (response.data!!.fitnessCertificateImages != null)
                        fitnessCertificateImages.addAll(response.data!!.fitnessCertificateImages!!)
                    if (response.data!!.certificateDetail != null) {
                        if (response.data!!.certificateDetail!!.fitnessCertificateStartDate != null)
                            startDateFitnessCertificate = response.data!!.certificateDetail!!.fitnessCertificateStartDate!!

                        if (response.data!!.certificateDetail!!.fitnessCertificateEndDate != null)
                            endDateFitnessCertificate = response.data!!.certificateDetail!!.fitnessCertificateEndDate!!
                    }


                    /** Road Tax */
                    if (response.data!!.roadTaxImages != null)
                        roadTaxImages.addAll(response.data!!.roadTaxImages!!)
                    if (response.data!!.certificateDetail != null) {
                        if (response.data!!.certificateDetail!!.roadTaxStartDate != null)
                            startDateRoadTax = response.data!!.certificateDetail!!.roadTaxStartDate!!
                        if (response.data!!.certificateDetail!!.roadTaxEndDate != null)
                            endDateRoadTax = response.data!!.certificateDetail!!.roadTaxEndDate!!
                    }


                    /** Permit */
                    if (response.data!!.permitImages != null)
                        permitImages.addAll(response.data!!.permitImages!!)
                    if (response.data!!.certificateDetail != null) {
                        if (response.data!!.certificateDetail!!.permitStartDate != null)
                            startDatePermit = response.data!!.certificateDetail!!.permitStartDate!!
                        if (response.data!!.certificateDetail!!.permitEndDate != null)
                            endDatePermit = response.data!!.certificateDetail!!.permitEndDate!!
                        if (response.data!!.certificateDetail!!.permitType != null)
                            permitType = response.data!!.certificateDetail!!.permitType!!
                    }


                    /** Speed Governer */
                    if (response.data!!.speedGovernerImages != null)
                        speedGovernerImages.addAll(response.data!!.speedGovernerImages!!)
                    if (response.data!!.certificateDetail != null) {
                        if (response.data!!.certificateDetail!!.speedGovernerStartDate != null)
                            startDateSpeedGoverner = response.data!!.certificateDetail!!.speedGovernerStartDate!!
                        if (response.data!!.certificateDetail!!.speedGovernerEndDate != null)
                            endDateSpeedGoverner = response.data!!.certificateDetail!!.speedGovernerEndDate!!
                    }
                }

                toolsList.add(TransportToolsItem(1, getString(R.string.transport_tool_fitness), fitnessCertificateImages, startDateFitnessCertificate, endDateFitnessCertificate, ""))
                toolsList.add(TransportToolsItem(2, getString(R.string.transport_tool_road_tax), roadTaxImages, startDateRoadTax, endDateRoadTax, ""))
                toolsList.add(TransportToolsItem(3, getString(R.string.transport_tool_permit), permitImages, startDatePermit, endDatePermit, permitType))
                toolsList.add(TransportToolsItem(4, getString(R.string.transport_tool_speed_governor), speedGovernerImages, startDateSpeedGoverner, endDateSpeedGoverner, ""))
                rvVehicalCategoryList.adapter = TransportToolsAdapter(toolsList, this@TransportToolsFragment)

            }

            override fun apiFailure(errorMessage: String) {
                Utility.showToast(requireActivity(), errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                Utility.showToast(requireActivity(), errorMessage)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        callGetDetailsAPI()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_transport_vehicles, false)
    }

    override fun onItemClick(view: View, selectedItem: TransportToolsItem, position: Int) {
        if (view.id == R.id.llAdd) {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddTransportToolsFragment(vehicalId, selectedItem.pos, null),
                true, ADD_TRANSPORT_TOOLS_FRAGMENT
            )
        } else {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddTransportToolsFragment(vehicalId, selectedItem.pos, selectedItem),
                true, ADD_TRANSPORT_TOOLS_FRAGMENT
            )
        }
    }

    override fun onItemLongClick(view: View, selectedItem: TransportToolsItem, position: Int) {
        /** this onItemLongClick() Used for imagesList dialog*/

        val viewGroup = requireView().findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_show_images, viewGroup, false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()
        val txtTitle = dialogView.findViewById<TextView>(R.id.txtTitle)
        val txtDismiss = dialogView.findViewById<TextView>(R.id.txtDismiss)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvList)
        val layoutManager = GridLayoutManager(activity, 2)
        val adapter = ShowImagesForDialogAdapter(selectedItem.imageList)
        if (selectedItem.imageList.size == 0) dialogView.findViewById<TextView>(R.id.txtListSizeInfoDialog).visibility = View.VISIBLE
        when (selectedItem.pos) {
            1 -> txtTitle.text = getString(R.string.transport_tool_fitness)
            2 -> txtTitle.text = getString(R.string.transport_tool_road_tax)
            3 -> txtTitle.text = getString(R.string.transport_tool_permit)
            4 -> txtTitle.text = getString(R.string.transport_tool_speed_governor_edit)
        }

        txtDismiss.setOnClickListener { alertDialog.dismiss() }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                requireContext(), recyclerView,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {

//                        alertDialog.dismiss()
                    }

                    override fun onLongClick(view: View, position: Int) {}
                })
        )
        alertDialog.show()
    }


}
