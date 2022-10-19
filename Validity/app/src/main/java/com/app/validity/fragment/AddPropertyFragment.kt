package com.app.validity.fragment

import android.Manifest
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetPropertyListResponse
import com.app.validity.model.GetPropertyTypeListResponse
import com.app.validity.model.PropertyType
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
import kotlinx.android.synthetic.main.fragment_add_property_new.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPropertyFragment(val selectedItem: GetPropertyListResponse.Data?, val listOwnershipStatus: MutableList<String>) : BaseFragment(), ItemClickCallback<Uri?> {

    var spPropertyTypeAdapter: ArrayAdapter<String>? = null
    val listStatus: MutableList<String> = ArrayList()
    var propertyType: MutableList<PropertyType> = ArrayList()
    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_add_property_new

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        //Todo PropertyType
        getPropertyTypesList()

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)

        //Todo OwnershipStatus
        val spOwnershipAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listOwnershipStatus)
        spOwnershipAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spOwnershipStatus.adapter = spOwnershipAdapter

        //Todo Status
        listStatus.add("Select")
        listStatus.add("Active")
        listStatus.add("Inactive")
        val spStatusAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listStatus)
        spStatusAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spStatus.adapter = spStatusAdapter

        if (selectedItem != null) {

            etLocationName.setText("")
            etCityName.setText(selectedItem.cityName)
            etPropertyAddress.setText(selectedItem.propertyAddress)
            txtPurchaseDate.setText(selectedItem.purchaseDate)

            if (selectedItem.ownershipStatus != null && selectedItem.ownershipStatus == "Rent")
                etPrice.setText(selectedItem.rentAmount)
            else if (selectedItem.ownershipStatus != null && selectedItem.ownershipStatus == "Owner") {
                etPrice.setText(selectedItem.purchaseDate)
            } else {
                etPrice.setText(selectedItem.rentAmount)
            }

            etPrice.setText(selectedItem.rentAmount)

            etTenantName.setText(selectedItem.tenantName.toString())
            etTenantPermanentAddress.setText(selectedItem.tenantPermenentAddress)
            etRentedPropertyAddress.setText("")
            etTenantNumber.setText(selectedItem.tenantNumber)
            txtAgreementStartDate.setText(selectedItem.agreementStartDate)
            txtAgreementEndDate.setText(selectedItem.agreementEndDate)
            etRentAmount.setText(selectedItem.rentAmount.toString())
            txtRentCollectionDate.setText(selectedItem.rentCollectionDate)

            for (i in propertyType.indices) {
                if (selectedItem.propertyType?.name == propertyType[i].name)
                    spPropertyType.setSelection(selectedItem.propertyType?.id!!)
            }

            for (i in listOwnershipStatus.indices) {
                if (listOwnershipStatus[i] == selectedItem.ownershipStatus) {
                    spOwnershipStatus.setSelection(i)
                }
            }
            for (i in listStatus.indices) {
                if (i == selectedItem.status) {
                    spStatus.setSelection(i)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedItem == null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_property, false)
        } else {
            txtAdd.text = "Update"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_property, false)
        }
    }

    override fun setListeners() {
        spOwnershipStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spOwnershipStatus.selectedItem.toString() == "Select") {
                    llOwned.visibility = View.GONE
                    llRented.visibility = View.GONE
                }
                if (spOwnershipStatus.selectedItem.toString() == "Owner") {
                    llOwned.visibility = View.VISIBLE
                    llRented.visibility = View.GONE
                }
                if (spOwnershipStatus.selectedItem.toString() == "Rent") {
                    llOwned.visibility = View.GONE
                    llRented.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        txtPurchaseDate.setOnClickListener {
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
                    val finalDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    txtPurchaseDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        txtAgreementStartDate.setOnClickListener {
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
                    val finalDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    txtAgreementStartDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        txtAgreementEndDate.setOnClickListener {
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
                    val finalDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    txtAgreementEndDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        txtRentCollectionDate.setOnClickListener {
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
                    val finalDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    txtRentCollectionDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        txtSelectImage.setOnClickListener { checkImageSelectionPermission() }
        txtAdd.setOnClickListener {

            if (spOwnershipStatus.selectedItem.toString() == "Select") {
                Utility.showToast(requireActivity(), "Please select ownership status")
            } else if (spOwnershipStatus.selectedItem.toString() == "Owner") {
                /** Common */
                val propertyType: String = propertyType.get(spPropertyType.selectedItemPosition).id.toString()
                val ownershipStatus: String = spOwnershipStatus.selectedItem.toString()
                val status: String = spStatus.selectedItemPosition.toString()

                /** Own */
                val locationName = etLocationName.text.toString()
                val cityName = etCityName.text.toString()
                val propertyAddress = etPropertyAddress.text.toString()
                val purchaseDate = txtPurchaseDate.text.toString()
                val price = etPrice.text.toString()
                if (TextUtils.isEmpty(locationName)) {
                    Utility.showToast(requireActivity(), "Please enter location name")
                    etLocationName.requestFocus()
                } else if (TextUtils.isEmpty(cityName)) {
                    Utility.showToast(requireActivity(), "Please enter city name")
                    etCityName.requestFocus()
                } else if (TextUtils.isEmpty(propertyAddress)) {
                    Utility.showToast(requireActivity(), "Please enter property address")
                    etPropertyAddress.requestFocus()
                } else if (TextUtils.isEmpty(purchaseDate)) {
                    Utility.showToast(requireActivity(), "Please select purchase date")
                } else if (TextUtils.isEmpty(price)) {
                    Utility.showToast(requireActivity(), "Please enter price")
                    etPrice.requestFocus()
                } else if (spStatus.selectedItemPosition == 0) {
                    Utility.showToast(requireActivity(), "Please select status")
//                } else if (selectedImageList.size == 1 && txtAdd.text.toString().toLowerCase() == "add") {
//                    Utility.showToast(requireActivity(), "Please add image")
                } else {
                    if (txtAdd.text == "Update") {
                        callUpdateAPI(
                            "${selectedItem?.id}",
                            "$propertyType",
                            "$ownershipStatus",
                            "",
                            "",
                            "",
                            "",
                            "$price",
                            "",
                            "$status",
                            "$locationName",
                            "$cityName",
                            "$propertyAddress",
                            "$purchaseDate",
                            ""
                        )
                    } else {
                        callAddAPI(
                            "$propertyType",
                            "$ownershipStatus",
                            "",
                            "",
                            "",
                            "",
                            "$price",
                            "",
                            "$status",
                            "$locationName",
                            "$cityName",
                            "$propertyAddress",
                            "$purchaseDate",
                            ""
                        )
                    }
                }
            } else if (spOwnershipStatus.selectedItem.toString() == "Rent") {
                /** Common */
                val propertyType: String = propertyType.get(spPropertyType.selectedItemPosition).id.toString()
                val ownershipStatus: String = spOwnershipStatus.selectedItem.toString()
                val status: String = spStatus.selectedItemPosition.toString()

                /** Rented */
                val tenantName: String = etTenantName.text.toString()
                val tenantPermanentAddress: String = etTenantPermanentAddress.text.toString()
                val rentedPropertyAddress: String = etRentedPropertyAddress.text.toString()
                val tenantNumber: String = etTenantNumber.text.toString()
                val agreeStartDate: String = txtAgreementStartDate.text.toString()
                val agreeEndDate: String = txtAgreementEndDate.text.toString()
                val rentAmount: String = etRentAmount.text.toString()
                val rentCollectionDate: String = txtRentCollectionDate.text.toString()
                if (TextUtils.isEmpty(tenantName)) {
                    Utility.showToast(requireActivity(), "Please enter tenant name")
                } else if (TextUtils.isEmpty(tenantPermanentAddress)) {
                    Utility.showToast(requireActivity(), "Please enter tenant permanent address")
                } else if (TextUtils.isEmpty(rentedPropertyAddress)) {
                    Utility.showToast(requireActivity(), "Please enter rented property address")
                } else if (TextUtils.isEmpty(tenantNumber)) {
                    Utility.showToast(requireActivity(), "Please select mobile number")
                } else if (TextUtils.isEmpty(agreeStartDate)) {
                    Utility.showToast(requireActivity(), "Please select agreement start date")
                } else if (TextUtils.isEmpty(agreeEndDate)) {
                    Utility.showToast(requireActivity(), "Please select agreement end date")
                } else if (TextUtils.isEmpty(rentAmount)) {
                    Utility.showToast(requireActivity(), "Please enter rent amount")
                } else if (TextUtils.isEmpty(rentCollectionDate)) {
                    Utility.showToast(requireActivity(), "Please select rent collection date")
                } else if (spStatus.selectedItemPosition == 0) {
                    Utility.showToast(requireActivity(), "Please select status")
//                } else if (selectedImageList.size == 1 && txtAdd.text.toString().toLowerCase() == "add") {
//                    Utility.showToast(requireActivity(), "Please add image")
                } else {
                    if (txtAdd.text == "Update") {
                        callUpdateAPI(
                            "${selectedItem?.id}",
                            "$propertyType",
                            "$ownershipStatus",
                            "$tenantName",
                            "$tenantNumber",
                            "$agreeStartDate",
                            "$agreeEndDate",
                            "$rentAmount",
                            "$rentCollectionDate",
                            "$status",
                            "",
                            "",
                            "",
                            "",
                            "$tenantPermanentAddress"
                        )
                    } else {
                        callAddAPI(
                            "$propertyType",
                            "$ownershipStatus",
                            "$tenantName",
                            "$tenantNumber",
                            "$agreeStartDate",
                            "$agreeEndDate",
                            "$rentAmount",
                            "$rentCollectionDate",
                            "$status",
                            "",
                            "",
                            "",
                            "",
                            "$tenantPermanentAddress"
                        )
                    }
                }
            } else {
                Utility.showToast(requireActivity(), "Something went wrong please try again")
            }
        }
        txtCancel.setOnClickListener {
            requireActivity().onBackPressed()
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
        val imagesFolder: File = File(requireActivity().getExternalFilesDir(null)!!.absolutePath)

        Log.e("Folder Path", imagesFolder.toString())

        try {
            if (!imagesFolder.exists()) {
                Log.e("Creating Folder: ", imagesFolder.toString())
                imagesFolder.mkdirs()
            }

            Log.e("Folder Exist: ", imagesFolder.exists().toString())

            val file = File(imagesFolder, Date().time.toString() + ".jpg")

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
                    Glide.with(requireActivity()).load(selectedImage).into(ivSelectedImage)
                    Log.e("from camera:", selectedImage.toString())
                    selectedImageList.add(selectedImage)
                    rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
                }
            }
            GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = data?.data!!
                    Glide.with(requireActivity()).load(selectedImage).into(ivSelectedImage)
                    Log.e("from gallery:", selectedImage.toString())
                    selectedImageList.add(selectedImage)
                    rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
                }
            }
        }

    }

    //API call
    private fun getPropertyTypesList() {
        ApiCallMethods(requireActivity()).getPropertyTypesList(object
            : OnApiCallCompleted<GetPropertyTypeListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetPropertyTypeListResponse = obj as GetPropertyTypeListResponse

                if (response.isSuccess) {
                    val listPropertyType: MutableList<String> = ArrayList()
                    propertyType = response.list
                    for (i in propertyType.indices) {
                        if (!TextUtils.isEmpty(response.list[i].name))
                            listPropertyType.add(response.list[i].name.toString())
                    }
                    spPropertyTypeAdapter = ArrayAdapter(
                        requireActivity(),
                        R.layout.spinner_item_txt, listPropertyType
                    )
                    spPropertyTypeAdapter?.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                    spPropertyType.adapter = spPropertyTypeAdapter
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

    private fun callAddAPI(
        propertyType: String,
        ownershipStatus: String,
        tenantName: String,
        tenantNumber: String,
        agreeStartDate: String,
        agreeEndDate: String,
        rentAmount: String,
        rentCollectionDate: String,
        status: String,
        location: String,
        city_name: String,
        property_address: String,
        purchase_date: String,
        tenant_permenent_address: String
    ) {
        ApiCallMethods(requireActivity())
            .addProperty(
                "$propertyType",
                "$ownershipStatus",
                "$tenantName",
                "$tenantNumber",
                "$agreeStartDate",
                "$agreeEndDate",
                "$rentAmount",
                "$rentCollectionDate",
                "$status",
                "$location",
                "$city_name",
                "$property_address",
                "$purchase_date",
                "$tenant_permenent_address",
                selectedImageList,
                object : OnApiCallCompleted<BaseResponse> {
                    override fun apiSuccess(obj: Any?) {
                        val response: BaseResponse = obj as BaseResponse
                        Utility.showToast(requireActivity(), response.message)
                        if (response.isSuccess) {
                            requireActivity().onBackPressed()
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

    private fun callUpdateAPI(
        id: String,
        propertyType: String,
        ownershipStatus: String,
        tenantName: String,
        tenantNumber: String,
        agreeStartDate: String,
        agreeEndDate: String,
        rentAmount: String,
        rentCollectionDate: String,
        status: String,
        location: String,
        city_name: String,
        property_address: String,
        purchase_date: String,
        tenant_permenent_address: String
    ) {
        ApiCallMethods(requireActivity())
            .updateProperty(
                "$id",
                "$propertyType",
                "$ownershipStatus",
                "$tenantName",
                "$tenantNumber",
                "$agreeStartDate",
                "$agreeEndDate",
                "$rentAmount",
                "$rentCollectionDate",
                "$status",
                "$location",
                "$city_name",
                "$property_address",
                "$purchase_date",
                "$tenant_permenent_address",
                selectedImageList,
                object : OnApiCallCompleted<BaseResponse> {
                    override fun apiSuccess(obj: Any?) {
                        val response: BaseResponse = obj as BaseResponse
                        Utility.showToast(requireActivity(), response.message)
                        if (response.isSuccess) {
                            requireActivity().onBackPressed()
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
