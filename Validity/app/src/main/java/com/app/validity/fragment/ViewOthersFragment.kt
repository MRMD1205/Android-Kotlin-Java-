package com.app.validity.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.EditOthersResponse
import com.app.validity.model.GetOthersListResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_view_others.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ViewOthersFragment(val item: GetOthersListResponse.Datum?) : BaseFragment(), ItemClickCallback<String?> {

    private lateinit var itemClickCallback: ItemClickCallback<String?>

    override fun setContentView(): Int = R.layout.fragment_view_others

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        itemClickCallback = this
        if (item != null) {
            txtPucNo.text = getDotedText(item.name)
            txtIssueDate.text = getDotedText(item.startDate)
            txtExpiryDate.text = getDotedText(item.endDate)
            txtDescription.text = getDotedText(item.description)
            getDetailsAPI(item.id.toString())
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
        apiCallMethods.storeOthersReminder(item?.id.toString(), date, object : OnApiCallCompleted<Boolean> {
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_others, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getOthersDetails(id, object : OnApiCallCompleted<EditOthersResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditOthersResponse = obj as EditOthersResponse
                if (response.otherImageData != null)
                    rvImages.adapter = ShowImagesAdapter(response.otherImageData!!, itemClickCallback)
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
