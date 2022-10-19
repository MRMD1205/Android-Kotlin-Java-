package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.RemindersAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.RemindersItem
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_remiders.*
import org.json.JSONObject

class RemindersFragment : BaseFragment(), ItemClickCallback<RemindersItem> {

    override fun setContentView(): Int = R.layout.fragment_remiders

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
//        fabHomeAppliances.setOnClickListener {
//            (requireActivity() as DashboardActivity).navigateToFragment(
//                AddHomeAppliancesFragment(null), true, ADD_HOME_APPLIANCES_FRAGMENT
//            )
//        }
    }

    override fun onResume() {
        super.onResume()
        callGetListAPI()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_reminders, false)
    }

    override fun setListeners() {
    }

    override fun onItemClick(view: View, selectedItem: RemindersItem, position: Int) {
//        when (view.id) {
//            R.id.llView -> {
//                (requireActivity() as DashboardActivity).navigateToFragment(
//                    ViewHomeAppliancesFragment(selectedItem), true, VIEW_HOME_APPLIANCES_FRAGMENT
//                )
//            }
//            R.id.llEdit -> {
//                (requireActivity() as DashboardActivity).navigateToFragment(
//                    AddHomeAppliancesFragment(selectedItem), true, ADD_HOME_APPLIANCES_FRAGMENT
//                )
//            }
//            R.id.llDelete -> {
//                deleteIndustriesDialog(selectedItem)
//            }
//        }
    }

    override fun onItemLongClick(view: View, selectedItem: RemindersItem, position: Int) {
        //deleteIndustriesDialog(selectedItem)
    }

    private fun setAdapter(list: MutableList<RemindersItem>) {
        val itemList: MutableList<RemindersItem> = list as ArrayList<RemindersItem>
        rvRemiders.adapter = RemindersAdapter(itemList, this)
    }

    private fun deleteIndustriesDialog(selectedItem: RemindersItem) {
        val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
        builder.setMessage(getString(R.string.want_delete_this_home_appliance))
        builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
            dialog.dismiss()
//            deleteHomeAppliances(selectedItem.id.toString())
        }
        builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
    }

    //API call
    private fun callGetListAPI() {
        ApiCallMethods(requireActivity()).getReminderList(object : OnApiCallCompleted<MutableList<RemindersItem>> {
            override fun apiSuccess(obj: Any?) {
                val response: MutableList<RemindersItem> = obj as MutableList<RemindersItem>
                isListEmpty(response.size == 0)
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

//    private fun deleteHomeAppliances(propertyId: String) {
//        ApiCallMethods(requireActivity()).deleteHomeAppliances(
//            propertyId,
//            object : OnApiCallCompleted<BaseResponse> {
//                override fun apiSuccess(obj: Any?) {
//                    val response: BaseResponse = obj as BaseResponse
//                    if (response.isSuccess) {
//                        callGetListAPI()
//                    }
//                }
//
//                override fun apiFailure(errorMessage: String) {
//                    Utility.showToast(requireActivity(), errorMessage)
//                }
//
//                override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
//                    val errorMessage: String = errorObject.getString(MESSAGE)
//                    Utility.showToast(requireActivity(), errorMessage)
//                }
//            })
//    }
}
