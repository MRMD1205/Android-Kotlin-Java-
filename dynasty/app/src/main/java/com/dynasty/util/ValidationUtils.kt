package com.dynasty.util

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText

import java.util.regex.Pattern

object ValidationUtils {

    /**
     * Regex pattern for Email Validation
     */
    private val regexEmail = (
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
    private val EMAIL_ADDRESS_PATTERN = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE)

    /**
     * Check EditText is Empty or not
     * Input:
     * 1. EditText
     * Output: True / False
     */
    fun isFieldEmpty(et: EditText): Boolean {
        return TextUtils.isEmpty(et.text.toString().trim { it <= ' ' })
    }

    /**
     * Check Email String is Valid or Not
     * Input:
     * 1. Email String
     * Output: True / False
     */
    fun isValidEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun isValidEmailAddress(emailAddress: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
    }

    fun isValidPhoneNumber(target: CharSequence): Boolean {
        return target.length >= 8 && target.length <= 15 && android.util.Patterns.PHONE.matcher(target).matches()
    }
}
