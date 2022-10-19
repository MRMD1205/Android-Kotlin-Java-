package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.PropertyAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetPropertyListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_PROPERTY_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_PROPERTY_FRAGMENT
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_property.*
import org.json.JSONObject

class PropertyFragment : BaseFragment(), ItemClickCallback<GetPropertyListResponse.Data> {

    val listOwnershipStatus: MutableList<String> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_property

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        fabProperty.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddPropertyFragment(null, listOwnershipStatus), true, ADD_PROPERTY_FRAGMENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getPropertyList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_property, false)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: GetPropertyListResponse.Data, position: Int) {
        when (view.id) {
            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewPropertyFragment(selectedItem), true, VIEW_PROPERTY_FRAGMENT
                )
            }
            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddPropertyFragment(selectedItem, listOwnershipStatus), true, ADD_PROPERTY_FRAGMENT
                )
            }
            R.id.llDelete -> {
                deletePropertyDialog(selectedItem)
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: GetPropertyListResponse.Data, position: Int) {

    }

    private fun setAdapter(listResponse: GetPropertyListResponse) {
        val itemList = listResponse.list
        rvProperty.adapter = PropertyAdapter(itemList, this)
    }

    private fun deletePropertyDialog(selectedItem: GetPropertyListResponse.Data) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_property))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
            deleteProperty(selectedItem.id.toString())
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    //API call
    private fun getPropertyList() {
        ApiCallMethods(requireActivity()).getPropertyList(object
            : OnApiCallCompleted<GetPropertyListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetPropertyListResponse = obj as GetPropertyListResponse
                listOwnershipStatus.clear()
                listOwnershipStatus.add("Select")
                listOwnershipStatus.addAll(response.listOwnershipStatus)
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

    private fun deleteProperty(propertyId: String) {
        ApiCallMethods(requireActivity()).deleteProperty(
            propertyId,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val response: BaseResponse = obj as BaseResponse
                    if (response.isSuccess) {
                        getPropertyList()
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
