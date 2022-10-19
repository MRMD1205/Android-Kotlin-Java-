package com.app.validity.fragment.vehicaltools


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.VahicalServiceListAdapter
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetVehicleServicesListResponse
import com.app.validity.model.VehicalServiceItem
import com.app.validity.util.ADD_SERVICES_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.base.BaseFragment
import com.app.validity.network.ApiCallMethods
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.util.VIEW_SERVICES_FRAGMENT
import kotlinx.android.synthetic.main.fragment_services.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ServicesFragment(var vehicalId: String) : BaseFragment(), ItemClickCallback<VehicalServiceItem> {

    val listServiceType: MutableList<String> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_services

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        fabServices.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddServicesFragment(vehicalId, null, listServiceType), true, ADD_SERVICES_FRAGMENT
            )
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
        apiCallMethods.storeVehicleServicesReminder(vehicalId, date, object : OnApiCallCompleted<Boolean> {
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
        getVehicleList()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_services, false)
    }

    //API call
    private fun getVehicleList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleServices(vehicalId, object : OnApiCallCompleted<GetVehicleServicesListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetVehicleServicesListResponse = obj as GetVehicleServicesListResponse
                listServiceType.clear()
                listServiceType.addAll(response.listServiceType)
                isListEmpty(response.list.size == 0)
                setAdapter(response)
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

    private fun setAdapter(vehicleListResponse: GetVehicleServicesListResponse) {
        rvServices.adapter = VahicalServiceListAdapter(
            vehicleListResponse.list as ArrayList<VehicalServiceItem>
            , this
        )
    }

    override fun onItemClick(view: View, selectedItem: VehicalServiceItem, position: Int) {
        when (view.id) {

            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewServicesFragment(vehicalId, selectedItem), true, VIEW_SERVICES_FRAGMENT
                )
            }

            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddServicesFragment(vehicalId, selectedItem, listServiceType), true, ADD_SERVICES_FRAGMENT
                )
            }

            R.id.llDelete -> {
                val builder = MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDialogTheme)
                builder.setMessage(getString(R.string.want_delete_this))
                builder.setPositiveButton(getString(R.string.dailog_ok)) { dialog, p1 ->
                    dialog.dismiss()
                    callDeleteToolAPI(selectedItem.id.toString())
                }
                builder.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, p1 ->
                    dialog.dismiss()
                }
                builder.show()
            }

        }
    }

    override fun onItemLongClick(view: View, selectedItem: VehicalServiceItem, position: Int) {

    }

    private fun callDeleteToolAPI(toolID: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.deleteService(vehicalId, toolID, object : OnApiCallCompleted<BaseResponse> {
            override fun apiSuccess(obj: Any?) {
                val baseResponse: BaseResponse = obj as BaseResponse
                Utility.showToast(requireActivity(), baseResponse.message)
                if (baseResponse.isSuccess) {
                    getVehicleList()
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
