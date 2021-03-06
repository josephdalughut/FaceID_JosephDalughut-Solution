package com.verifoxx.faceID_JosephDalughut.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentLoginDetailExtractionBinding


/**
 * A fragment-class which shadows the login-image extraction process, showing a progress bar until it's
 * done.
 * Use the [LoginFeatureExtractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFeatureExtractionFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationFeatureExtractionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFeatureExtractionFragment()
    }

    private val viewModel: LoginViewModel get() {
        return (activity as LoginActivity).viewModel
    }
    lateinit var binding: FragmentLoginDetailExtractionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginDetailExtractionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationImageFile.observe(viewLifecycleOwner) {
            Glide.with(requireActivity()).load(it).centerCrop().fitCenter().into(binding.imgProfile)
        }
    }

}