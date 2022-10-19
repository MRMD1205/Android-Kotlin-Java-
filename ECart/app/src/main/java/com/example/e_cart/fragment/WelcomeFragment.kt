package com.example.e_cart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.e_cart.R

class WelcomeFragment : Fragment() {

    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            loadFragment(LoginFragment())
        }

        btnRegister.setOnClickListener {
            loadFragment(RegisterFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}