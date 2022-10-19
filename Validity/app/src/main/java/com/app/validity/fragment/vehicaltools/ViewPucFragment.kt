package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehiclePucResponse
import com.app.validity.model.VehicalPUC
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_puc.*
import org.json.JSONObject

class ViewPucFragment(var vehicalId: String, val vehicalItem: VehicalPUC?) : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_view_puc

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            txtPucNo.text = getDotedText(vehicalItem.pucNumber)
            txtIssueDate.text = getDotedText(vehicalItem.issueDate)
            txtExpiryDate.text = getDotedText(vehicalItem.expiryDate)
            txtCost.text = getDotedText(vehicalItem.amount)
            txtDescription.text = getDotedText(vehicalItem.description)
            getDetailsAPI(vehicalItem!!.id.toString())
        }
    }

    override fun setListeners() {
        txtOk.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_puc, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehiclePucDetails(vehicalId, id, object : OnApiCallCompleted<EditVehiclePucResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehiclePucResponse = obj as EditVehiclePucResponse
                if (response.imageList != null)
                    rvImages.adapter = ShowImagesNoClickAdapter(response.imageList!!)
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
