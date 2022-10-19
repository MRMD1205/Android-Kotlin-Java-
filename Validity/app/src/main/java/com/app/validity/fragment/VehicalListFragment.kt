package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.VehicalListAdapter
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetVehicleListResponse
import com.app.validity.model.VehicleItem
import com.app.validity.util.ADD_VEHICAL_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.TOOLS_FRAGMENT
import com.app.validity.base.BaseFragment
import com.app.validity.network.ApiCallMethods
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility.Companion.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.util.VIEW_VEHICAL_FRAGMENT
import kotlinx.android.synthetic.main.fragment_vehical_list.*
import org.json.JSONObject
import kotlin.collections.ArrayList

class VehicalListFragment : BaseFragment(), ItemClickCallback<VehicleItem> {

    var vehicleItem: ArrayList<VehicleItem> = ArrayList()
    var vehicalListAdapter: VehicalListAdapter? = null
    val listFuelType: MutableList<String> = ArrayList()
    val listVehicleCondition: MutableList<String> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_vehical_list

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        fabAddAddress.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddVehicalFragment(listVehicleCondition, listFuelType), true, ADD_VEHICAL_FRAGMENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getVehicleList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_vehicles, false)
    }

    private fun setAdapter(vehicleListResponse: GetVehicleListResponse) {
        vehicleItem = vehicleListResponse.list as ArrayList<VehicleItem>
        vehicalListAdapter = VehicalListAdapter(vehicleItem, this)
        rvVehicalList.adapter = vehicalListAdapter
    }

    private fun deleteVehicle(selectedItem: VehicleItem, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_vehicle))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deleteVehicle(selectedItem.id.toString(), position)
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onItemClick(view: View, selectedItem: VehicleItem, position: Int) {
        when (view.id) {

            R.id.llOpen -> {
                val frag = ToolsFragment(selectedItem.id.toString())
                (requireActivity() as DashboardActivity).navigateToFragment(frag, true, TOOLS_FRAGMENT)
            }

            R.id.llView -> {
                val bundle = Bundle()
                val frag = ViewVehicalFragment()
                bundle.putParcelable("VehicleItem", selectedItem)
                frag.arguments = bundle
                (requireActivity() as DashboardActivity).navigateToFragment(frag, true, VIEW_VEHICAL_FRAGMENT)
            }

            R.id.llDelete -> {
                deleteVehicle(selectedItem, position)
            }

            R.id.llEdit -> {
                val bundle = Bundle()
                val frag = AddVehicalFragment(listVehicleCondition, listFuelType)
                bundle.putParcelable("VehicleItem", selectedItem)
                frag.arguments = bundle
                (requireActivity() as DashboardActivity).navigateToFragment(frag, true, ADD_VEHICAL_FRAGMENT)
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: VehicleItem, position: Int) {

    }

    //API call
    private fun getVehicleList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleList(object : OnApiCallCompleted<GetVehicleListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetVehicleListResponse = obj as GetVehicleListResponse
                listVehicleCondition.clear()
                listFuelType.clear()
                listVehicleCondition.addAll(response.listVehicleCondition)
                listFuelType.addAll(response.listFuelType)
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

    private fun deleteVehicle(vehicleID: String, position: Int) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.deleteVehicle(vehicleID, object : OnApiCallCompleted<BaseResponse> {
            override fun apiSuccess(obj: Any?) {
                val baseResponse: BaseResponse = obj as BaseResponse

                if (baseResponse.isSuccess) {
                    vehicleItem.removeAt(position)
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
