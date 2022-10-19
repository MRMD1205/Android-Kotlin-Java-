package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehicleInsuranceResponse
import com.app.validity.model.VehicalInsurance
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_insurance.*
import org.json.JSONObject

class ViewInsuranceFragment(var vehicalId: String, var vehicalItem: VehicalInsurance?) : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_view_insurance

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            txtCompany.text = getDotedText(vehicalItem!!.companyName)
            txtPolicyNo.text = getDotedText(vehicalItem!!.policyNumber)
            txtIssueDate.text = getDotedText(vehicalItem!!.issueDate)
            txtExpiryDate.text = getDotedText(vehicalItem!!.expiryDate)
            txtInsuranceAmount.text = getDotedText(vehicalItem!!.amount)
            txtPremium.text = getDotedText(vehicalItem!!.premium)
            txtAgentName.text = getDotedText(vehicalItem!!.agentName)
            txtAgentContactNumber.text = getDotedText(vehicalItem!!.agentContactNo)
            txtDescription.text = getDotedText(vehicalItem!!.description)
            txtPolicyType.text = getDotedText(vehicalItem!!.policyType)
            txtPaymentMode.text = getDotedText(vehicalItem!!.paymentMode)
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
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_insurance, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleInsuranceDetails(vehicalId, id, object : OnApiCallCompleted<EditVehicleInsuranceResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehicleInsuranceResponse = obj as EditVehicleInsuranceResponse
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
