package com.app.validity.fragment.vehicaltools

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditVehicleAccidentResponse
import com.app.validity.model.VehicalAccident
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_accident.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ViewAccidentFragment(var vehicalId: String, val vehicalItem: VehicalAccident?) : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_view_accident

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (vehicalItem != null) {
            if (!TextUtils.isEmpty(vehicalItem.accidentTime)) {
                txtAccidentTime.hint = vehicalItem.accidentTime
                val orgSdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val newSdf = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val dateObj: Date = orgSdf.parse(vehicalItem.accidentTime.toString())
                txtAccidentTime.text = getDotedText(SimpleDateFormat("KK:mm a", Locale.getDefault()).format(dateObj))
            }

            txtAccidentDate.text = getDotedText(vehicalItem.accidentDate)
            txtDriverName.text = getDotedText(vehicalItem.driverName)
            txtAmount.text = getDotedText(vehicalItem.amount)
            txtKmReading.text = getDotedText(vehicalItem.kmReading)
            txtDescription.text = getDotedText(vehicalItem.description)
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
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_accident, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleAccidentDetails(vehicalId, id, object : OnApiCallCompleted<EditVehicleAccidentResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehicleAccidentResponse = obj as EditVehicleAccidentResponse
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
