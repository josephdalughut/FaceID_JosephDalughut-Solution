package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationRequestImageBinding

/**
 * The [Fragment] the user sees first, asking them to login or sign-in.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationRequestImageFragment.
         */
        @JvmStatic
        fun newInstance() =
            RegistrationFragment()

    }

    private lateinit var binding: FragmentRegistrationRequestImageBinding
    private val viewModel: RegistrationViewModel get() {
        return (activity as RegistrationActivity).viewModel
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            viewModel.onClickCloseButton()
        }
        binding.btnLogin.setOnClickListener {
            viewModel.onClickLoginButton()
        }
        binding.btnRegister.setOnClickListener {
            viewModel.onClickRegistrationButton()
        }
    }
}