package com.example.dailydeals.utility

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import java.util.regex.Matcher

import java.util.regex.Pattern

object ValidationUtils {

    fun isValidPassword(pwd: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(pwd)
        return matcher.matches()
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern
            .compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            )
        val emailMatcher = emailPattern.matcher(email)
        return emailMatcher.matches()
    }

    fun isValidPhone(phone : CharSequence) : Boolean{
        val phonePattern = Pattern
            .compile(
                "^[+]?[0-9]{10,13}\$"
            )
        val phoneMatcher = phonePattern.matcher(phone)
        return phoneMatcher.matches()
    }

}
