package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.model.VehicalExpense
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehicleExpenseResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_expense.*
import org.json.JSONObject

class ViewExpenseFragment(var vehicalId: String, val vehicalItem: VehicalExpense?) :
    BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_view_expense

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            txtExpenseDate.text = getDotedText(vehicalItem.expenseDate)
            txtTotalAmount.text = getDotedText(vehicalItem.amount)
            txtKmReading.text = getDotedText(vehicalItem.kmReading)
            txtDescription.text = getDotedText(vehicalItem.description)
            txtExpenseType.text = getDotedText(vehicalItem.expenseType)
            getDetailsAPI(vehicalItem.id.toString())
        }
    }

    override fun setListeners() {
        txtOk.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_expense, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleExpenseDetails(vehicalId, id, object : OnApiCallCompleted<EditVehicleExpenseResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehicleExpenseResponse = obj as EditVehicleExpenseResponse
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
