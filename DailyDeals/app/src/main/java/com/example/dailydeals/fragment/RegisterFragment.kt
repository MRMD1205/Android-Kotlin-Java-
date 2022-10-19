package com.example.dailydeals.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.dailydeals.R
import com.example.dailydeals.activity.LoginActivity
import com.example.dailydeals.base.BaseFragment
import com.example.dailydeals.utility.LOGIN_FRAGMENT
import com.example.dailydeals.utility.RESULT_LOAD_IMAGE
import com.example.dailydeals.utility.ValidationUtils
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : BaseFragment() {

    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var evPhone: EditText
    private lateinit var evUserName: EditText
    private lateinit var btnSignUp: ImageView
    private lateinit var tvSignInAccount: TextView
    private lateinit var ivProfile: CircleImageView
    private lateinit var ivTakePhoto: CircleImageView
    private lateinit var registerProgressBar: ProgressBar
    private lateinit var databaseReference: DatabaseReference
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var rendomProfileImageKey: String? = null
    private var mImageUri: Uri? = null
    private lateinit var profileImageUrl: String
    private var mStorageRef: StorageReference? = null

    override fun setContentView(): Int {
        return R.layout.fragment_register
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        evEmail = rootView!!.findViewById(R.id.evEmail)
        evPassword = rootView.findViewById(R.id.evPassword)
        evPhone = rootView.findViewById(R.id.evPhone)
        evUserName = rootView.findViewById(R.id.evUserName)
        btnSignUp = rootView.findViewById(R.id.btnSignUp)
        ivProfile = rootView.findViewById(R.id.ivProfile)
        ivTakePhoto = rootView.findViewById(R.id.ivTakePhoto)
        tvSignInAccount = rootView.findViewById(R.id.tvSignInAccount)
        registerProgressBar = rootView.findViewById(R.id.registerProgressBar)
        databaseReference = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().getReference("Profile Images")
    }

    override fun setListeners() {

        tvSignInAccount.setOnClickListener {
            (activity as LoginActivity).navigateToFragment(LoginFragment(), false, LOGIN_FRAGMENT)
        }

        btnSignUp.setOnClickListener {
            isValidate()
        }

        ivProfile.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RESULT_LOAD_IMAGE
            )
        } else {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(
                intent, RESULT_LOAD_IMAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RESULT_LOAD_IMAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULT_LOAD_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    mImageUri = data!!.data
                    ivProfile.setImageURI(mImageUri)
                }
            }
        }
    }

    private fun checkAndRegisterUser() {
        val name = evUserName.text.toString().trim()
        val phone = evPhone.text.toString().trim()
        val email = evEmail.text.toString().trim()
        val password = evPassword.text.toString().trim()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(snapshot.child("Users").child(phone).exists())) {
                    val userDataMap = HashMap<String, Any>()
                    userDataMap["name"] = name
                    userDataMap["phone"] = phone
                    userDataMap["email"] = email
                    userDataMap["password"] = password
                    userDataMap["userType"] = "1"
                    userDataMap["profileImage"] = profileImageUrl

                    databaseReference.child("Users").child(phone).updateChildren(userDataMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showShortToast("Your account has been created")
                                registerProgressBar.visibility = View.GONE

                                (activity as LoginActivity).navigateToFragment(
                                    LoginFragment(),
                                    false,
                                    LOGIN_FRAGMENT
                                )

                            } else {
                                showShortToast("Error : " + it.exception.toString())
                            }
                        }

                } else {
                    showShortToast("This $phone already exists")
                    registerProgressBar.visibility = View.GONE
                    showShortToast("Try again using another phone number or Login")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun isValidate() {
        val name = evUserName.text.toString().trim()
        val phone = evPhone.text.toString().trim()
        val email = evEmail.text.toString().trim()
        val password = evPassword.text.toString().trim()

        if (name.isEmpty()) {
            evUserName.error = "Enter Name"
            evUserName.requestFocus()
        } else if (!(ValidationUtils.isValidPhone(phone))) {
            evPhone.error = "Enter Valid Phone Number"
            evPhone.requestFocus()
        } else if (!(ValidationUtils.isValidEmail(email))) {
            evEmail.error = "Enter Valid Email"
            evEmail.requestFocus()
        } else if (!(ValidationUtils.isValidPassword(password))) {
            evPassword.error = "Enter Valid Password"
            evPhone.requestFocus()
        } else if (mImageUri == null) {
            showShortToast("Please Select Profile Image.")
        } else {
            profileImageInfo()
        }
    }

    private fun profileImageInfo() {

        registerProgressBar.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.time)

        rendomProfileImageKey = "$saveCurrentDate  $saveCurrentTime"

        val filePath =
            mStorageRef!!.child(rendomProfileImageKey + ".jpg")

        val uploadTask = filePath.putFile(mImageUri!!)

        uploadTask.addOnFailureListener {
            showShortToast("Error : $it")
            registerProgressBar.visibility = View.GONE
        }
            .addOnSuccessListener {
                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    profileImageUrl = filePath.downloadUrl.toString()
                    filePath.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        profileImageUrl = it.result.toString()
                        checkAndRegisterUser()
                    }
                }
            }
    }
}