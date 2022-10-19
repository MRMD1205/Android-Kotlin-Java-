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
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.VehicalInsurance
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
import kotlinx.android.synthetic.main.fragment_add_insurance.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddInsuranceFragment(
    var vehicalId: String,
    var vehicalItem: VehicalInsurance?,
    val listPaymentType: MutableList<String>,
    val listPolicyType: MutableList<String>
) : BaseFragment(), ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()

    private lateinit var spPolicyTypeArrayAdapter: ArrayAdapter<String>
    private lateinit var spPaymentModeArrayAdapter: ArrayAdapter<String>

    override fun setContentView(): Int = R.layout.fragment_add_insurance

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)

//        val list: MutableList<String> = ArrayList()
//        list.add("Body")
//        list.add("Body")
        spPolicyTypeArrayAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listPolicyType)
        spPolicyTypeArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spPolicyType.adapter = spPolicyTypeArrayAdapter

//        val list1: MutableList<String> = ArrayList()
//        list1.add("Online")
//        list1.add("Card")
        spPaymentModeArrayAdapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listPaymentType)
        spPaymentModeArrayAdapter.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
        spPaymentMode.adapter = spPaymentModeArrayAdapter

        if (vehicalItem != null) {
            txtAdd.text = "Update"
            etCompany.setText(if (TextUtils.isEmpty(vehicalItem!!.companyName)) "" else vehicalItem!!.companyName)
            etPolicyNo.setText(if (TextUtils.isEmpty(vehicalItem!!.policyNumber)) "" else vehicalItem!!.policyNumber)
            txtIssueDate.setText(if (TextUtils.isEmpty(vehicalItem!!.issueDate)) "" else vehicalItem!!.issueDate)
            txtExpiryDate.setText(if (TextUtils.isEmpty(vehicalItem!!.expiryDate)) "" else vehicalItem!!.expiryDate)
            etInsuranceAmount.setText(if (TextUtils.isEmpty(vehicalItem!!.amount)) "" else vehicalItem!!.amount)
            etPremium.setText(if (TextUtils.isEmpty(vehicalItem!!.premium)) "" else vehicalItem!!.premium)
            etAgentName.setText(if (TextUtils.isEmpty(vehicalItem!!.agentName)) "" else vehicalItem!!.agentName)
            etAgentContactNumber.setText(if (TextUtils.isEmpty(vehicalItem!!.agentContactNo)) "" else vehicalItem!!.agentContactNo)
            etDescription.setText(if (TextUtils.isEmpty(vehicalItem!!.description)) "" else vehicalItem!!.description)
            if (!TextUtils.isEmpty(vehicalItem!!.policyType)) {
                for (i in listPolicyType.indices) {
                    if (listPolicyType[i] == vehicalItem!!.policyType) {
                        spPolicyType.setSelection(i)
                        break
                    }
                }
            }
            if (!TextUtils.isEmpty(vehicalItem!!.paymentMode)) {
                for (i in listPaymentType.indices) {
                    if (listPaymentType[i] == vehicalItem!!.paymentMode) {
                        spPaymentMode.setSelection(i)
                        break
                    }
                }
            }
        }
    }

    override fun setListeners() {

        txtSelectImage.setOnClickListener { checkImageSelectionPermission() }

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
                    txtIssueDate.text =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
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
                    txtExpiryDate.text =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtAdd.setOnClickListener {
            if (TextUtils.isEmpty(etCompany.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter company name")
            } else if (TextUtils.isEmpty(etPolicyNo.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter policy number")
            } else if (TextUtils.isEmpty(txtIssueDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select issue date")
            } else if (TextUtils.isEmpty(txtExpiryDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select expiry date")
            } else if (TextUtils.isEmpty(etInsuranceAmount.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter insurance amount")
            } else if (TextUtils.isEmpty(etPremium.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter premium")
            } else if (TextUtils.isEmpty(etAgentName.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter agent name")
            } else if (TextUtils.isEmpty(etAgentContactNumber.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter agent contact number.")
            } else if (TextUtils.isEmpty(etDescription.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter description.")
            } else {
                if (txtAdd.text == "Update") {
                    updateVehicleInsurances()
                } else {
                    addVehicleInsurances()
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
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_insurance, false)
        } else {
            (requireActivity() as DashboardActivity).setHeader(
                R.string.menu_update_insurance,
                false
            )
        }
    }

    // API Call
    private fun addVehicleInsurances() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addVehicleInsurances(
            vehicalId,
            etCompany.text.toString(),
            spPolicyType.selectedItem.toString(),
            etPolicyNo.text.toString(),
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            spPaymentMode.selectedItem.toString(),
            etInsuranceAmount.text.toString(),
            etPremium.text.toString(),
            etAgentName.text.toString(),
            etAgentContactNumber.text.toString(),
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

    private fun updateVehicleInsurances() {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.updateVehicleInsurances(
            vehicalId,
            vehicalItem!!.id.toString(),
            etCompany.text.toString(),
            spPolicyType.selectedItem.toString(),
            etPolicyNo.text.toString(),
            txtIssueDate.text.toString(),
            txtExpiryDate.text.toString(),
            spPaymentMode.selectedItem.toString(),
            etInsuranceAmount.text.toString(),
            etPremium.text.toString(),
            etAgentName.text.toString(),
            etAgentContactNumber.text.toString(),
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
