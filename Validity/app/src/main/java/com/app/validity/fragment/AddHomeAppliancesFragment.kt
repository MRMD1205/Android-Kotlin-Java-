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
import com.app.validity.model.GetHomeAppliancesListResponse
import com.app.validity.model.GetHomeAppliancesTypeListResponse
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
import kotlinx.android.synthetic.main.fragment_add_home_appliances.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddHomeAppliancesFragment(val selectedItem: GetHomeAppliancesListResponse.Datum?) : BaseFragment()
    , ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()
    var homeAppliancesType: MutableList<GetHomeAppliancesTypeListResponse.Datum> = ArrayList()
    var spHomeAppliancesTypeAdapter: ArrayAdapter<String>? = null

    private var homeAppliancesTypeID = ""
    private var homeAppliancesBrandID = ""
    var spHomeApplianceBrandArrayAdapter: ArrayAdapter<String>? = null

    override fun setContentView(): Int = R.layout.fragment_add_home_appliances

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        getHomeAppliancesTypesList()

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
        //Todo Status
        val spStatusAdapter: ArrayAdapter<String>
        val listStatus: MutableList<String> = ArrayList()
        listStatus.add("Select")
        listStatus.add("Active")
        listStatus.add("Inactive")
        spStatusAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listStatus)
        spStatusAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spStatus.adapter = spStatusAdapter

        if (selectedItem != null) {
            etName.setText(selectedItem.name)
            etPurchaseFrom.setText(selectedItem.purchaseFrom)
            etAmount.setText(selectedItem.amount.toString())
            etPurchaseName.setText(selectedItem.purchaserName)
            etPurchaseAddress.setText(selectedItem.purchaseFromAddress)
            txtPurchaseDate.setText(selectedItem.purchaseDate)
            etWarrentyPeriod.setText(selectedItem.warrentyPeriod)
            txtWarrentyExpireDate.setText(selectedItem.warrentyExpiredDate)
            etDescription.setText(selectedItem.description)

            for (i in homeAppliancesType.indices) {
                if (selectedItem.homeApplianceType?.name == homeAppliancesType[i].name)
                    spHomeApplianceType.setSelection(selectedItem.homeApplianceType?.id!!)
            }

            for (i in listStatus.indices) {
                if (i == selectedItem.status) {
                    spStatus.setSelection(i)
                }
            }
        }
    }

    override fun setListeners() {
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
        txtWarrentyExpireDate.setOnClickListener {
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
                    txtWarrentyExpireDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        txtSelectImage.setOnClickListener {
            checkImageSelectionPermission()
        }

        txtAdd.setOnClickListener {

            val etName: String = etName.text.toString()
            val etPurchaseFrom: String = etPurchaseFrom.text.toString()
            val etAmount: String = etAmount.text.toString()
            val etPurchaseName: String = etPurchaseName.text.toString()
            val etPurchaseAddress: String = etPurchaseAddress.text.toString()
            val txtPurchaseDate: String = txtPurchaseDate.text.toString()
            val etWarrentyPeriod: String = etWarrentyPeriod.text.toString()
            val txtWarrentyExpireDate: String = txtWarrentyExpireDate.text.toString()
            val etDescription: String = etDescription.text.toString()
            val status: String = spStatus.selectedItemPosition.toString()

            when {
                //                spPersonalDocumentType.selectedItemPosition == 0 -> Utility.showToast(requireActivity(), "Please select property type")
                TextUtils.isEmpty(homeAppliancesTypeID) -> Utility.showToast(requireActivity(), "Please select home appliances Type")
                TextUtils.isEmpty(homeAppliancesBrandID) -> Utility.showToast(requireActivity(), "Please select home appliances Brand")
                TextUtils.isEmpty(etName) -> Utility.showToast(requireActivity(), "Please enter name")
                TextUtils.isEmpty(etPurchaseFrom) -> Utility.showToast(requireActivity(), "Please enter purchase from")
                TextUtils.isEmpty(etAmount) -> Utility.showToast(requireActivity(), "Please enter amount")
                TextUtils.isEmpty(etPurchaseName) -> Utility.showToast(requireActivity(), "Please enter purchase name")
                TextUtils.isEmpty(etPurchaseAddress) -> Utility.showToast(requireActivity(), "Please enter purchase address")
                TextUtils.isEmpty(txtPurchaseDate) -> Utility.showToast(requireActivity(), "Please select purchase date")
                TextUtils.isEmpty(etWarrentyPeriod) -> Utility.showToast(requireActivity(), "Please enter warrenty period")
                TextUtils.isEmpty(txtWarrentyExpireDate) -> Utility.showToast(requireActivity(), "Please select warrenty expire date")
                TextUtils.isEmpty(etDescription) -> Utility.showToast(requireActivity(), "Please enter description")
                spStatus.selectedItemPosition == 0 -> Utility.showToast(requireActivity(), "Please select status")
//                (txtAdd.text != "Update" && selectedImage == null) -> Utility.showToast(requireActivity(), "Please select image")
                else -> {
                    if (txtAdd.text == "Update") {
                        updateHomeAppliances(
                            selectedItem?.id.toString(),
                            homeAppliancesTypeID,
                            homeAppliancesBrandID,
                            etName,
                            etPurchaseFrom,
                            txtPurchaseDate,
                            etWarrentyPeriod,
                            txtWarrentyExpireDate,
                            etDescription,
                            etAmount,
                            status,
                            etPurchaseName,
                            etPurchaseAddress
                        )
                    } else {
                        addHomeAppliances(
                            homeAppliancesTypeID,
                            homeAppliancesBrandID,
                            etName,
                            etPurchaseFrom,
                            txtPurchaseDate,
                            etWarrentyPeriod,
                            txtWarrentyExpireDate,
                            etDescription,
                            etAmount,
                            status,
                            etPurchaseName,
                            etPurchaseAddress
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedItem == null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_Add_home_appliances, false)
        } else {
            txtAdd.text = "Update"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_Update_home_appliances, false)
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
    private fun getHomeAppliancesTypesList() {
        ApiCallMethods(requireActivity()).getHomeAppliancesTypesList(object
            : OnApiCallCompleted<GetHomeAppliancesTypeListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetHomeAppliancesTypeListResponse = obj as GetHomeAppliancesTypeListResponse

                if (response.isSuccess) {
                    val listPropertyType: MutableList<String> = ArrayList()
                    homeAppliancesType = response.list
                    for (i in homeAppliancesType.indices) {
                        if (!TextUtils.isEmpty(response.list[i].name))
                            listPropertyType.add(response.list[i].name.toString())
                    }
                    spHomeAppliancesTypeAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listPropertyType)
                    spHomeAppliancesTypeAdapter?.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                    spHomeApplianceType.adapter = spHomeAppliancesTypeAdapter

                    spHomeApplianceType.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val listVehicleBrand: MutableList<String> = ArrayList()
                                val vehicleType = response.list[spHomeApplianceType.selectedItemPosition]
                                homeAppliancesTypeID = vehicleType.id.toString()
                                for (i in vehicleType.homeApplianceBrands!!.indices) {
                                    if (!TextUtils.isEmpty(vehicleType.homeApplianceBrands!![i].name))
                                        listVehicleBrand.add(vehicleType.homeApplianceBrands!![i].name.toString())
                                }

                                spHomeApplianceBrandArrayAdapter = ArrayAdapter(
                                    requireActivity(),
                                    R.layout.spinner_item_txt,
                                    listVehicleBrand
                                )
                                spHomeApplianceBrandArrayAdapter!!.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                                spHomeApplianceBrand.adapter = spHomeApplianceBrandArrayAdapter
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }

                    spHomeApplianceBrand.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val vehicleType = response.list[spHomeApplianceType.selectedItemPosition]
                                homeAppliancesBrandID =
                                    vehicleType.homeApplianceBrands!![spHomeApplianceBrand.selectedItemPosition].id.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

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

    private fun addHomeAppliances(
        home_appliance_type_id: String,
        home_appliance_brand_id: String,
        name: String,
        purchase_from: String,
        purchase_date: String,
        warrenty_period: String,
        warrenty_expired_date: String,
        description: String,
        amount: String,
        status: String,
        etPurchaseName: String,
        etPurchaseAddress: String
    ) {
        ApiCallMethods(requireActivity())
            .addHomeAppliances(
                home_appliance_type_id,
                home_appliance_brand_id,
                name,
                purchase_from,
                purchase_date,
                warrenty_period,
                warrenty_expired_date,
                description,
                amount,
                status,
                etPurchaseName,
                etPurchaseAddress,
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

    private fun updateHomeAppliances(
        id: String,
        home_appliance_type_id: String,
        home_appliance_brand_id: String,
        name: String,
        purchase_from: String,
        purchase_date: String,
        warrenty_period: String,
        warrenty_expired_date: String,
        description: String,
        amount: String,
        status: String,
        etPurchaseName: String,
        etPurchaseAddress: String
    ) {
        ApiCallMethods(requireActivity())
            .updateHomeAppliances(
                id,
                home_appliance_type_id,
                home_appliance_brand_id,
                name,
                purchase_from,
                purchase_date,
                warrenty_period,
                warrenty_expired_date,
                description,
                amount,
                status,
                etPurchaseName,
                etPurchaseAddress,
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
