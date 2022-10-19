package com.app.validity.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesNoClickAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.model.EditPropertyResponse
import com.app.validity.model.GetPropertyListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_view_property.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ViewPropertyFragment(val selectedItem: GetPropertyListResponse.Data?) : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_view_property

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        val listStatus: MutableList<String> = ArrayList()
        val listOwnership: MutableList<String> = ArrayList()

        listOwnership.add("Select")
        listOwnership.add("Owner")
        listOwnership.add("Rent")

        listStatus.add("Select")
        listStatus.add("Active")
        listStatus.add("Inactive")

        if (selectedItem != null) {

            txtLocationName.text = getDotedText(selectedItem.location)
            txtCityName.text = getDotedText(selectedItem.cityName)
            txtPropertyAddress.text = getDotedText(selectedItem.propertyAddress)
            txtPurchaseDate.text = getDotedText(selectedItem.purchaseDate)
            txtPurchasePrice.text = getDotedText(selectedItem.rentAmount)

            txtTenantName.text = getDotedText(selectedItem.tenantName)
            txtTenantNumber.text = getDotedText(selectedItem.tenantNumber)
            txtAgreementStartDate.text = getDotedText(selectedItem.agreementStartDate)
            txtAgreementEndDate.text = getDotedText(selectedItem.agreementEndDate)
            txtRentAmount.text = getDotedText(selectedItem.rentAmount.toString())
            txtRentedPropertyAddress.text = getDotedText(selectedItem.rentedPropertyAddress.toString())
            txtRentCollectionDate.text = getDotedText(selectedItem.rentCollectionDate)
            txtPropertyType.text = getDotedText(selectedItem.propertyType?.name)
            txtStatus.text = getDotedText(listStatus[selectedItem.status!!])
//            txtOwnershipStatus.text = getDotedText(listOwnership[selectedItem.ownershipStatus!!])
            txtOwnershipStatus.text = getDotedText(selectedItem.ownershipStatus)
            getDetailsAPI(selectedItem.id.toString())

            if (selectedItem.ownershipStatus != null && selectedItem.ownershipStatus == "Owner") {
                llOwned.visibility = View.VISIBLE
                llRented.visibility = View.GONE
            }

            if (selectedItem.ownershipStatus != null && selectedItem.ownershipStatus == "Rent") {
                llOwned.visibility = View.GONE
                llRented.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_property, false)
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
                requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val monthInt = month + 1

                    val strDayOfMonth = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val strMonthOfYear = if (monthInt < 10) "0${monthInt}" else "$monthInt"
                    val strYear = if (year < 10) "0${year}" else "$year"
                    cal.set(strYear.toInt(), strMonthOfYear.toInt() - 1, strDayOfMonth.toInt())

                    val strDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
                    builder.setMessage("${getString(R.string.want_set_reminder)} $strDate")
                    builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
                        callSetReminderAPI(strDate)
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

    private fun callSetReminderAPI(date: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.storePropertyReminder(selectedItem?.id.toString(), date, object : OnApiCallCompleted<Boolean> {
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

    private fun getDetailsAPI(id: String) {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getPropertyDetails(id, object : OnApiCallCompleted<EditPropertyResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditPropertyResponse = obj as EditPropertyResponse
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
