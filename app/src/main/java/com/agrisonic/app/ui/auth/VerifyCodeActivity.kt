package com.agrisonic.app.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.R
import com.agrisonic.app.data.model.*
import com.agrisonic.app.data.remote.RetrofitInstance
import com.agrisonic.app.databinding.ActivityVerifyCodeBinding
import kotlinx.coroutines.launch

class VerifyCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyCodeBinding
    private lateinit var email: String
    private lateinit var from: String
    private var isCodeVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("email") ?: ""
        from = intent.getStringExtra("from") ?: "signup"

        binding.tvEmail.text = email

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnVerify.setOnClickListener {
            val code = binding.etCode.text.toString().trim()
            if (validateCode(code)) {
                verifyCode(code)
            }
        }

        binding.tvResendCode.setOnClickListener {
            resendCode()
        }

        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmNewPassword.text.toString()
            if (validatePasswords(newPassword, confirmPassword)) {
                changePassword(newPassword, confirmPassword)
            }
        }
    }

    private fun validateCode(code: String): Boolean {
        if (code.isEmpty()) {
            showMessage("Please enter the verification code", false)
            return false
        }

        if (code.length != 6) {
            showMessage("Verification code must be 6 digits", false)
            return false
        }

        return true
    }

    private fun validatePasswords(password: String, confirmPassword: String): Boolean {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please enter both passwords", false)
            return false
        }

        if (password.length < 8) {
            showMessage("Password must be at least 8 characters", false)
            return false
        }

        if (password != confirmPassword) {
            showMessage("Passwords do not match", false)
            return false
        }

        return true
    }

    private fun verifyCode(code: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnVerify.isEnabled = false
        binding.tvMessage.visibility = View.GONE

        lifecycleScope.launch {
            try {
                if (from == "signup") {
                    // Verify signup code
                    val request = VerifySignupRequest(email = email, code = code)
                    val response = RetrofitInstance.authApi.verifySignup(request)

                    if (response.isSuccessful && response.body()?.success == true) {
                        showMessage("Email verified successfully! Redirecting to login...", true)

                        // Navigate to login after delay
                        binding.root.postDelayed({
                            startActivity(Intent(this@VerifyCodeActivity, LoginActivity::class.java))
                            finish()
                        }, 1500)
                    } else {
                        val errorMessage = response.body()?.error ?: "Invalid verification code"
                        showMessage(errorMessage, false)
                    }
                } else {
                    // Verify password reset code
                    val request = VerifyCodeRequest(
                        email = email,
                        code = code,
                        type = "password_change"
                    )
                    val response = RetrofitInstance.authApi.verifyCode(request)

                    if (response.isSuccessful && response.body()?.success == true) {
                        showMessage("Code verified! Please enter your new password.", true)
                        isCodeVerified = true

                        // Show password fields
                        binding.layoutNewPassword.visibility = View.VISIBLE
                        binding.btnVerify.visibility = View.GONE
                        binding.etCode.isEnabled = false
                    } else {
                        val errorMessage = response.body()?.error ?: "Invalid verification code"
                        showMessage(errorMessage, false)
                    }
                }
            } catch (e: Exception) {
                showMessage("Network error: ${e.message}", false)
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnVerify.isEnabled = true
            }
        }
    }

    private fun resendCode() {
        binding.tvResendCode.isEnabled = false
        showMessage("Sending new code...", true)

        lifecycleScope.launch {
            try {
                val request = SendVerificationCodeRequest(
                    email = email,
                    type = if (from == "signup") "signup" else "password_change"
                )

                val response = RetrofitInstance.authApi.sendVerificationCode(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    showMessage("New code sent! Check your email.", true)
                } else {
                    showMessage("Failed to resend code", false)
                }
            } catch (e: Exception) {
                showMessage("Network error: ${e.message}", false)
            } finally {
                binding.tvResendCode.isEnabled = true
            }
        }
    }

    private fun changePassword(newPassword: String, confirmPassword: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnChangePassword.isEnabled = false
        binding.tvMessage.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val request = ChangePasswordRequest(
                    email = email,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )

                val response = RetrofitInstance.authApi.changePassword(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    showMessage("Password changed successfully! Redirecting to login...", true)

                    // Navigate to login after delay
                    binding.root.postDelayed({
                        startActivity(Intent(this@VerifyCodeActivity, LoginActivity::class.java))
                        finish()
                    }, 1500)
                } else {
                    val errorMessage = response.body()?.error ?: "Failed to change password"
                    showMessage(errorMessage, false)
                }
            } catch (e: Exception) {
                showMessage("Network error: ${e.message}", false)
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnChangePassword.isEnabled = true
            }
        }
    }

    private fun showMessage(message: String, isSuccess: Boolean) {
        binding.tvMessage.text = message
        binding.tvMessage.visibility = View.VISIBLE

        if (isSuccess) {
            binding.tvMessage.setTextColor(getColor(R.color.success))
            binding.tvMessage.setBackgroundColor(Color.parseColor("#dcfce7"))
        } else {
            binding.tvMessage.setTextColor(getColor(R.color.error))
            binding.tvMessage.setBackgroundColor(Color.parseColor("#fee2e2"))
        }
    }
}
