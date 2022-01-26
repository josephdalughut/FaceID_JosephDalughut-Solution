package com.verifoxx.faceID_JosephDalughut.presentation.login

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.verifoxx.faceID_JosephDalughut.R
import com.verifoxx.faceID_JosephDalughut.core.FACE_REC_DIR
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentLoginBinding
import com.verifoxx.faceID_JosephDalughut.databinding.FragmentRegistrationCameraBinding
import com.verifoxx.faceID_JosephDalughut.presentation.extensions.showSnackbarMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService


/**
 * The [Fragment] which allows the user to take images or videoss for extraction.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val TAG = "CameraXBasic"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegistrationCameraFragment.
         */
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                permissionLauncher = registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        startCamera()
                    } else {
                        binding.btnPermissions.visibility = View.VISIBLE
                        binding.btnTakePhoto.visibility = View.GONE
                        binding.txtDescription.setText(R.string.permissions_request_description)
                    }
                }
            }
    }

    private lateinit var binding: FragmentLoginBinding
    private var cameraConnected = false
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val viewModel: LoginViewModel get() {
        return (activity as LoginActivity).viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()) {
            startCamera()
        }
    }

    private fun setupActions() {
        binding.btnPermissions.setOnClickListener {
            checkCameraPermissions()
        }
        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
        binding.btnTakePhoto.setOnLongClickListener {
            takeVideo()
            true
        }
        binding.btnBack.setOnClickListener {
            viewModel.onClickCloseButton()
        }
    }

    /**
     * Checks and requests for permissions.
     */
    private fun checkCameraPermissions() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, FACE_REC_DIR).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
//        cameraExecutor.shutdown()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        binding.btnPermissions.visibility = View.GONE
        binding.btnTakePhoto.show()
        binding.txtDescription.setText(R.string.recording_description)
        outputDirectory = getOutputDirectory()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                imageCapture = ImageCapture.Builder()
                    .build()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                cameraConnected = true
            } catch(exc: Exception) {
                cameraConnected = false
                requireActivity().showSnackbarMessage(binding.root, R.string.error_connecting_camera)
            }

        }, ContextCompat.getMainExecutor(requireActivity()))

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture?.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireActivity()), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    requireActivity().showSnackbarMessage(binding.root, R.string.error_capturing_image)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModel.onImageCapture(photoFile)
                }
            })
    }

    private fun takeVideo() {

    }

}