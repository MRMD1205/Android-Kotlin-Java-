package com.app.validity.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.EditPersonalDocumentsResponse
import com.app.validity.model.GetPersonalDocumentListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.app.validity.util.Utility
import com.crashpot.network.OnApiCallCompleted
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_view_personal_document.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ViewPersonalDocumentFragment(val selectedItem: GetPersonalDocumentListResponse.Datum?) : BaseFragment(), ItemClickCallback<String?> {

    private lateinit var itemClickCallback: ItemClickCallback<String?>

    override fun setContentView(): Int = R.layout.fragment_view_personal_document

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        itemClickCallback = this
        if (selectedItem != null) {
            txtDocumentType.text = getDotedText(selectedItem.personalDocumentType?.name)
            txtName.text = getDotedText(selectedItem.name)
            txtCompanyName.text = getDotedText(selectedItem.companyName)
            txtSumAssured.text = getDotedText(selectedItem.sumAssured)
            txtStartDate.text = getDotedText(selectedItem.startDate)
            txtEndDate.text = getDotedText(selectedItem.endDate)
            txtAddress.text = getDotedText(selectedItem.address)
            txtFee.text = getDotedText(selectedItem.fee.toString())
            txtDescription.text = getDotedText(selectedItem.description)
            val listStatus: MutableList<String> = ArrayList()
            listStatus.add("Select")
            listStatus.add("Active")
            listStatus.add("Inactive")
            txtStatus.text = getDotedText(listStatus[selectedItem.status!!])
//            if (selectedItem.status != null)
//                txtStatus.text = getDotedText(selectedItem.status.toString())

            getDetailsAPI(selectedItem.id.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_personal_documents, false)
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
        apiCallMethods.storePersonalDocumentsReminder(selectedItem?.id.toString(), date, object : OnApiCallCompleted<Boolean> {
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
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getPersonalDocumentsDetails(id, object : OnApiCallCompleted<EditPersonalDocumentsResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditPersonalDocumentsResponse = obj as EditPersonalDocumentsResponse
                if (response.personalDocumentImageData != null)
                    rvImages.adapter = ShowImagesAdapter(response.personalDocumentImageData!!, itemClickCallback)
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

    override fun onItemClick(view: View, selectedItem: String?, position: Int) {

    }

    override fun onItemLongClick(view: View, selectedItem: String?, position: Int) {

    }
}
