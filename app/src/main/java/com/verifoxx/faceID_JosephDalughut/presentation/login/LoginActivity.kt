package com.verifoxx.faceID_JosephDalughut.presentation.login

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityLoginBinding
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityRegistrationBinding
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStateAdapter
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStatePagerAdapter
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
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var adapter: FragmentStatePagerAdapter
    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPager()
        observeModel()
        OpenCVLoader.initDebug()
    }

    /**
     * Sets-up the [ViewPager] to display the fragments responsible for registration.
     */
    private fun setupPager() {
        val fragments = arrayListOf<Fragment>(LoginFragment.newInstance(),
            LoginFeatureExtractionFragment.newInstance())
        adapter = FragmentStatePagerAdapter(supportFragmentManager, androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.fragments = fragments
        binding.pager.adapter = adapter
    }

    private fun observeModel() {
        viewModel.events.observe(this, {
            when (it) {
                LoginViewModel.Event.NAV_CLOSE -> {
                    finish()
                }
                LoginViewModel.Event.NAV_FEATURE_EXTRACTION -> {
                    pager.setCurrentItem(1)
                }
                LoginViewModel.Event.NOTIFY_LOGIN_SUCCESS -> {
                    notifyLoginSuccess()
                }
                LoginViewModel.Event.NOTIFY_LOGIN_FAILURE -> {
                    notifyLoginFailure()
                }
            }
        })
    }

    private fun notifyLoginSuccess() {
        AlertDialog.Builder(this)
            .setMessage(R.string.login_success_message)
            .setCancelable(false)
            .setPositiveButton(R.string.login) { _, _ ->
                pager.currentItem = 0
            }.show()
    }

    private fun notifyLoginFailure() {
        AlertDialog.Builder(this)
            .setMessage(R.string.login_error_message)
            .setCancelable(false)
            .setPositiveButton(R.string.okay, DialogInterface.OnClickListener { _, _ ->
                // navigate back
                pager.currentItem = 0
            }).show()
    }

}