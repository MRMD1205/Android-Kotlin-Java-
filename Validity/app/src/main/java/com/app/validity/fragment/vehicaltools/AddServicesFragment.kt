package com.app.validity.fragment.vehicaltools


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
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.adapter.ServiceTypeListAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.ClsServiceType
import com.app.validity.model.VehicalServiceItem
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
import kotlinx.android.synthetic.main.fragment_add_services.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddServicesFragment(var vehicalId: String, var vehicalItem: VehicalServiceItem?, var listServiceType: MutableList<String>) : BaseFragment(), ItemClickCallback<Uri?> {

    val clsServiceTypeList: ArrayList<ClsServiceType> = ArrayList()
    lateinit var serviceTypeListAdapter: ServiceTypeListAdapter
    private var selectedImage: Uri? = null
    private var selectedImageList: java.util.ArrayList<Uri?> = java.util.ArrayList()

    override fun setContentView(): Int = R.layout.fragment_add_services

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
        txtSelectImage.setOnClickListener { checkImageSelectionPermission() }

        if (vehicalItem != null) {
            txtAdd.text = "Update"
            txtServiceDate.setText(if (TextUtils.isEmpty(vehicalItem!!.date)) "" else vehicalItem!!.date)
            etGarage.setText(if (TextUtils.isEmpty(vehicalItem!!.garage)) "" else vehicalItem!!.garage)
            etContactNo.setText(if (TextUtils.isEmpty(vehicalItem!!.contactNo)) "" else vehicalItem!!.contactNo)
            etTotalAmount.setText(if (TextUtils.isEmpty(vehicalItem!!.amount)) "" else vehicalItem!!.amount)
            etKmReading.setText(if (TextUtils.isEmpty(vehicalItem!!.kmReading)) "" else vehicalItem!!.kmReading)
            etDescription.setText(if (TextUtils.isEmpty(vehicalItem!!.description)) "" else vehicalItem!!.description)

            if (!TextUtils.isEmpty(vehicalItem!!.serviceType)) {
                vehicalItem!!.serviceType = vehicalItem!!.serviceType!!.replace("[", "")
                vehicalItem!!.serviceType = vehicalItem!!.serviceType!!.replace("]", "")
                vehicalItem!!.serviceType = vehicalItem!!.serviceType!!.replace("\"", "")
                val stList: List<String> = vehicalItem!!.serviceType!!.split(",")
                //
                //                val s_chkBody = chkBody.text.toString()
                //                val s_chkBreak = chkBreak.text.toString()
                //                val s_chkCluth = chkCluth.text.toString()
                //                val s_chkCoolingSystem = chkCoolingSystem.text.toString()
                //                val s_chkEngine = chkEngine.text.toString()
                //                val s_chkSparkPlug = chkSparkPlug.text.toString()
                //                val s_chkGeneral = chkGeneral.text.toString()
                //                val s_chkOther = chkOther.text.toString()
                //                val s_chkOilChange = chkOilChange.text.toString()
                //                val s_chkBattery = chkBattery.text.toString()
                //
                //                for (i in stList.indices) {
                //                    if (stList[i] == s_chkBody) chkBody.isChecked = true
                //                    if (stList[i] == s_chkBreak) chkBreak.isChecked = true
                //                    if (stList[i] == s_chkCluth) chkCluth.isChecked = true
                //                    if (stList[i] == s_chkCoolingSystem) chkCoolingSystem.isChecked = true
                //                    if (stList[i] == s_chkEngine) chkEngine.isChecked = true
                //                    if (stList[i] == s_chkSparkPlug) chkSparkPlug.isChecked = true
                //                    if (stList[i] == s_chkGeneral) chkGeneral.isChecked = true
                //                    if (stList[i] == s_chkOther) chkOther.isChecked = true
                //                    if (stList[i] == s_chkOilChange) chkOilChange.isChecked = true
                //                    if (stList[i] == s_chkBattery) chkBattery.isChecked = true
                //                }

                for (i in 0 until listServiceType.size) {
                    var isCheck: Boolean = false
                    for (j in stList.indices) {
                        if (listServiceType[i] == stList[j]) {
                            isCheck = true
                            break
                        }
                    }
                    clsServiceTypeList.add(ClsServiceType(isCheck, listServiceType[i]))
                }

                serviceTypeListAdapter = ServiceTypeListAdapter(clsServiceTypeList, null)
                rvServicesType.adapter = serviceTypeListAdapter
            }
        } else {

            for (i in 0 until listServiceType.size) {
                clsServiceTypeList.add(ClsServiceType(false, listServiceType[i]))
            }

            serviceTypeListAdapter = ServiceTypeListAdapter(clsServiceTypeList, null)
            rvServicesType.adapter = serviceTypeListAdapter
        }
    }

    override fun setListeners() {

        txtServiceDate.setOnClickListener {
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
                    txtServiceDate.text =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtAdd.setOnClickListener {
            var serviceType: String = ""
            if (::serviceTypeListAdapter.isInitialized) {

                for (i in 0 until serviceTypeListAdapter.getServiceItemList().size) {
                    if (serviceTypeListAdapter.getServiceItemList()[i].isChecked) {
                        if (TextUtils.isEmpty(serviceType))
                            serviceType = serviceTypeListAdapter.getServiceItemList()[i].serviceType.toString()
                        else
                            serviceType = serviceType + "," + serviceTypeListAdapter.getServiceItemList()[i].serviceType
                    }
                }
            }

            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkBreak)) "$serviceType,$s_chkBreak" else serviceType
            //            else s_chkBreak
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkCluth)) "$serviceType,$s_chkCluth" else serviceType
            //            else s_chkCluth
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkCoolingSystem)) "$serviceType,$s_chkCoolingSystem" else serviceType
            //            else s_chkCoolingSystem
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkEngine)) "$serviceType,$s_chkEngine" else serviceType
            //            else s_chkEngine
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkSparkPlug)) "$serviceType,$s_chkSparkPlug" else serviceType
            //            else s_chkSparkPlug
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkGeneral)) "$serviceType,$s_chkGeneral" else serviceType
            //            else s_chkGeneral
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkOther)) "$serviceType,$s_chkOther" else serviceType
            //            else s_chkOther
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkOilChange)) "$serviceType,$s_chkOilChange" else serviceType
            //            else s_chkOilChange
            //
            //            serviceType = if (!TextUtils.isEmpty(serviceType.trim()))
            //                if (!TextUtils.isEmpty(s_chkBattery)) "$serviceType,$s_chkBattery" else serviceType
            //            else s_chkBattery

            if (TextUtils.isEmpty(txtServiceDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select date.")
            } else if (TextUtils.isEmpty(serviceType)) {
                Utility.showToast(requireActivity(), "Please select atleast 1 service type.")
            } else if (TextUtils.isEmpty(etGarage.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter garage name.")
            } else if (TextUtils.isEmpty(etContactNo.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter contact no.")
            } else if (TextUtils.isEmpty(etTotalAmount.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter amount.")
            } else if (TextUtils.isEmpty(etKmReading.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter KM reading.")
            } else if (TextUtils.isEmpty(etDescription.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter description.")
            } else {
                Log.e("WW", serviceType)
                if (txtAdd.text == "Update") {
                    updateVehicleServices("[$serviceType]")
                } else {
                    addVehicleServices("[$serviceType]")
                }
            }
        }

        txtCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (vehicalItem == null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_services, false)
        } else {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_services, false)
        }
    }

    // API Call
    private fun addVehicleServices(serviceType: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addVehicleServices(
            vehicalId,
            serviceType,
            txtServiceDate.text.toString(),
            etGarage.text.toString(),
            etContactNo.text.toString(),
            etTotalAmount.text.toString(),
            etKmReading.text.toString(),
            etDescription.text.toString(),
            selectedImageList,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: BaseResponse = obj as BaseResponse
                    if (baseResponse.isSuccess) {
                        Utility.showToast(requireActivity(), baseResponse.message)
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

    private fun updateVehicleServices(serviceType: String) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.updateVehicleServices(
            vehicalId,
            vehicalItem!!.id.toString(),
            serviceType,
            txtServiceDate.text.toString(),
            etGarage.text.toString(),
            etContactNo.text.toString(),
            etTotalAmount.text.toString(),
            etKmReading.text.toString(),
            etDescription.text.toString(),
            selectedImageList,
            object : OnApiCallCompleted<BaseResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: BaseResponse = obj as BaseResponse
                    if (baseResponse.isSuccess) {
                        Utility.showToast(requireActivity(), baseResponse.message)
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
}
