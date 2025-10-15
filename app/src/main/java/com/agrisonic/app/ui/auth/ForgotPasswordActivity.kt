package com.agrisonic.app.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.R
import com.agrisonic.app.data.model.SendVerificationCodeRequest
import com.agrisonic.app.data.remote.RetrofitInstance
import com.agrisonic.app.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSendCode.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (validateEmail(email)) {
                sendVerificationCode(email)
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            showMessage("Please enter your email", false)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email address", false)
            return false
        }

        return true
    }

    private fun sendVerificationCode(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSendCode.isEnabled = false
        binding.tvMessage.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val request = SendVerificationCodeRequest(
                    email = email,
                    type = "password_change"
                )

                val response = RetrofitInstance.authApi.sendVerificationCode(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    showMessage("Verification code sent! Please check your email.", true)

                    // Navigate to verification screen after delay
                    binding.root.postDelayed({
                        val intent = Intent(this@ForgotPasswordActivity, VerifyCodeActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("from", "forgot_password")
                        startActivity(intent)
                        finish()
                    }, 1500)
                } else {
                    val errorMessage = response.body()?.error ?: "Failed to send code. Please try again."
                    showMessage(errorMessage, false)
                }
            } catch (e: Exception) {
                showMessage("Network error: ${e.message}", false)
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnSendCode.isEnabled = true
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
