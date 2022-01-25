package com.verifoxx.faceID_JosephDalughut.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityLandingBinding
import com.verifoxx.faceID_JosephDalughut.presentation.registration.RegistrationActivity

/**
 * The main and first activity displayed to the user on startup.
 */
class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // insert our splash-screen to support new Android12 API's
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // move to landing page
        beginRegistrationActivity()
    }

    /**
     * Navigates to the page for registration.
     */
    private fun beginRegistrationActivity() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

}

