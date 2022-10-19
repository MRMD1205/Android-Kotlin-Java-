package com.example.e_cart.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.e_cart.R
import com.example.e_cart.activity.AdminPanelAddNewProductActivity
import com.example.e_cart.activity.HomeActivity
import com.example.e_cart.model.User
import com.example.e_cart.utility.Util
import com.google.firebase.database.*

class LoginFragment : Fragment() {

    private lateinit var userPhone: EditText
    private lateinit var userPassword: EditText
    private lateinit var forgetPassword: TextView
    private lateinit var admin: TextView
    private lateinit var notAdmin: TextView
    private lateinit var btnLogin: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var parentDb: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPassword = view.findViewById(R.id.editTextPassword)
        userPhone = view.findViewById(R.id.editTextPhone)
        forgetPassword = view.findViewById(R.id.tvForgotPassword)
        admin = view.findViewById(R.id.tvAdmin)
        notAdmin = view.findViewById(R.id.tvNotAdmin)
        btnLogin = view.findViewById(R.id.btnLogin)
        progressDialog = ProgressDialog(activity)

        parentDb = "Users"

        btnLogin.setOnClickListener {
            loginUser()
        }

        admin.setOnClickListener {
            btnLogin.text = getString(R.string.login_admin)
            admin.visibility = View.GONE
            notAdmin.visibility = View.VISIBLE
            parentDb = "Admins"
        }

        notAdmin.setOnClickListener {
            btnLogin.text = getString(R.string.login)
            admin.visibility = View.VISIBLE
            notAdmin.visibility = View.GONE
            parentDb = "Users"
        }

    }

    private fun loginUser() {

        val phone = userPhone.text.toString()
        val password = userPassword.text.toString()

        if (isValidate()) {
            progressDialog.setTitle("Create Account")
            progressDialog.setMessage("Please wait, while we are checking credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            checkCredentials(phone, password)
        }

    }

    private fun checkCredentials(phone: String, password: String) {

        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(parentDb).child(phone).exists()) {
                    val userData: User =
                        snapshot.child(parentDb).child(phone).getValue(User::class.java)!!
                    if (userData.phone == phone) {
                        if (userData.password == password) {
                            progressDialog.dismiss()

                            val sp = PreferenceManager.getDefaultSharedPreferences(activity)
                            val editor = sp.edit()
                            editor.putBoolean("booleanIsChecked", true)
                            editor.apply()

                            if (parentDb == "Admins") {
                                editor.putBoolean("AdminPanel", true)
                                editor.apply()
                                Toast.makeText(
                                    activity,
                                    "Admin!!Logged in Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        activity,
                                        AdminPanelAddNewProductActivity::class.java
                                    )
                                )
                            } else if (parentDb == "Users") {
                                editor.putBoolean("UserPanel", true)
                                editor.apply()
                                Toast.makeText(
                                    activity,
                                    "Logged in Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(activity, HomeActivity::class.java))
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(activity, "User with $phone does not exists", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isValidate(): Boolean {
        val phone = userPhone.text.toString()
        val password = userPassword.text.toString()

        if (!(Util.isValidPhone(phone))) {
            userPhone.error = "Enter Valid Phone Number"
            userPhone.requestFocus()
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