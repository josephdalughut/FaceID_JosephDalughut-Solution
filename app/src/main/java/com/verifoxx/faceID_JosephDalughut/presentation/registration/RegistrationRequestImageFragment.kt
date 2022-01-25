package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationRequestImageBinding

/**
 * The [Fragment] displayed when requesting a users image/video for facial feature extraction..
 * Use the [RegistrationRequestImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationRequestImageFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationRequestImageFragment.
         */
        @JvmStatic
        fun newInstance() =
            RegistrationRequestImageFragment()

    }

    private lateinit var binding: FragmentRegistrationRequestImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationRequestImageBinding.inflate(inflater)
        return binding.root
    }
}