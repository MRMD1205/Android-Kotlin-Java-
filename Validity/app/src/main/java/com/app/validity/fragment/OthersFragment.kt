package com.app.validity.fragment

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.OthersListAdapter
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetOthersListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_OTHERS_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_OTHERS_DETAILS
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_others.*
import org.json.JSONObject

class OthersFragment : BaseFragment(), ItemClickCallback<GetOthersListResponse.Datum> {
    override fun setContentView(): Int = R.layout.fragment_others

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        fabOthers.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddOthersFragment(null), true, ADD_OTHERS_FRAGMENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getOthersList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_others, false)
    }

    //API call
    private fun getOthersList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getOthers(object : OnApiCallCompleted<GetOthersListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetOthersListResponse = obj as GetOthersListResponse
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

    private fun setAdapter(vehicleListResponse: GetOthersListResponse) {
        val adapter: OthersListAdapter =
            OthersListAdapter(
                vehicleListResponse.list as ArrayList<GetOthersListResponse.Datum>
                , this
            )
        rvOthers.adapter = adapter
    }

    override fun onItemClick(view: View, selectedItem: GetOthersListResponse.Datum, position: Int) {
        when (view.id) {
            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewOthersFragment(selectedItem), true, VIEW_OTHERS_DETAILS
                )
            }
            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddOthersFragment(selectedItem), true, ADD_OTHERS_FRAGMENT
                )
            }
            R.id.llDelete -> {
                val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
                builder.setMessage(getString(R.string.want_delete_this))
                builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
                    dialog.dismiss()
                    callDeleteToolAPI(selectedItem.id.toString())
                }
                builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
                    dialog.dismiss()
                }
                builder.show()
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: GetOthersListResponse.Datum, position: Int) {

    }

    private fun callDeleteToolAPI(othersID: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.deleteOthers(othersID, object : OnApiCallCompleted<BaseResponse> {
            override fun apiSuccess(obj: Any?) {
                val baseResponse: BaseResponse = obj as BaseResponse
                Utility.showToast(requireActivity(), baseResponse.message)
                if (baseResponse.isSuccess) {
                    getOthersList()
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
