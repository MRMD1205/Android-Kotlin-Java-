package com.example.firebaselogin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.firebaselogin.R
import com.example.firebaselogin.Utility.Util
import com.example.firebaselogin.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mobile: EditText
    private lateinit var name: EditText
    private lateinit var btnRegister: ImageView
    private lateinit var login: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = view.findViewById(R.id.editTextName)
        mobile = view.findViewById(R.id.editTextMobile)
        email = view.findViewById(R.id.editTextEmail)
        password = view.findViewById(R.id.editTextPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        login = view.findViewById(R.id.tvRegisterToLogin)
        progressBar = view.findViewById(R.id.progressBar)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        login.setOnClickListener {
            val fragment = LoginFragment()
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.containerFrameLayout, fragment).commit()
        }
        btnRegister.setOnClickListener {
            registerUser()
        }
    }


    private fun registerUser() {
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        val userMobile = mobile.text.toString()
        val userName = name.text.toString()

        if (isValidate()) {
            progressBar.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "User Created", Toast.LENGTH_SHORT)
                            .show()
                        val user = firebaseAuth.currentUser
                        val userId = user!!.uid
                        databaseReference.child(userId).child("Name").setValue(userName)
                        databaseReference.child(userId).child("Email").setValue(userEmail)
                        databaseReference.child(userId).child("Mobile").setValue(userMobile)
                        databaseReference.child(userId).child("Password").setValue(userPassword)
                        startActivity(Intent(activity, HomeActivity::class.java))
                    } else {
                        Toast.makeText(
                            activity,
                            "Error : " + it.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                }
        }
    }

    private fun isValidate(): Boolean {
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        val userMobile = mobile.text.toString()
        val userName = name.text.toString()

        if (userName.isEmpty()) {
            Toast.makeText(activity, "Enter Name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!(Util.isValidPhone(userMobile))) {
            Toast.makeText(activity, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!(Util.isValidEmail(userEmail))) {
            Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!(Util.isValidPassword(userPassword))) {
            Toast.makeText(activity, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}