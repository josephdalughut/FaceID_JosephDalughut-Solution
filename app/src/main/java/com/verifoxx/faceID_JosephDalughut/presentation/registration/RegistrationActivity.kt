package com.verifoxx.faceID_JosephDalughut.presentation.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.databinding.ActivityRegistrationBinding
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStateAdapter
import com.verifoxx.faceID_JosephDalughut.presentation.FragmentStatePagerAdapter
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPager()
    }

    /**
     * Sets-up the [ViewPager] to display the fragments responsible for registration.
     */
    private fun setupPager() {

        val fragments = arrayListOf<Fragment>(RegistrationRequestImageFragment.newInstance())
        adapter = FragmentStatePagerAdapter(supportFragmentManager, androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.fragments = fragments
        binding.pager.adapter = adapter
        binding.pageIndicator.setViewPager(binding.pager)
    }

}