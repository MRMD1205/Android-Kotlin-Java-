package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.IndustrialAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetIndustrialListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_INDUSTRIAL_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_INDUSTRIAL_FRAGMENT
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_industrial.*
import org.json.JSONObject

class IndustrialFragment : BaseFragment(), ItemClickCallback<GetIndustrialListResponse.Datum> {

    override fun setContentView(): Int = R.layout.fragment_industrial

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        fabIndustrial.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddIndustrialFragment(null), true, ADD_INDUSTRIAL_FRAGMENT)
        }
    }

    override fun onResume() {
        super.onResume()
        getIndustrialList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_industrial, false)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: GetIndustrialListResponse.Datum, position: Int) {
        when (view.id) {
            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewIndustrialFragment(selectedItem), true, VIEW_INDUSTRIAL_FRAGMENT)
            }
            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddIndustrialFragment(selectedItem), true, ADD_INDUSTRIAL_FRAGMENT)
            }
            R.id.llDelete -> {
                deleteIndustriesDialog(selectedItem)
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: GetIndustrialListResponse.Datum, position: Int) {

    }

    private fun setAdapter(listResponse: GetIndustrialListResponse) {
        val itemList: ArrayList<GetIndustrialListResponse.Datum> =
            listResponse.list as ArrayList<GetIndustrialListResponse.Datum>
        rvIndustrial.adapter = IndustrialAdapter(itemList, this)
    }

    private fun deleteIndustriesDialog(selectedItem: GetIndustrialListResponse.Datum) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_industy))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deleteIndustries(selectedItem.id.toString())
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    //API call
    private fun getIndustrialList() {
        ApiCallMethods(requireActivity()).getIndustrialList(object : OnApiCallCompleted<GetIndustrialListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetIndustrialListResponse = obj as GetIndustrialListResponse
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

    private fun deleteIndustries(propertyId: String) {
        ApiCallMethods(requireActivity()).deleteIndustries(
            propertyId,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val response: BaseResponse = obj as BaseResponse
                    if (response.isSuccess) {
                        getIndustrialList()
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
