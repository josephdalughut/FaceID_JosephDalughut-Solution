package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationCaptureMediaBinding
import iam.thevoid.mediapicker.coroutines.MediaPicker
import iam.thevoid.mediapicker.coroutines.file
import iam.thevoid.mediapicker.picker.Purpose
import iam.thevoid.mediapicker.util.FileUtil.isImage
import iam.thevoid.mediapicker.util.FileUtil.isVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File


/**
 * A fragment which allows the user to capture a picture or video for analysis.
 * Use the [RegistrationCaptureMediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationCaptureMediaFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationCaptureMediaFragment.
         */
        @JvmStatic
        fun newInstance() = RegistrationCaptureMediaFragment().apply {
            this.pickImageActivityResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    viewModel.onSelectImage(requireContext(), it)
                }
            }
            this.pickVideoActivityResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    viewModel.onSelectVideo(requireContext(), it)
                }
            }
        }
    }

    private val viewModel: RegistrationViewModel get() {
        return (activity as RegistrationActivity).viewModel
    }
    lateinit var pickImageActivityResult: ActivityResultLauncher<String>
    lateinit var pickVideoActivityResult: ActivityResultLauncher<String>
    lateinit var binding: FragmentRegistrationCaptureMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationCaptureMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSelectImage.setOnClickListener {
            selectImage()
        }
        binding.btnSelectVideo.setOnClickListener {
            selectVideo()
        }
        binding.btnClose.setOnClickListener{
            viewModel.onClickRegistrationCancelButton()
        }
    }

    /**
     * Starts the image selection process.
     */
    private fun selectImage() {
        pickImageActivityResult.launch("image/*")
    }

    /**
     * Starts the video selection process.
     */
    private fun selectVideo() {
        pickVideoActivityResult.launch("video/*")
    }


}