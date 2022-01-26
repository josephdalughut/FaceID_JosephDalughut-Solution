package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationCameraBinding
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationFeatureExtractionBinding


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFeatureExtractionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFeatureExtractionFragment : Fragment() {

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
            RegistrationFeatureExtractionFragment()
    }

    private val viewModel: RegistrationViewModel get() {
        return (activity as RegistrationActivity).viewModel
    }
    lateinit var binding: FragmentRegistrationFeatureExtractionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationFeatureExtractionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationImageFile.observe(viewLifecycleOwner, {
            Picasso.get().load(it).centerCrop().fit().into(binding.imgProfile)
        })
    }

}