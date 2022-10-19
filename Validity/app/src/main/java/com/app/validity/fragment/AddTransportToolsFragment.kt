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
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.ClsAddFitnessCertificateResponse
import com.app.validity.model.TransportToolsItem
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
import kotlinx.android.synthetic.main.fragment_add_transport_tool.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddTransportToolsFragment(var vehicalId: String, var pos: Int, val vehicalItem: TransportToolsItem?) : BaseFragment(), ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_add_transport_tool

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)

        if (vehicalItem != null) {
            txtIssueDate.text = if (TextUtils.isEmpty(vehicalItem.startDate)) "" else vehicalItem.startDate
            txtExpiryDate.text = if (TextUtils.isEmpty(vehicalItem.endDate)) "" else vehicalItem.endDate
            if (TextUtils.isEmpty(vehicalItem.permitType)) etPermitType.setText("") else etPermitType.setText(vehicalItem.permitType)
        }

        if (pos == 3) {
            etPermitType.visibility = View.VISIBLE
        }
    }

    override fun setListeners() {

        txtIssueDate.setOnClickListener {
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
                    txtIssueDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtExpiryDate.setOnClickListener {
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
                    txtExpiryDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtAdd.setOnClickListener {
            if (TextUtils.isEmpty(txtIssueDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select start date.")
            } else if (TextUtils.isEmpty(txtExpiryDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select end date.")
            } else if (pos == 3 && TextUtils.isEmpty(etPermitType.text.toString().trim())) {
                Utility.showToast(requireActivity(), "Please enter permit type.")
//            } else if (selectedImageList.size == 1 && txtAdd.text.toString().toLowerCase() == "add") {
//                Utility.showToast(requireActivity(), "Please add image.")
            } else {
                when (pos) {
                    1 -> callAddFitnessCertificateAPI()
                    2 -> callAddRoadTaxAPI()
                    3 -> callAddPermitAPI()
                    4 -> callAddSpeedGovernerAPI()
                    else -> Utility.showToast(requireActivity(), "Something went wrong.")
                }
            }
        }

        txtCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        txtSelectImage.setOnClickListener { checkImageSelectionPermission() }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (vehicalItem == null) {
            when (pos) {
                1 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_fitness_add, false)
                2 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_road_tax_add, false)
                3 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_permit_add, false)
                else -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_speed_governor_add, false)
            }
        } else {
            txtAdd.text = "Update"
            when (pos) {
                1 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_fitness_edit, false)
                2 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_road_tax_edit, false)
                3 -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_permit_edit, false)
                else -> (requireActivity() as DashboardActivity).setHeader(R.string.transport_tool_speed_governor_edit, false)
            }
        }
    }

    private fun callAddFitnessCertificateAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addFitnessCertificate(
            vehicalId,
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            selectedImageList,
            object : OnApiCallCompleted<ClsAddFitnessCertificateResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: ClsAddFitnessCertificateResponse = obj as ClsAddFitnessCertificateResponse

                    if (baseResponse.status != null && baseResponse.status!!) {
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

    private fun callAddRoadTaxAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addRoadTax(
            vehicalId,
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            selectedImageList,
            object : OnApiCallCompleted<ClsAddFitnessCertificateResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: ClsAddFitnessCertificateResponse = obj as ClsAddFitnessCertificateResponse

                    if (baseResponse.status != null && baseResponse.status!!) {
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

    private fun callAddPermitAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addPermit(
            vehicalId,
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            etPermitType.text.toString().trim(),
            selectedImageList,
            object : OnApiCallCompleted<ClsAddFitnessCertificateResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: ClsAddFitnessCertificateResponse = obj as ClsAddFitnessCertificateResponse

                    if (baseResponse.status != null && baseResponse.status!!) {
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

    private fun callAddSpeedGovernerAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addSpeedGoverner(
            vehicalId,
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            selectedImageList,
            object : OnApiCallCompleted<ClsAddFitnessCertificateResponse> {
                override fun apiSuccess(obj: Any?) {
                    val baseResponse: ClsAddFitnessCertificateResponse = obj as ClsAddFitnessCertificateResponse

                    if (baseResponse.status != null && baseResponse.status!!) {
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
