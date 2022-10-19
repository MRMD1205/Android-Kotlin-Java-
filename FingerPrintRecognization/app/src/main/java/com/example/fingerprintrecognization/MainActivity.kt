package com.example.fingerprintrecognization

import android.annotation.SuppressLint
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var prompt: androidx.biometric.BiometricPrompt.PromptInfo

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = androidx.biometric.BiometricPrompt(this, executor, object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                tvLabel.text = "Authentication Error : $errString"
                Toast.makeText(this@MainActivity, "Authentication Error : $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                tvLabel.text = "Authentication Failed..."
                Toast.makeText(this@MainActivity, "Authentication Failed...", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                tvLabel.text = "Authentication Successfully Done"
                Toast.makeText(this@MainActivity, "Authentication Successfully Done", Toast.LENGTH_SHORT).show()
            }
        })

        prompt = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Verify User By Fingerprint Authentication")
            .setNegativeButtonText("Use Face Authentication Instead")
            .build()

        ivFingerScanner.setOnClickListener {
            biometricPrompt.authenticate(prompt)
        }
    }
}