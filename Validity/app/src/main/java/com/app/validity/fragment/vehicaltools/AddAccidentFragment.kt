package com.app.validity.fragment.vehicaltools


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.app.validity.model.BaseResponse
import com.app.validity.model.VehicalAccident
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
import kotlinx.android.synthetic.main.fragment_add_accident.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddAccidentFragment(var vehicalId: String, val vehicalItem: VehicalAccident?) : BaseFragment(), ItemClickCallback<Uri?> {

    private var selectedImage: Uri? = null
    private var selectedImageList: ArrayList<Uri?> = ArrayList()

    override fun setContentView(): Int = R.layout.fragment_add_accident

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

        selectedImageList.add(selectedImage)
        rvVehicalImages.adapter = ImageAdapter(selectedImageList, this)

        if (vehicalItem != null) {
            if (!TextUtils.isEmpty(vehicalItem.accidentTime)) {
                txtAccidentTime.hint = vehicalItem.accidentTime
                val orgSdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val newSdf = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val dateObj: Date = orgSdf.parse(vehicalItem.accidentTime.toString())
                txtAccidentTime.text = SimpleDateFormat("KK:mm a").format(dateObj)
            }

            txtAccidentDate.setText(if (TextUtils.isEmpty(vehicalItem.accidentDate)) "" else vehicalItem.accidentDate)
            etDriverName.setText(if (TextUtils.isEmpty(vehicalItem.driverName)) "" else vehicalItem.driverName)
            etAmount.setText(if (TextUtils.isEmpty(vehicalItem.amount)) "" else vehicalItem.amount)
            etKmReading.setText(if (TextUtils.isEmpty(vehicalItem.kmReading)) "" else vehicalItem.kmReading)
            etDescription.setText(if (TextUtils.isEmpty(vehicalItem.description)) "" else vehicalItem.description)
        }
    }

    override fun setListeners() {
        txtAccidentDate.setOnClickListener {
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
                    txtAccidentDate.text =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        txtAccidentTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                txtAccidentTime.text =
                    SimpleDateFormat("HH:mm a", Locale.getDefault()).format(cal.time)
                txtAccidentTime.hint =
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(cal.time)
            }
            TimePickerDialog(
                requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), false
            ).show()
        }

        txtAdd.setOnClickListener {
            if (TextUtils.isEmpty(txtAccidentDate.text.toString())) {
                Utility.showToast(requireActivity(), "Please select accident date")
            } else if (TextUtils.isEmpty(txtAccidentTime.text.toString())) {
                Utility.showToast(requireActivity(), "Please select accident time")
            } else if (TextUtils.isEmpty(etDriverName.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter driver name")
            } else if (TextUtils.isEmpty(etAmount.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter amount")
            } else if (TextUtils.isEmpty(etKmReading.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter KM reading")
            } else if (TextUtils.isEmpty(etDescription.text.toString())) {
                Utility.showToast(requireActivity(), "Please enter description")
            } else {
                if (txtAdd.text.toString() == "Update") {
                    callUpdateAPI()
                } else {
                    callAddAPI()
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
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_add_accident, false)
        } else {
            txtAdd.setText("Update")
            (requireActivity() as DashboardActivity).setHeader(R.string.menu_update_accident, false)
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

    private fun callAddAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.addVehicleAccident(
            vehicalId,
            txtAccidentDate.text.toString(),
            txtAccidentTime.hint.toString(),
            etDriverName.text.toString(),
            etAmount.text.toString(),
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

    private fun callUpdateAPI() {
        val apiCallMethods = ApiCallMethods(requireActivity())
        apiCallMethods.updateVehicleAccident(
            vehicalId,
            vehicalItem!!.id.toString(),
            txtAccidentDate.text.toString(),
            txtAccidentTime.hint.toString(),
            etDriverName.text.toString(),
            etAmount.text.toString(),
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

}
