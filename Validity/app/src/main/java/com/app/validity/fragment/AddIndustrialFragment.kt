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
import com.app.validity.interfaces.ItemClickCallback
import com.app.validity.model.BaseResponse
import com.app.validity.model.GetIndustrialListResponse
import com.app.validity.model.GetIndustryTypeListResponse
import com.app.validity.model.IndustryType
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.*
import com.bumptech.glide.Glide
import com.app.validity.base.BaseFragment
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_add_industrial.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddIndustrialFragment(val selectedItem: GetIndustrialListResponse.Datum?) : BaseFragment()
    , ItemClickCallback<Uri?> {

    var IndustryType: MutableList<IndustryType> = ArrayList()
    var spIndustryTypeAdapter: ArrayAdapter<String>? = null
    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_add_industrial

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        getIndustryTypesList()

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)
    }

    private fun setUpdateData() {
        if (selectedItem != null) {
            txtPurchaseDate.setText(selectedItem.purchaseDate.toString())
            txtExpiryDate.setText(selectedItem.expiryDate)
            etPurchasePrice.setText(selectedItem.amount.toString())
            etDescription.setText(selectedItem.description)

            for (i in IndustryType.indices) {
                if (selectedItem.industryType?.name == IndustryType[i].name) {
                    spIndustryType.setSelection(selectedItem.industryType?.id!! - 1)
                    break
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
                    val finalDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    txtExpiryDate.text = finalDate

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
            val industryType: String = IndustryType.get(spIndustryType.selectedItemPosition).id.toString()
            val purchaseDate: String = txtPurchaseDate.text.toString()
            val expiryDate: String = txtExpiryDate.text.toString()
            val purchasePrice: String = etPurchasePrice.text.toString()
            val description: String = etDescription.text.toString()

            if (TextUtils.isEmpty(purchaseDate)) {
                Utility.showToast(requireActivity(), "Please select purchase date")
            } else if (TextUtils.isEmpty(expiryDate)) {
                Utility.showToast(requireActivity(), "Please select expiry date")
            } else if (TextUtils.isEmpty(purchasePrice)) {
                Utility.showToast(requireActivity(), "Please enter purchase price")
            } else if (TextUtils.isEmpty(description)) {
                Utility.showToast(requireActivity(), "Please enter description")
//            } else if (txtAdd.text != "Update" && selectedImage == null) {
//                Utility.showToast(requireActivity(), "Please select image")
            } else {
                if (txtAdd.text == "Update") {
                    updateIndustries(
                        selectedItem?.id.toString(),
                        industryType,
                        purchaseDate,
                        expiryDate,
                        purchasePrice,
                        description
                    )
                } else {
                    callAddAPI(
                        industryType,
                        purchaseDate,
                        expiryDate,
                        purchasePrice,
                        description
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedItem == null) {
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_industrial, false)
        } else {
            txtAdd.text = "Update"
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_industrial, false)
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
    private fun getIndustryTypesList() {
        ApiCallMethods(requireActivity()).getIndustryTypesList(object
            : OnApiCallCompleted<GetIndustryTypeListResponse> {
            override fun apiSuccess(obj: Any?) {
                val response: GetIndustryTypeListResponse = obj as GetIndustryTypeListResponse
                if (response.isSuccess) {
                    val listPropertyType: MutableList<String> = ArrayList()
                    IndustryType = response.list
                    for (i in IndustryType.indices) {
                        if (!TextUtils.isEmpty(response.list[i].name))
                            listPropertyType.add(response.list[i].name.toString())
                    }
                    spIndustryTypeAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item_txt, listPropertyType)
                    spIndustryTypeAdapter?.setDropDownViewResource(R.layout.spinner_layout_drop_txt)
                    spIndustryType.adapter = spIndustryTypeAdapter

                    setUpdateData()
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
        industryTypeId: String,
        purchaseDate: String,
        expiryDate: String,
        amount: String,
        description: String
    ) {
        ApiCallMethods(requireActivity())
            .addIndustries(
                industryTypeId,
                purchaseDate,
                expiryDate,
                amount,
                description,
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

    private fun updateIndustries(
        id: String,
        industryTypeId: String,
        purchaseDate: String,
        expiryDate: String,
        amount: String,
        description: String
    ) {
        ApiCallMethods(requireActivity())
            .updateIndustries(
                id,
                industryTypeId,
                purchaseDate,
                expiryDate,
                amount,
                description,
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
