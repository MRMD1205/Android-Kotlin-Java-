package com.app.validity.fragment


import android.os.Bundle
import android.view.View
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ShowImagesAdapter
import com.app.validity.model.VehicleItem
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.EditVehicleResponse
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import kotlinx.android.synthetic.main.fragment_view_vehical.*
import org.json.JSONObject

class ViewVehicalFragment : BaseFragment(), ItemClickCallback<String?> {

    private lateinit var itemClickCallback: ItemClickCallback<String?>

    override fun setContentView(): Int = R.layout.fragment_view_vehical

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        itemClickCallback = this
        if (arguments != null && arguments!!.getParcelable<VehicleItem>("VehicleItem") != null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_vehicles, false)
            val item: VehicleItem = arguments!!.getParcelable<VehicleItem>("VehicleItem") as VehicleItem
            txtModelName.text = getDotedText(item.name)
            txtOwnerName.text = getDotedText(item.ownerName)
            txtVehicleType.text = getDotedText(item.vehicleType?.name)
            txtVehicleBrand.text = getDotedText(item.vehicleBrand?.name)
            txtBuildYear.text = getDotedText(item.buildYear)
            txtRegistrationNumber.text = getDotedText(item.registrationNumber)
            txtTankCapacity.text = getDotedText(item.tankCapicity)
            txtPurchasePrice.text = getDotedText(item.purchasePrice)
            txtPurchaseDate.text = getDotedText(item.purchaseDate)
            txtKmReading.text = getDotedText(item.kmReading.toString())
            txtVehicleCondition.text = getDotedText(item.vehicleCondition)
            txtFuelType.text = getDotedText(item.fuelType)
            getDetailsAPI(item.id.toString())
        }
    }

    override fun setListeners() {
        txtOk.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_view_vehicles, false)
    }

    private fun getDetailsAPI(id: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleDetails(id, object : OnApiCallCompleted<EditVehicleResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: EditVehicleResponse = obj as EditVehicleResponse
                if (response.vehicleImages != null)
                    rvImages.adapter = ShowImagesAdapter(response.vehicleImages!!, itemClickCallback)
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
