package com.app.validity.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.TransportVehicalDropdownsResponse
import com.app.validity.model.TransportVehicleItem
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.*
import com.bumptech.glide.Glide
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_add_transport_vehical.*
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddTransportVehicalFragment(private val editThisItem: TransportVehicleItem?, private val licenseTypeList: MutableList<String>) : BaseFragment(), ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()
    private var transportVehicleDropDownsResponse = TransportVehicalDropdownsResponse()

    override fun setContentView(): Int = R.layout.fragment_add_transport_vehical

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
        val licenseTypeListNEW: MutableList<String> = ArrayList()
        licenseTypeListNEW.add(" -- Select -- ")
        licenseTypeListNEW.addAll(licenseTypeList)

        val spLicenseTypeArrayAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, licenseTypeListNEW)
        spLicenseTypeArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spLicenseType.adapter = spLicenseTypeArrayAdapter

        if (editThisItem != null) {
            for (i in licenseTypeListNEW.indices) {
                if (licenseTypeListNEW[i] == editThisItem.driverLicenseType) {
                    spLicenseType.setSelection(i)
                    break
                }
            }
            etRegNo.setText(editThisItem.registerNo)
            etDriverName.setText(editThisItem.driverName)
            etDriverAddress.setText(editThisItem.driverAddress)
            etDriverPhoneNumber.setText(editThisItem.driverPhoneNo)
            etDriverLicenseNumber.setText(editThisItem.driverLicenseNo)
            txtExpiryDate.text = editThisItem.driverLicenseExpiryDate
        }
        getTransportVehicleTypes()
    }

    override fun onItemClick(view: View, selectedItem: Uri?, position: Int) {
        when (view.id) {
            R.id.llDelete -> {
                selectedImageList.removeAt(position)
                rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
            }
            R.id.ivImage -> {
                checkImageSelectionPermission()
            }
        }
    }

    override fun onItemLongClick(view: View, selectedItem: Uri?, position: Int) {

    }

    @SuppressLint("SetTextI18n")
    @Suppress("RemoveSingleExpressionStringTemplate")
    override fun setListeners() {
        txtCancel.setOnClickListener { requireActivity().onBackPressed() }

        txtExpiryDate.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val mYear = myCalender.get(Calendar.YEAR)
            val mMonth = myCalender.get(Calendar.MONTH) + 1
            val mDay = myCalender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val monthInt = month + 1

                    val strDayOfMonth = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val strMonthOfYear = if (monthInt < 10) "0${monthInt}" else "$monthInt"
                    val strYear = if (year < 10) "0${year}" else "$year"

                    txtExpiryDate.text = "${strYear}-$strMonthOfYear-${strDayOfMonth}"
                    myCalender.set(
                        strYear.toInt(),
                        strMonthOfYear.toInt() - 1,
                        strDayOfMonth.toInt()
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtSelectImage.setOnClickListener { checkImageSelectionPermission() }

        txtAdd.setOnClickListener {
            if (spVehicleType.selectedItemPosition == 0) {
                Utility.showToast(requireActivity(), "Please select vehicle type.")
                spVehicleType.requestFocus()
            } else if (spVehicleBrand.selectedItemPosition == 0) {
                Utility.showToast(requireActivity(), "Please select vehicle brand.")
                spVehicleBrand.requestFocus()
            } else if (spVehicleCategory.selectedItemPosition == 0) {
                Utility.showToast(requireActivity(), "Please select vehicle category.")
                spVehicleCategory.requestFocus()
            } else if (TextUtils.isEmpty(etRegNo.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter register number.")
                etRegNo.requestFocus()
            } else if (TextUtils.isEmpty(etDriverName.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter driver name.")
                etDriverName.requestFocus()
            } else if (TextUtils.isEmpty(etDriverAddress.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter driver address.")
                etDriverAddress.requestFocus()
            } else if (TextUtils.isEmpty(etDriverPhoneNumber.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter phone number.")
                etDriverPhoneNumber.requestFocus()
            } else if (etDriverPhoneNumber.text.toString().trim().length < 10) {
                Utility.showToast(requireActivity(), "Please enter valid phone number.")
                etDriverPhoneNumber.requestFocus()
            } else if (TextUtils.isEmpty(etDriverLicenseNumber.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter driver license number.")
                etDriverLicenseNumber.requestFocus()
            } else if (TextUtils.isEmpty(txtExpiryDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select license expiry date.")
                txtExpiryDate.requestFocus()
            } else if (spLicenseType.selectedItemPosition == 0) {
                Utility.showToast(requireActivity(), "Please select license type.")
//            } else if (txtAdd.text != "Update" && selectedImageList.size == 1) {
//                Utility.showToast(requireActivity(), "Please select image.")
            } else {
                val vehicalTypeID = "${transportVehicleDropDownsResponse.type!![spVehicleType.selectedItemPosition - 1].id}"
                val vehicalBrandID = "${transportVehicleDropDownsResponse.brand!![spVehicleBrand.selectedItemPosition - 1].id}"
                val vehicalCategoryID = "${transportVehicleDropDownsResponse.category!![spVehicleCategory.selectedItemPosition - 1].id}"

                if (txtAdd.text == "Update") {
                    updateVehical(
                        "$vehicalTypeID",
                        "$vehicalBrandID",
                        "$vehicalCategoryID",
                        "${etRegNo.text}",
                        "${etDriverName.text}",
                        "${etDriverAddress.text}",
                        "${etDriverPhoneNumber.text}",
                        "${etDriverLicenseNumber.text}",
                        "${txtExpiryDate.text}",
                        "${spLicenseType.selectedItem}"
                    )
                } else {
                    addVehical(
                        "$vehicalTypeID",
                        "$vehicalBrandID",
                        "$vehicalCategoryID",
                        "${etRegNo.text}",
                        "${etDriverName.text}",
                        "${etDriverAddress.text}",
                        "${etDriverPhoneNumber.text}",
                        "${etDriverLicenseNumber.text}",
                        "${txtExpiryDate.text}",
                        "${spLicenseType.selectedItem}"
                    )
                }
            }
        }
    }

    private fun checkImageSelectionPermission() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                arrayListOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        selectPhoto()
                    } else {
                        Utility.showSettingsDialog(requireActivity())
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Utility.showSettingsDialog(requireActivity())
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener { }
            .onSameThread()
            .check()
    }

    private fun selectPhoto() {
        ImagePickerDialog(requireActivity(), object : onItemClick {
            override fun onCameraClicked() {
                displayCamera()
            }

            override fun onGalleryClicked() {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, GALLERY)
            }
        }).show()
    }

    private fun displayCamera() {
        val imagesFolder = File(requireActivity().getExternalFilesDir(null)!!.absolutePath)

        Log.e("Folder Path", imagesFolder.toString())

        try {
            if (!imagesFolder.exists()) {
                Log.e("Creating Folder: ", imagesFolder.toString())
                imagesFolder.mkdirs()
            }

            Log.e("Folder Exist: ", imagesFolder.exists().toString())

            val file = File(imagesFolder, Date().time.toString() + ".jpg")

            @Suppress("LiftReturnOrAssignment")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                selectedImage = FileProvider.getUriForFile(
                    requireActivity(),
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file
                )
            } else {
                selectedImage = Uri.fromFile(file)
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("resultCode: ", resultCode.toString())
        when (requestCode) {
            CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("from camera:", selectedImage.toString())
                    Glide.with(requireActivity()).load(selectedImage).into(imgVehical)
                    Log.e("from camera:", selectedImage.toString())
                    selectedImageList.add(selectedImage)
                    rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
                }
            }
            GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = data?.data!!
                    Glide.with(requireActivity()).load(selectedImage).into(imgVehical)
                    Log.e("from gallery:", selectedImage.toString())
                    selectedImageList.add(selectedImage)
                    rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
                }
            }
        }

    }

    @SuppressLint("DefaultLocale")
    override fun onResume() {
        super.onResume()
        if (editThisItem != null) {
            txtAdd.text = "Update"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_transport_vehicle, false)
        } else {
            txtAdd.text = "Add"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_transport_vehicle, false)
        }
    }

    // API call
    private fun addVehical(
        vehicle_type_id: String,
        vehicle_brand_id: String,
        vehicle_category_id: String,
        register_no: String,
        driver_name: String,
        driver_address: String,
        driver_phone_no: String,
        driver_license_no: String,
        driver_license_expiry_date: String,
        driver_license_type: String
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addTransportVehical(
            vehicle_type_id,
            vehicle_brand_id,
            vehicle_category_id,
            register_no,
            driver_name,
            driver_address,
            driver_phone_no,
            driver_license_no,
            driver_license_expiry_date,
            driver_license_type,
            selectedImageList
            , object : OnApiCallCompleted<BaseResponse> {
                @Suppress("ConstantConditionIf")
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: BaseResponse = obj as BaseResponse
                    if (baseResponse.isSuccess) {
                        Utility.showToast(requireActivity(), "Vehicle added successfully")
                        requireActivity().onBackPressed()
                    } else {
                        Utility.showToast(requireActivity(), baseResponse.message)
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

    private fun updateVehical(
        vehicle_type_id: String,
        vehicle_brand_id: String,
        vehicle_category_id: String,
        register_no: String,
        driver_name: String,
        driver_address: String,
        driver_phone_no: String,
        driver_license_no: String,
        driver_license_expiry_date: String,
        driver_license_type: String
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.updateTransportVehical(
            editThisItem!!.id.toString(),
            vehicle_type_id,
            vehicle_brand_id,
            vehicle_category_id,
            register_no,
            driver_name,
            driver_address,
            driver_phone_no,
            driver_license_no,
            driver_license_expiry_date,
            driver_license_type,
            selectedImageList
            , object : OnApiCallCompleted<BaseResponse> {
                @Suppress("ConstantConditionIf")
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: BaseResponse = obj as BaseResponse
                    if (baseResponse.isSuccess) {
                        Utility.showToast(requireActivity(), "Vehicle added successfully")
                        requireActivity().onBackPressed()
                    } else {
                        Utility.showToast(requireActivity(), baseResponse.message)
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

    private fun getTransportVehicleTypes() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getTransportVehicleTypes(object : OnApiCallCompleted<TransportVehicalDropdownsResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: TransportVehicalDropdownsResponse = obj as TransportVehicalDropdownsResponse
                transportVehicleDropDownsResponse = response
                val listType: MutableList<String> = ArrayList()
                val listBrand: MutableList<String> = ArrayList()
                val listCategory: MutableList<String> = ArrayList()

                listType.add(" -- Select -- ")
                listBrand.add(" -- Select -- ")
                listCategory.add(" -- Select -- ")

                if (response.type != null) for (i in response.type!!.indices) listType.add("${response.type!![i].name}")
                if (response.brand != null) for (i in response.brand!!.indices) listBrand.add("${response.brand!![i].name}")
                if (response.category != null) for (i in response.category!!.indices) listCategory.add("${response.category!![i].name}")

                val spTypeAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listType)
                spTypeAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                spVehicleType.adapter = spTypeAdapter

                val spBrandAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listBrand)
                spBrandAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                spVehicleBrand.adapter = spBrandAdapter

                val spCategoryAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listCategory)
                spCategoryAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                spVehicleCategory.adapter = spCategoryAdapter

                if (editThisItem != null) {
                    if (response.type != null) {
                        for (i in response.type!!.indices) {
                            if (editThisItem.transportVehicleType != null) {
                                if (response.type!![i].id == editThisItem.transportVehicleType!!.id) {
                                    spVehicleType.setSelection(i + 1)
                                    break
                                }
                            }
                        }
                    }

                    if (response.brand != null) {
                        for (i in response.brand!!.indices) {
                            if (editThisItem.transportVehicleBrand != null) {
                                if (response.brand!![i].id == editThisItem.transportVehicleBrand!!.id) {
                                    spVehicleBrand.setSelection(i + 1)
                                    break
                                }
                            }
                        }
                    }

                    if (response.category != null) {
                        for (i in response.category!!.indices) {
                            if (editThisItem.transportVehicleCategory != null) {
                                if (response.category!![i].id == editThisItem.transportVehicleCategory!!.id) {
                                    spVehicleCategory.setSelection(i + 1)
                                    break
                                }
                            }
                        }
                    }
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
