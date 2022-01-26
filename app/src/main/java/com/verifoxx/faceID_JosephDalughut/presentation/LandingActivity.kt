package com.verifoxx.faceID_JosephDalughut.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.verifoxx.faceID_JosephDalughut.core.FACES
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityLandingBinding
import com.verifoxx.faceID_JosephDalughut.preferences.SecureSharedPreferences
import com.verifoxx.faceID_JosephDalughut.presentation.login.LoginActivity
import com.verifoxx.faceID_JosephDalughut.presentation.registration.RegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The main and first activity displayed to the user on startup.
 */
@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject lateinit var sharedPreferences: SecureSharedPreferences
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // insert our splash-screen to support new Android12 API's
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (sharedPreferences!!.get().contains(FACES)) {
            // move to login as we have a user
            navigateToLogin()
        } else {
            // move to landing page
            beginRegistrationActivity()
        }
    }

    /**
     * Navigates to the page for registration.
     */
    private fun beginRegistrationActivity() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Navigates to the login page
     */
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}

