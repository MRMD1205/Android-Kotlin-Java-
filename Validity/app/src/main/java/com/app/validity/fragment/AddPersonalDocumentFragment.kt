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
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.app.validity.BuildConfig
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import com.app.validity.adapter.ImageAdapter
import com.app.validity.base.BaseFragment
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetPersonalDocumentListResponse
import com.app.validity.model.GetPersonalDocumentTypeListResponse
import com.app.validity.model.PersonalDocumentType
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
import kotlinx.android.synthetic.main.fragment_add_personal_document.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPersonalDocumentFragment(val selectedItem: GetPersonalDocumentListResponse.Datum?) : BaseFragment()
    , ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()
    var personalDocumentType: MutableList<PersonalDocumentType> = ArrayList()
    var spPersonalDocumentTypeAdapter: ArrayAdapter<String>? = null

    override fun setContentView(): Int = R.layout.fragment_add_personal_document

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        //Todo PropertyType
        getPersonalDocumentTypesList()

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
            etCompanyName.setText(selectedItem.companyName)
            etSumAssured.setText(selectedItem.sumAssured)
            etName.setText(selectedItem.name)
            txtStartDate.setText(selectedItem.startDate)
            txtEndDate.setText(selectedItem.endDate)
            etAddress.setText(selectedItem.address)
            etFee.setText(selectedItem.fee.toString())
            etDescription.setText(selectedItem.description)

            for (i in personalDocumentType.indices) {
                if (selectedItem.personalDocumentType?.name == personalDocumentType[i].name)
                    spPersonalDocumentType.setSelection(selectedItem.personalDocumentType?.id!!)
            }

            for (i in listStatus.indices) {
                if (i == selectedItem.status) {
                    spStatus.setSelection(i)
                }
            }
        }
        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_personal_documents, false)

        if (selectedItem == null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_personal_documents, false)
        } else {
            txtAdd.text = "Update"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_personal_documents, false)
        }
    }

    override fun setListeners() {

        txtStartDate.setOnClickListener {
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
                    txtStartDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        txtEndDate.setOnClickListener {
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
                    txtEndDate.text = finalDate

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtSelectImage.setOnClickListener {
            checkImageSelectionPermission()
        }

        txtAdd.setOnClickListener {

            val propertyType: String = personalDocumentType.get(spPersonalDocumentType.selectedItemPosition).id.toString()
            val companyName: String = etCompanyName.text.toString()
            val sumAssured: String = etSumAssured.text.toString()
            val name: String = etName.text.toString()
            val startDate: String = txtStartDate.text.toString()
            val endDate: String = txtEndDate.text.toString()
            val address: String = etAddress.text.toString()
            val fee: String = etFee.text.toString()
            val description: String = etDescription.text.toString()
            val status: String = spStatus.selectedItemPosition.toString()

            when {
                //                spPersonalDocumentType.selectedItemPosition == 0 -> Utility.showToast(requireActivity(), "Please select property type")
                TextUtils.isEmpty(companyName) -> Utility.showToast(requireActivity(), "Please enter company name")
                TextUtils.isEmpty(sumAssured) -> Utility.showToast(requireActivity(), "Please enter sum assured")
                TextUtils.isEmpty(name) -> Utility.showToast(requireActivity(), "Please enter name")
                TextUtils.isEmpty(startDate) -> Utility.showToast(requireActivity(), "Please select start date")
                TextUtils.isEmpty(endDate) -> Utility.showToast(requireActivity(), "Please select end date")
                TextUtils.isEmpty(address) -> Utility.showToast(requireActivity(), "Please enter address")
                TextUtils.isEmpty(description) -> Utility.showToast(requireActivity(), "Please enter description")
                TextUtils.isEmpty(fee) -> Utility.showToast(requireActivity(), "Please enter fee")
                spStatus.selectedItemPosition == 0 -> Utility.showToast(requireActivity(), "Please select status")
                else -> {
                    if (txtAdd.text == "Update") {
                        updatePersonalDocuments(
                            selectedItem?.id.toString(),
                            propertyType,
                            name,
                            companyName,
                            sumAssured,
                            startDate,
                            endDate,
                            address,
                            fee,
                            description,
                            status
                        )
                    } else {
                        callAddAPI(
                            propertyType,
                            name,
                            companyName,
                            sumAssured,
                            startDate,
                            endDate,
                            address,
                            fee,
                            description,
                            status
                        )
                    }
                }
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
    private fun getPersonalDocumentTypesList() {
        ApiCallMethods(requireActivity()).getPersonalDocumentTypesList(object
            : OnApiCallCompleted<GetPersonalDocumentTypeListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetPersonalDocumentTypeListResponse = obj as GetPersonalDocumentTypeListResponse

                if (response.isSuccess) {
                    val listPropertyType: MutableList<String> = ArrayList()
                    personalDocumentType = response.list
                    for (i in personalDocumentType.indices) {
                        if (!TextUtils.isEmpty(response.list[i].name))
                            listPropertyType.add(response.list[i].name.toString())
                    }
                    spPersonalDocumentTypeAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listPropertyType)
                    spPersonalDocumentTypeAdapter?.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                    spPersonalDocumentType.adapter = spPersonalDocumentTypeAdapter
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
        name: String,
        companyName: String,
        sumAssured: String,
        startDate: String,
        endDate: String,
        address: String,
        fee: String,
        description: String,
        status: String
    ) {
        ApiCallMethods(requireActivity())
            .addPersonalDocument(
                propertyType,
                name,
                startDate,
                endDate,
                address,
                description,
                fee,
                status,
                companyName,
                sumAssured,
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

    private fun updatePersonalDocuments(
        id: String,
        propertyType: String,
        name: String,
        companyName: String,
        sumAssured: String,
        startDate: String,
        endDate: String,
        address: String,
        fee: String,
        description: String,
        status: String
    ) {
        ApiCallMethods(requireActivity())
            .updatePersonalDocuments(
                id,
                propertyType,
                name,
                startDate,
                endDate,
                address,
                description,
                fee,
                status,
                companyName,
                sumAssured,
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
