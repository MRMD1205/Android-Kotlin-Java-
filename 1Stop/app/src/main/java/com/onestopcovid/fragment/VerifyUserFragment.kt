package com.onestopcovid.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onestopcovid.BuildConfig
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.base.BaseFragment
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
        imgCameraBrowseView.setOnClickListener {
            checkImagePickerPermission()
        }

        imgCameraBrowseViewTwo.setOnClickListener {
            checkImagePickerPermissionTwo()
        }

        btnVerifyNow.setOnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    private fun checkImagePickerPermissionTwo() {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object :
            MultiplePermissionsListener {
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

    private fun checkImagePickerPermission() {

        Dexter.withActivity(activity).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object :
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

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                token!!.continuePermissionRequest()
            }
        }).withErrorListener { }.onSameThread().check()
    }

    private fun openImagePickerDialogTwo() {
        ImagePickerDialog(requireActivity(), object : onItemClick {
            override fun onCameraClicked() {
                displayCameraTwo()
            }

            override fun onGalleryClicked() {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, GALLERY_TWO)
            }
        }).show()
    }

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUriTwo)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA_TWO)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("resultCode: ", resultCode.toString())
        when (requestCode) {

            CAMERA -> {
                if (resultCode == RESULT_OK) {
                    Log.e("from camera:", selectedImageUri.toString())
                    tvCamera.visibility = View.GONE
                    Glide.with(this).load(selectedImageUri).into(imgCameraBrowseView)
                }
            }

            GALLERY -> {
                if (resultCode == RESULT_OK) {
                    Log.e("from gallery:", selectedImageUri.toString())
                    tvCamera.visibility = View.GONE
                    selectedImageUri = data?.data!!
                    Glide.with(this).load(selectedImageUri).into(imgCameraBrowseView)
                }
            }

            CAMERA_TWO -> {
                if (resultCode == RESULT_OK) {
                    Log.e("from camera:", selectedImageUriTwo.toString())
                    tvCameraTwo.visibility = View.GONE
                    Glide.with(this).load(selectedImageUriTwo).into(imgCameraBrowseViewTwo)
                }
            }

            GALLERY_TWO -> {
                if (resultCode == RESULT_OK) {
                    Log.e("from gallery:", selectedImageUriTwo.toString())
                    tvCameraTwo.visibility = View.GONE
                    selectedImageUri = data?.data!!
                    Glide.with(this).load(selectedImageUriTwo).into(imgCameraBrowseViewTwo)
                }
            }
        }
    }

    private fun openImagePickerDialog() {

        ImagePickerDialog(requireActivity(), object : onItemClick {
            override fun onCameraClicked() {
                displayCamera()
            }

            override fun onGalleryClicked() {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, GALLERY)
            }
        }).show()
    }


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

            selectedImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file)
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}