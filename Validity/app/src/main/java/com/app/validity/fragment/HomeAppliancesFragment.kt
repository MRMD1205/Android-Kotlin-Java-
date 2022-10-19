package com.app.validity.fragment


import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.HomeApplianceAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetHomeAppliancesListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_HOME_APPLIANCES_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_HOME_APPLIANCES_FRAGMENT
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_home_appliances.*
import org.json.JSONObject

class HomeAppliancesFragment : BaseFragment(), ItemClickCallback<GetHomeAppliancesListResponse.Datum> {

    override fun setContentView(): Int = R.layout.fragment_home_appliances

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        fabHomeAppliances.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddHomeAppliancesFragment(null), true, ADD_HOME_APPLIANCES_FRAGMENT)
        }
    }

    override fun onResume() {
        super.onResume()
        callGetListAPI()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_home_appliances, false)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: GetHomeAppliancesListResponse.Datum, position: Int) {
        when (view.id) {
            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewHomeAppliancesFragment(selectedItem), true, VIEW_HOME_APPLIANCES_FRAGMENT)
            }
            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddHomeAppliancesFragment(selectedItem), true, ADD_HOME_APPLIANCES_FRAGMENT)
            }
            R.id.llDelete -> {
                deleteIndustriesDialog(selectedItem)
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: GetHomeAppliancesListResponse.Datum, position: Int) {
        //deleteIndustriesDialog(selectedItem)
    }

    private fun setAdapter(listResponse: GetHomeAppliancesListResponse) {
        val itemList: ArrayList<GetHomeAppliancesListResponse.Datum> =
            listResponse.list as ArrayList<GetHomeAppliancesListResponse.Datum>
        rvHomeAppliances.adapter = HomeApplianceAdapter(itemList, this)
    }

    private fun deleteIndustriesDialog(selectedItem: GetHomeAppliancesListResponse.Datum) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_home_appliance))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deleteHomeAppliances(selectedItem.id.toString())
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    //API call
    private fun callGetListAPI() {
        ApiCallMethods(requireActivity()).getHomeAppliancesList(object : OnApiCallCompleted<GetHomeAppliancesListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetHomeAppliancesListResponse = obj as GetHomeAppliancesListResponse
                isListEmpty(response.list.size == 0)
                setAdapter(response)
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

    private fun deleteHomeAppliances(propertyId: String) {
        ApiCallMethods(requireActivity()).deleteHomeAppliances(
            propertyId,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val response: BaseResponse = obj as BaseResponse
                    if (response.isSuccess) {
                        callGetListAPI()
                    }
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
}
