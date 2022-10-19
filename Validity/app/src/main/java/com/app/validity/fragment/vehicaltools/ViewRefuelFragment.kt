package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.model.VehicalReFuel
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehicleRefuelResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_refuel.*
import org.json.JSONObject

class ViewRefuelFragment(var vehicalId: String, val vehicalItem: VehicalReFuel?) : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_view_refuel

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            txtFuelDate.text = getDotedText(vehicalItem.date)
            txtTotalAmount.text = getDotedText(vehicalItem.amount)
            txtFuelPrice.text = getDotedText(vehicalItem.fuelPrice)
            txtQuantity.text = getDotedText(vehicalItem.quantity)
            txtFuelStation.text = getDotedText(vehicalItem.fuelStation)
            txtKmReading.text = getDotedText(vehicalItem.kmReading)
            txtFuelType.text = getDotedText(vehicalItem.fuelType)
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
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_refuel, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleRefuelDetails(vehicalId, id, object : OnApiCallCompleted<EditVehicleRefuelResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehicleRefuelResponse = obj as EditVehicleRefuelResponse
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
