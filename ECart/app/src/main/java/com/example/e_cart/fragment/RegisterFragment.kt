package com.example.e_cart.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.e_cart.R
import com.example.e_cart.utility.Util
import com.google.firebase.database.*

class RegisterFragment : Fragment() {

    private lateinit var userName: EditText
    private lateinit var userPhone: EditText
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userEmail = view.findViewById(R.id.editTextEmail)
        userPassword = view.findViewById(R.id.editTextPassword)
        userName = view.findViewById(R.id.editTextName)
        userPhone = view.findViewById(R.id.editTextPhone)
        btnRegister = view.findViewById(R.id.btnRegister)
        progressDialog = ProgressDialog(activity)

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val name = userName.text.toString().trim()
        val phone = userPhone.text.toString().trim()
        val email = userEmail.text.toString().trim()
        val password = userPassword.text.toString().trim()

        if (isValidate()) {
            progressDialog.setTitle("Create Account")
            progressDialog.setMessage("Please wait, while we are checking credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            checkAndRegisterUser(name, phone, email, password)
        }
    }

    private fun checkAndRegisterUser(name: String, phone: String, email: String, password: String) {

        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(snapshot.child("Users").child(phone).exists())) {
                    val userDataMap = HashMap<String, Any>()
                    userDataMap["name"] = name
                    userDataMap["phone"] = phone
                    userDataMap["email"] = email
                    userDataMap["password"] = password

                    databaseReference.child("Users").child(phone).updateChildren(userDataMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    "Your account has been created.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressDialog.dismiss()

                                val fragmentTransaction = activity!!
                                    .supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.containerFragment, LoginFragment())
                                    .commit()
                            } else {
                                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(activity, "This $phone already exists", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    Toast.makeText(
                        activity,
                        "Try again using another phone number or Login",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun isValidate(): Boolean {
        val name = userName.text.toString().trim()
        val phone = userPhone.text.toString().trim()
        val email = userEmail.text.toString().trim()
        val password = userPassword.text.toString().trim()

        if (name.isEmpty()) {
            userName.error = "Enter Name"
            userName.requestFocus()
            return false
        }

        if (!(Util.isValidPhone(phone))) {
            userPhone.error = "Enter Valid Phone Number"
            userPhone.requestFocus()
            return false
        }

        if (!(Util.isValidEmail(email))) {
            userEmail.error = "Enter Valid Email"
            userEmail.requestFocus()
            return false
        }

        if (!(Util.isValidPassword(password))) {
            userPassword.error = "Enter Valid Password"
            userPassword.requestFocus()
            return false
        }
        return true
    }
}