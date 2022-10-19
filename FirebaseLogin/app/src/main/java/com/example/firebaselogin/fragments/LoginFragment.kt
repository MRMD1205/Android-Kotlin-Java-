package com.example.firebaselogin.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.firebaselogin.R
import com.example.firebaselogin.Utility.Util
import com.example.firebaselogin.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: ImageView
    private lateinit var register: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = view.findViewById(R.id.editTextEmail)
        password = view.findViewById(R.id.editTextPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        register = view.findViewById(R.id.tvLoginToRegister)
        progressBar = view.findViewById(R.id.progressBar)

        firebaseAuth = FirebaseAuth.getInstance()

        register.setOnClickListener {
            val fragment = SignUpFragment()
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.containerFrameLayout, fragment)
                .addToBackStack(null)
            fragmentTransaction.commit()
        }

        btnLogin.setOnClickListener {
            val userEmail = email.text.toString()
            val userPassword = password.text.toString()

            if (isValidate()) {
                progressBar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT)
                                .show()
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
    }

    private fun isValidate(): Boolean {
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        if (!(Util.isValidEmail(userEmail))) {
            Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(Util.isValidPassword(userPassword))) {
            Toast.makeText(activity, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sp.edit()
        editor.putBoolean("booleanIsChecked", true)
        editor.apply()
        return true
    }
}
