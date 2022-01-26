package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityRegistrationBinding
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStateAdapter
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStatePagerAdapter
import com.verifoxx.faceID_JosephDalughut.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_registration.*
import org.opencv.android.OpenCVLoader

/**
 * The [Activity] responsible for the registration process. This activity houses a [ViewPager] with
 *  a couple of fragments which:
 *  - Inform the user of the registration process and rules.
 *  - Captures their selfie/video
 *  - Extracts their info and stores it on the app for future use.
 */
@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var adapter: FragmentStatePagerAdapter
    val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPager()
        observeModel()
        OpenCVLoader.initDebug()
    }

    /**
     * Sets-up the [ViewPager] to display the fragments responsible for registration.
     */
    private fun setupPager() {
        val fragments = arrayListOf<Fragment>(RegistrationRequestImageFragment.newInstance(),
            RegistrationCameraFragment.newInstance(), RegistrationFeatureExtractionFragment.newInstance())
        adapter = FragmentStatePagerAdapter(supportFragmentManager, androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.fragments = fragments
        binding.pager.adapter = adapter
    }

    private fun observeModel() {
        viewModel.events.observe(this, {
            when (it) {
                RegistrationViewModel.Event.NAV_LANDING -> {
                    // navigate to the registration screen
                    pager.setCurrentItem(0, true)
                }
                RegistrationViewModel.Event.NAV_REGISTRATION -> {
                    pager.setCurrentItem(1, true)
                }
                RegistrationViewModel.Event.NAV_LOGIN -> {
                    navigateToLogin()
                }
                RegistrationViewModel.Event.NAV_FEATURE_EXTRACTION -> {
                    pager.setCurrentItem(2, true)
                }
                RegistrationViewModel.Event.NAV_CLOSE -> {
                    finish()
                }
                RegistrationViewModel.Event.NOTIFY_REGISTRATION_SUCCESS -> {
                    notifyRegistrationSuccess()
                }
                RegistrationViewModel.Event.NOTIFY_REGISTRATION_FAILURE -> {
                    notifyRegistrationFailure()
                }
            }
        })
    }

    private fun notifyRegistrationSuccess() {
        AlertDialog.Builder(this)
            .setTitle(R.string.success)
            .setMessage(R.string.success_message)
            .setCancelable(false)
            .setPositiveButton(R.string.login) { _, _ ->
                navigateToLogin()
            }.show()
    }

    private fun notifyRegistrationFailure() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.registration_error_message)
            .setCancelable(false)
            .setPositiveButton(R.string.okay, DialogInterface.OnClickListener { _, _ ->
                // navigate back to go register again
                pager.currentItem = 1
            }).show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}