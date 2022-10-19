package com.app.validity.fragment.vehicaltools


import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.model.GetVehicleSummeryResponse
import com.app.validity.model.VehicalSummery
import com.app.validity.util.MESSAGE
import com.app.validity.base.BaseFragment
import com.app.validity.network.ApiCallMethods
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_summery.*
import org.json.JSONObject

class SummeryFragment(var vehicalId: String) : BaseFragment(), CompoundButton.OnCheckedChangeListener {
    private var isCheckBoxCheck: Boolean = false
    var sum: Int = 0
    override fun setContentView(): Int = R.layout.fragment_summery

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        getVehicleList()
    }

    override fun setListeners() {
        chkAddPurchase.setOnCheckedChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_summery, false)
    }

    //API call
    private fun getVehicleList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleSummery(vehicalId, object : OnApiCallCompleted<GetVehicleSummeryResponse> {
            override fun apiSuccess(obj: Any?) {
                val getVehicleListResponse: GetVehicleSummeryResponse = obj as GetVehicleSummeryResponse
                if (getVehicleListResponse.summeryData != null)
                    setData(getVehicleListResponse.summeryData)
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

    private fun setData(vehicalSummery: VehicalSummery) {
        txtPurchaseProiceValue.setText(vehicalSummery.purchasePrice.toString())
        txtRefuelValue.setText(vehicalSummery.fuelSum.toString())
        txtServiceValue.setText(vehicalSummery.serviceSum.toString())
        txtExpenseValue.setText(vehicalSummery.expenseSum.toString())
        txtInsuranceValue.setText(vehicalSummery.insuranceSum.toString())
        txtPermitValue.setText(vehicalSummery.permitSum.toString())
        txtPUCValue.setText(vehicalSummery.pucSum.toString())
        if (isCheckBoxCheck) {
            sum = vehicalSummery.purchasePrice!! +
                    vehicalSummery.fuelSum!! +
                    vehicalSummery.serviceSum!! +
                    vehicalSummery.expenseSum!! +
                    vehicalSummery.insuranceSum!! +
                    vehicalSummery.permitSum!! +
                    vehicalSummery.pucSum!!
        } else {
            sum = vehicalSummery.fuelSum!! +
                    vehicalSummery.serviceSum!! +
                    vehicalSummery.expenseSum!! +
                    vehicalSummery.insuranceSum!! +
                    vehicalSummery.permitSum!! +
                    vehicalSummery.pucSum!!
        }
        txtTotalExpenditureValue.setText(sum.toString())
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        isCheckBoxCheck = isChecked
        getVehicleList()
    }
}
