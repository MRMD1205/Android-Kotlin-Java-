package com.example.covidThreeStep.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.onestopcovid.BuildConfig
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.covidThreeStep.R
import com.example.covidThreeStep.activity.HomeActivity
import com.example.covidThreeStep.base.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onestopcovid.CovidThreeStopApplication
import com.onestopcovid.util.*
import kotlinx.android.synthetic.main.fragment_verify_user.*
import java.io.File
import java.util.*

class VerifyUserFragment : BaseFragment() {

    private var selectedImageUri: Uri? = null
    private var selectedImageUriTwo: Uri? = null

    override fun setContentView(): Int = R.layout.fragment_verify_user

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        btnVerifyNow.setOnClickListener {
            navigateToHomeScreen()
        }

        imgCameraBrowseView.setOnClickListener {
            checkImagePickerPermission()
        }

        imgCameraBrowseViewTwo.setOnClickListener {

            checkImagePickerPermissionTwo()
        }

        ivBackArrowVerifyUser.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun navigateToHomeScreen() {
        CovidThreeStopApplication.instance.preferenceData?.setValueBoolean(PREF_IS_USER_LOGGED_IN, true)
        val intent = Intent(activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun checkImagePickerPermission() {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object :
                MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        openImagePickerDialog()
                    } else {
                        Utility.showSettingsDialog(activity)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Utility.showSettingsDialog(activity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener { }.onSameThread().check()
    }

    private fun checkImagePickerPermissionTwo() {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        openImagePickerDialogTwo()
                    } else {
                        Utility.showSettingsDialog(activity)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Utility.showSettingsDialog(activity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener { }.onSameThread().check()
    }

    private fun openImagePickerDialog() {
        ImagePickerDialog(activity, object : onItemClick {
            override fun onCameraClicked() {
                displayCamera()
            }

            override fun onGalleryClicked() {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, GALLERY)
            }
        }).show()
    }

    private fun openImagePickerDialogTwo() {
        ImagePickerDialog(activity, object : onItemClick {
            override fun onCameraClicked() {
                displayCameraTwo()
            }

            override fun onGalleryClicked() {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, GALLERY_CODE)
            }
        }).show()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun displayCamera() {
        val imagesFolder = File(activity?.getExternalFilesDir(getString(R.string.app_name))!!.absolutePath)
        Log.e("Folder Path", imagesFolder.toString())
        try {
            if (!imagesFolder.exists()) {
                Log.e("Creating Folder: ", imagesFolder.toString())
                imagesFolder.mkdirs()
            }
            Log.e("Folder Exist: ", imagesFolder.exists().toString())

            val file = File(imagesFolder, Date().time.toString() + ".jpg")

            selectedImageUriTwo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file)
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun displayCameraTwo() {
        val imagesFolder = File(activity?.getExternalFilesDir(getString(R.string.app_name))!!.absolutePath)
        Log.e("Folder Path", imagesFolder.toString())
        try {
            if (!imagesFolder.exists()) {
                Log.e("Creating Folder: ", imagesFolder.toString())
                imagesFolder.mkdirs()
            }
            Log.e("Folder Exist: ", imagesFolder.exists().toString())

            val file = File(imagesFolder, Date().time.toString() + ".jpg")

            selectedImageUriTwo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file)
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("resultCode: ", resultCode.toString())
        when (requestCode) {
            CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("from camera:", selectedImageUri.toString())
                    tvCamera.visibility = View.GONE
                    Glide.with(this).load(selectedImageUri).into(imgCameraBrowseView)
                }
            }
            GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("from gallery:", selectedImageUri.toString())
                    tvCamera.visibility = View.GONE
                    selectedImageUri = data?.data!!
                    Glide.with(this).load(selectedImageUri).into(imgCameraBrowseView)
                }
            }
            CAMERA_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("from camera:", selectedImageUriTwo.toString())
                    tvCameraTwo.visibility = View.GONE
                    Glide.with(this).load(selectedImageUriTwo).into(imgCameraBrowseViewTwo)
                }
            }
            GALLERY_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("from gallery:", selectedImageUriTwo.toString())
                    tvCameraTwo.visibility = View.GONE
                    selectedImageUriTwo = data?.data!!
                    Glide.with(this).load(selectedImageUriTwo).into(imgCameraBrowseViewTwo)
                }
            }
        }
    }
}