package com.app.validity.fragment


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ToolsAdapter
import com.app.validity.fragment.vehicaltools.*
import com.app.validity.model.DashboardList
import com.app.validity.util.*
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.network.ApiCallMethods
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_tools.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ToolsFragment(var vehicalId: String) : BaseFragment(), ItemClickCallback<DashboardList> {

    override fun setContentView(): Int = R.layout.fragment_tools

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        val toolsList: ArrayList<DashboardList> = ArrayList()
        toolsList.add(DashboardList(1, R.drawable.ic_refual, "Refuel"))
        toolsList.add(DashboardList(2, R.drawable.ic_services, "Service"))
        toolsList.add(DashboardList(3, R.drawable.ic_expences, "Expense"))
        toolsList.add(DashboardList(4, R.drawable.ic_insurance, "Insurance"))
        toolsList.add(DashboardList(5, R.drawable.ic_permit, "Permit"))
        toolsList.add(DashboardList(6, R.drawable.ic_puc, "PUC"))
        toolsList.add(DashboardList(7, R.drawable.ic_accident, "Accident"))
        toolsList.add(DashboardList(8, R.drawable.ic_summery, "Summery"))
        rvVehicalCategoryList.adapter = ToolsAdapter(toolsList, this)
    }

    override fun setListeners() {
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
        apiCallMethods.storeVehicleReminder(vehicalId, date, object : OnApiCallCompleted<Boolean> {
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
        isListEmpty(false)
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_tools, false)
    }

    override fun onItemClick(view: View, selectedItem: DashboardList, position: Int) {
        when (selectedItem.pos) {
            1 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    RefuelFragment(vehicalId), true, REFUEL_FRAGMENT
                )
            }
            2 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ServicesFragment(vehicalId), true, SERVICES_FRAGMENT
                )
            }
            3 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    ExpenseFragment(vehicalId), true, EXPENSE_FRAGMENT
                )
            }
            4 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    InsuranceFragment(vehicalId), true, INSURANCE_FRAGMENT
                )
            }
            5 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    PermitFragment(vehicalId), true, PERMIT_FRAGMENT
                )
            }
            6 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    PucFragment(vehicalId), true, PUC_FRAGMENT
                )
            }
            7 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    AccidentFragment(vehicalId), true, ACCIDENT_FRAGMENT
                )
            }
            8 -> {
                (requireActivity() as DashboardActivity).navigateToFragment(
                    SummeryFragment(vehicalId), true, SUMMERY_FRAGMENT
                )
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: DashboardList, position: Int) {

    }
}
