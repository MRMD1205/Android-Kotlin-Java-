package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehiclePermitResponse
import com.app.validity.model.VehicalPermit
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_permit.*
import org.json.JSONObject

class ViewPermitFragment(var vehicalId: String, var vehicalItem: VehicalPermit?) : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_view_permit

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            txtPermitNo.text = getDotedText(vehicalItem!!.permitNumber)
            txtIssueDate.text = getDotedText(vehicalItem!!.issueDate)
            txtExpiryDate.text = getDotedText(vehicalItem!!.expiryDate)
            txtCost.text = getDotedText(vehicalItem!!.amount)
            txtDescription.text = getDotedText(vehicalItem!!.description)
            txtPermitType.text = getDotedText(vehicalItem!!.permitType)
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
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_permit, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehiclePermitDetails(vehicalId, id, object : OnApiCallCompleted<EditVehiclePermitResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehiclePermitResponse = obj as EditVehiclePermitResponse
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
