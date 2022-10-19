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
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.*
import com.app.validity.util.*
import com.bumptech.glide.Glide
import com.app.validity.base.BaseFragment
import com.app.validity.network.ApiCallMethods
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_add_vehical.*
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddVehicalFragment(val listVehicleCondition: MutableList<String>, val listFuelType: MutableList<String>) : BaseFragment(), ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()
    private lateinit var spVehicleConditionArrayAdapter: ArrayAdapter<String>
    private lateinit var spVehicleTypeArrayAdapter: ArrayAdapter<String>
    private lateinit var spVehicleBrandArrayAdapter: ArrayAdapter<String>
    private lateinit var spFuelTypeArrayAdapter: ArrayAdapter<String>
    private var vehicalID = ""
    private var vehicalTypeID = ""
    private var vehicalBrandID = ""

    override fun setContentView(): Int = R.layout.fragment_add_vehical

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
        callGetVehicleTypeList()
//        val listVehicleCondition: MutableList<String> = ArrayList()
//        listVehicleCondition.add("Old")
//        listVehicleCondition.add("New")
        spVehicleConditionArrayAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listVehicleCondition)
        spVehicleConditionArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spVehicleCondition.adapter = spVehicleConditionArrayAdapter

//        val listFuelType: MutableList<String> = ArrayList()
//        listFuelType.add("Petrol")
//        listFuelType.add("Diesel")
//        listFuelType.add("CNG")
//        listFuelType.add("PNG")
        spFuelTypeArrayAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listFuelType)
        spFuelTypeArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spFuelType.adapter = spFuelTypeArrayAdapter
        if (arguments != null && arguments!!.getParcelable<VehicleItem>("VehicleItem") != null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_vehicles, false)
            txtAdd.text = "Update"
            var item = VehicleItem()
            item = arguments!!.getParcelable<VehicleItem>("VehicleItem") as VehicleItem
            vehicalID = item.id.toString()
            etModelName.setText(item.name.toString())
            etOwnerName.setText(item.ownerName.toString())
            etBuildYear.setText(item.buildYear.toString())
            etRegistrationNumber.setText(item.registrationNumber.toString())
            etTankCapacity.setText(item.tankCapicity.toString())
            etPurchasePrice.setText(item.purchasePrice.toString())
            txtPurchaseDate.setText(item.purchaseDate.toString())
            etKmReading.setText(item.kmReading.toString())

            for (i in listVehicleCondition.indices) {
                if (listVehicleCondition[i].toLowerCase() == item.vehicleCondition!!.toLowerCase()) {
                    spVehicleCondition.setSelection(i)
                }
            }

            for (k in listFuelType.indices) {
                if (listFuelType[k].toLowerCase() == item.fuelType!!.toLowerCase()) {
                    spFuelType.setSelection(k)
                }
            }
        }

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

    override fun setListeners() {
        txtCancel.setOnClickListener {
            requireActivity().onBackPressed()

        }

        txtPurchaseDate.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val mYear = myCalender.get(Calendar.YEAR)
            val mMonth = myCalender.get(Calendar.MONTH) + 1
            val mDay = myCalender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val monthInt = month + 1

                    val strDayOfMonth = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val strMonthOfYear = if (monthInt < 10) "0${monthInt}" else "$monthInt"
                    val strYear = if (year < 10) "0${year}" else "$year"

                    txtPurchaseDate.text = "${strYear}-$strMonthOfYear-${strDayOfMonth}"
                    myCalender.set(
                        strYear.toInt(),
                        strMonthOfYear.toInt() - 1,
                        strDayOfMonth.toInt()
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtSelectImage.setOnClickListener {
            checkImageSelectionPermission()
        }

        txtAdd.setOnClickListener {
            if (TextUtils.isEmpty(etModelName.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter model name")
            } else if (TextUtils.isEmpty(etOwnerName.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter owner name")
            } else if (TextUtils.isEmpty(etBuildYear.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter build year")
            } else if (TextUtils.isEmpty(etRegistrationNumber.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter registration number")
            } else if (TextUtils.isEmpty(etTankCapacity.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter tank capacity")
            } else if (TextUtils.isEmpty(txtPurchaseDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select purchase date")
            } else if (TextUtils.isEmpty(etPurchasePrice.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter purchase price")
            } else if (TextUtils.isEmpty(etKmReading.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter KM reading")
//            } else if (txtAdd.text != "Update" && selectedImage == null) {
//                Utility.showToast(requireActivity(), "Please select image")
            } else {
                if (txtAdd.text == "Update") {
                    updateVehical(
                        "$vehicalID",
                        "$vehicalTypeID",
                        "$vehicalBrandID",
                        "${etModelName.text}",
                        "${etOwnerName.text}",
                        "${spVehicleCondition.selectedItem}",
                        "${etBuildYear.text}",
                        "${etRegistrationNumber.text}",
                        "${spFuelType.selectedItem}",
                        "${etTankCapacity.text}",
                        "${txtPurchaseDate.text}",
                        "${etPurchasePrice.text}",
                        "${etKmReading.text}"
                    )
                } else {
                    addVehical(
                        "$vehicalTypeID",
                        "$vehicalBrandID",
                        "${etModelName.text}",
                        "${etOwnerName.text}",
                        "${spVehicleCondition.selectedItem}",
                        "${etBuildYear.text}",
                        "${etRegistrationNumber.text}",
                        "${spFuelType.selectedItem}",
                        "${etTankCapacity.text}",
                        "${txtPurchaseDate.text}",
                        "${etPurchasePrice.text}",
                        "${etKmReading.text}",
                        selectedImage
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

    override fun onResume() {
        super.onResume()
        if (txtAdd.text.toString().toLowerCase() == "update") {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_vehicles, false)
        } else {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_vehicles, false)
        }
    }

    // API call
    private fun addVehical(
        vehicle_type_id: String,
        vehicle_brand_id: String,
        name: String,
        owner_name: String,
        vehicle_condition: String,
        build_year: String,
        registration_number: String,
        fuel_type: String,
        tank_capicity: String,
        purchase_date: String,
        purchase_price: String,
        km_reading: String,
        file: Uri?
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addVehical(
            vehicle_type_id,
            vehicle_brand_id,
            name,
            owner_name,
            vehicle_condition,
            build_year,
            registration_number,
            fuel_type,
            tank_capicity,
            purchase_date,
            purchase_price,
            km_reading,
            selectedImageList
            , object : OnApiCallCompleted<BaseResponse> {
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
        vehicle_id: String,
        vehicle_type_id: String,
        vehicle_brand_id: String,
        name: String,
        owner_name: String,
        vehicle_condition: String,
        build_year: String,
        registration_number: String,
        fuel_type: String,
        tank_capicity: String,
        purchase_date: String,
        purchase_price: String,
        km_reading: String
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.updateVehical(
            vehicle_id,
            vehicle_type_id,
            vehicle_brand_id,
            name,
            owner_name,
            vehicle_condition,
            build_year,
            registration_number,
            fuel_type,
            tank_capicity,
            purchase_date,
            purchase_price,
            km_reading,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: BaseResponse = obj as BaseResponse
                    if (baseResponse.isSuccess) {
                        Utility.showToast(requireActivity(), "Vehicle updates successfully")
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


    //API call
    private fun callGetVehicleTypeList() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.getVehicleTypeList(object : OnApiCallCompleted<GetVehicleTypeListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetVehicleTypeListResponse = obj as GetVehicleTypeListResponse
                if (response.isSuccess) {

                    val listVehicleType: MutableList<String> = ArrayList()
                    for (i in response.list.indices) {
                        if (!TextUtils.isEmpty(response.list[i].name))
                            listVehicleType.add(response.list[i].name.toString())
                    }
                    spVehicleTypeArrayAdapter =
                        ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listVehicleType)
                    spVehicleTypeArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                    spVehicleType.adapter = spVehicleTypeArrayAdapter

                    spVehicleType.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val listVehicleBrand: MutableList<String> = ArrayList()
                                val vehicleType = response.list[spVehicleType.selectedItemPosition]
                                vehicalTypeID = vehicleType.id.toString()
                                for (i in vehicleType.brandList.indices) {
                                    if (!TextUtils.isEmpty(vehicleType.brandList[i].name))
                                        listVehicleBrand.add(vehicleType.brandList[i].name.toString())
                                }

                                spVehicleBrandArrayAdapter = ArrayAdapter(
                                    requireActivity(),
                                    R.layout.spinner_item_txt,
                                    listVehicleBrand
                                )
                                spVehicleBrandArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                                spVehicleBrand.adapter = spVehicleBrandArrayAdapter
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }

                    spVehicleBrand.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val vehicleType = response.list[spVehicleType.selectedItemPosition]
                                vehicalBrandID =
                                    vehicleType.brandList[spVehicleBrand.selectedItemPosition].id.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                } else {
                    Utility.showToast(requireActivity(), response.message)
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
