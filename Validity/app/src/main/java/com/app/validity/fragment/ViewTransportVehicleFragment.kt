package com.app.validity.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditTransportVehicleResponse
import com.app.validity.model.TransportVehicleItem
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_view_transport_vehicle.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ViewTransportVehicleFragment(private val editThisItem: TransportVehicleItem?) : BaseFragment() {

    var vehicalId: String = ""

    override fun setContentView(): Int = R.layout.fragment_view_transport_vehicle

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (editThisItem != null) {

            val vehicleType = if (editThisItem.transportVehicleType == null) ""
            else if (editThisItem.transportVehicleType?.name == null) ""
            else editThisItem.transportVehicleType?.name

            val vehicleCategory = if (editThisItem.transportVehicleCategory == null) ""
            else if (editThisItem.transportVehicleCategory?.name == null) ""
            else editThisItem.transportVehicleCategory?.name

            val vehicleBrand = if (editThisItem.transportVehicleBrand == null) ""
            else if (editThisItem.transportVehicleBrand?.name == null) ""
            else editThisItem.transportVehicleBrand?.name

            txtVehicleType.text = getDotedText(vehicleType)
            txtVehicleBrand.text = getDotedText(vehicleBrand)
            txtVehicleCategory.text = getDotedText(vehicleCategory)
            txtVehicleRegisterNo.text = getDotedText(editThisItem.registerNo)
            txtDriverName.text = getDotedText(editThisItem.driverName)
            txtDriverAddress.text = getDotedText(editThisItem.driverAddress)
            txtDriverPhoneNumber.text = getDotedText(editThisItem.driverPhoneNo)
            txtDriverLicenseNumber.text = getDotedText(editThisItem.driverLicenseNo)
            txtLicenseExpiryDate.text = getDotedText(editThisItem.driverLicenseExpiryDate)
            txtLicenseType.text = getDotedText(editThisItem.driverLicenseType)
            getDetailsAPI(editThisItem.id.toString())

            vehicalId = editThisItem.id.toString()
        }
    }

    override fun setListeners() {
        txtOk.setOnClickListener {
            requireActivity().onBackPressed()
        }
        fabAddRemainder.setOnClickListener {
            val cal = Calendar.getInstance()
            val mYear = cal.get(Calendar.YEAR)
            val mMonth = cal.get(Calendar.MONTH) + 1
            val mDay = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val monthInt = month + 1

                    val strDayOfMonth = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val strMonthOfYear = if (monthInt < 10) "0${monthInt}" else "$monthInt"
                    val strYear = if (year < 10) "0${year}" else "$year"
                    cal.set(strYear.toInt(), strMonthOfYear.toInt() - 1, strDayOfMonth.toInt())

                    val strDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
                    builder.setMessage("${getString(R.string.want_set_reminder)} $strDate")
                    builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
                        transportVehiclReminder(strDate)
                    }
                    builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
                        dialog.dismiss()
                    }
                    builder.show()

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_transport_vehicle, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getTransportVehicleDetails(id, object : OnApiCallCompleted<EditTransportVehicleResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditTransportVehicleResponse = obj as EditTransportVehicleResponse
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

    private fun transportVehiclReminder(date: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.transportVehiclReminder(vehicalId, date, object : OnApiCallCompleted<Boolean> {
            override fun apiSuccess(obj: Any?) {
                val isSuccess: Boolean = obj as Boolean
                if (isSuccess) {
                    Utility.showToast(requireActivity(), getString(R.string.reminder_added_successfully))
                } else {
                    Utility.showToast(requireActivity(), getString(R.string.msg_server_error))
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
