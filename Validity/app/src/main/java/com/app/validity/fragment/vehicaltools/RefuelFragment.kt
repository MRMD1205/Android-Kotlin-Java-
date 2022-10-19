package com.app.validity.fragment.vehicaltools

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.VehicalReFuelListAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetVehicleReFuelListResponse
import com.app.validity.model.VehicalReFuel
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.ADD_REFUEL_FRAGMENT
import com.app.validity.util.MESSAGE
import com.app.validity.util.VIEW_REFUEL_FRAGMENT
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_refuel.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RefuelFragment(var vehicalId: String) : BaseFragment(), ItemClickCallback<VehicalReFuel> {
    val listFuelType: MutableList<String> = ArrayList()
    override fun setContentView(): Int = R.layout.fragment_refuel

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        fabRefuel.setOnClickListener {
            (requireActivity() as DashboardActivity).navigateToFragment(
                AddRefuelFragment(vehicalId, null, listFuelType), true, ADD_REFUEL_FRAGMENT
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
        apiCallMethods.storeVehicleRefuelReminder(vehicalId, date, object : OnApiCallCompleted<Boolean> {
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
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_refuel, false)
    }

    //API call
    private fun getVehicleList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleFuels(vehicalId, object : OnApiCallCompleted<GetVehicleReFuelListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetVehicleReFuelListResponse = obj as GetVehicleReFuelListResponse
                listFuelType.clear()
                listFuelType.addAll(response.listFuelType)
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

    private fun setAdapter(response: GetVehicleReFuelListResponse) {
        rvRefuel.adapter =
            VehicalReFuelListAdapter(response.list as ArrayList<VehicalReFuel>, this)
    }

    override fun onItemClick(view: View, selectedItem: VehicalReFuel, position: Int) {
        when (view.id) {

            R.id.llView -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ViewRefuelFragment(vehicalId, selectedItem), true, VIEW_REFUEL_FRAGMENT
                )
            }

            R.id.llEdit -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AddRefuelFragment(vehicalId, selectedItem, listFuelType), true, ADD_REFUEL_FRAGMENT
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

    override fun onItemLongClick(view: View, selectedItem: VehicalReFuel, position: Int) {

    }

    private fun callDeleteToolAPI(toolID: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.deleteFuels(vehicalId, toolID, object : OnApiCallCompleted<BaseResponse> {
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
