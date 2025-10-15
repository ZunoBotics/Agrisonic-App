package com.agrisonic.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.AgrisonicApplication
import com.agrisonic.app.ui.auth.LoginActivity
import com.agrisonic.app.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val preferencesManager by lazy {
        (application as AgrisonicApplication).preferencesManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2000) // Show splash for 2 seconds

            val isLoggedIn = preferencesManager.isLoggedIn().first()

            val intent = if (isLoggedIn) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}
