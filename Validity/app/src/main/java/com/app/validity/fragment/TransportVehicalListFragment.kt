package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.TransportVehicalListAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetTransportVehicleListResponse
import com.app.validity.model.TransportVehicleItem
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_TRANSPORT_VEHICLE_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.TRANSPORT_TOOLS_FRAGMENT
import com.app.validity.util.VIEW_TRANSPORT_VEHICLE_FRAGMENT
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility.Companion.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_transport_vehical_list.*
import org.json.JSONObject

class TransportVehicalListFragment : BaseFragment(), ItemClickCallback<TransportVehicleItem> {

    var list: ArrayList<TransportVehicleItem> = ArrayList()
    var vehicalListAdapter: TransportVehicalListAdapter? = null
    val license_type: MutableList<String> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_transport_vehical_list

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        fabAdd.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddTransportVehicalFragment(null, license_type), true, ADD_TRANSPORT_VEHICLE_FRAGMENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getVehicleList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_transport_vehicles, false)
    }

    private fun setAdapter(response: GetTransportVehicleListResponse) {
        list = response.list as ArrayList<TransportVehicleItem>
        vehicalListAdapter = TransportVehicalListAdapter(list, this)
        rvVehicalList.adapter = vehicalListAdapter
    }

    private fun deleteVehicle(selectedItem: TransportVehicleItem, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_transport_vehicle))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deleteTransportVehicle(selectedItem.id.toString(), position)
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onItemClick(view: View, selectedItem: TransportVehicleItem, position: Int) {
        when (view.id) {

            R.id.llOpen -> {
                val frag = TransportToolsFragment(selectedItem.id.toString())
                (requireActivity() as DashboardActivity).navigateToFragment(frag, true, TRANSPORT_TOOLS_FRAGMENT)
            }

            R.id.llView -> {
                val frag = ViewTransportVehicleFragment(selectedItem)
                (requireActivity() as DashboardActivity).navigateToFragment(frag, true, VIEW_TRANSPORT_VEHICLE_FRAGMENT)
            }

            R.id.llDelete -> {
                deleteVehicle(selectedItem, position)
            }

            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddTransportVehicalFragment(selectedItem, license_type), true, ADD_TRANSPORT_VEHICLE_FRAGMENT
                )
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: TransportVehicleItem, position: Int) {

    }

    //API call
    private fun getVehicleList() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getTransportVehicleList(object : OnApiCallCompleted<GetTransportVehicleListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetTransportVehicleListResponse = obj as GetTransportVehicleListResponse
                license_type.clear()
                license_type.addAll(response.listLicenseType)
                isListEmpty(response.list.size == 0)
                setAdapter(response)
            }

            override fun apiFailure(errorMessage: String) {
                showToast(requireActivity(), errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                showToast(requireActivity(), errorMessage)
            }
        })
    }

    private fun deleteTransportVehicle(vehicleID: String, position: Int) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.deleteTransportVehicle(vehicleID, object : OnApiCallCompleted<BaseResponse> {
            override fun apiSuccess(obj: Any?) {
                val baseResponse: BaseResponse = obj as BaseResponse

                if (baseResponse.isSuccess) {
                    list.removeAt(position)
                    if (vehicalListAdapter != null)
                        vehicalListAdapter!!.notifyDataSetChanged()
                    showToast(requireActivity(), baseResponse.message)
                }
            }

            override fun apiFailure(errorMessage: String) {
                showToast(requireActivity(), errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                showToast(requireActivity(), errorMessage)
            }
        })
    }
}
