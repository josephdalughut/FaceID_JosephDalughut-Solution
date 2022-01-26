package com.verifoxx.faceID_JosephDalughut.presentation.login

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.verifoxx.faceID_JosephDalughut.core.FACES
import com.verifoxx.faceID_JosephDalughut.domain.model.Face
import com.verifoxx.faceID_JosephDalughut.preferences.SecureSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val sharedPreferences: SecureSharedPreferences): ViewModel() {

    companion object {

        const val LOG_TAG = "LoginViewModel"

    }

    val events = MutableLiveData<Event>()
    val registrationImageFile = MutableLiveData<File>()

    /**
     * Called when the user clicks the close button. This would trigger an [Event] which closes the app.
     */
    fun onClickCloseButton() {
        events.postValue(Event.NAV_CLOSE)
    }

    /**
     * Called when an image is captured for registration.
     */
    fun onImageCapture(context: Context, file: File) {
        events.postValue(Event.NAV_FEATURE_EXTRACTION)
        registrationImageFile.postValue(file)
        val inputImage = InputImage.fromFilePath(context, Uri.fromFile(file))
        compareFaces(inputImage)
    }

    /**
     * Compares the logged-in face with the saved ones using the MLKit api's.
     *
     * @param inputImage An input image for detection.
     */
    private fun compareFaces(inputImage: InputImage) {
        val detector = FaceDetection.getClient(getFaceDetectorOptions())
        detector.process(inputImage)
            .addOnSuccessListener { faces ->

                // fetch our saved faces.
                val gson = Gson()
                val savedFacesJson = sharedPreferences.get().getString(FACES, null)

                savedFacesJson?.let {
                    val type = object : TypeToken<List<Face>>() {}.type
                    val savedFaces = gson.fromJson<ArrayList<Face>>(it, type)
                    for (face in faces) {

                        // we'll now compare each face found in the login with saved ones.
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                        val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                        val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                        val user = Face(bounds, rotY, rotZ, leftEar, leftEyeContour, upperLipBottomContour)

                        savedFaces.forEach { savedUser ->
                            if (savedUser == user) {
                                onLoginSuccess()
                                return@addOnSuccessListener
                            }
                        }
                    }
                    onLoginFailure()
                } ?: run {
                    onLoginFailure()
                }
            }
            .addOnFailureListener { e ->
                onLoginFailure()
            }
    }

    fun getFaceDetectorOptions(): FaceDetectorOptions {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()
        return highAccuracyOpts
    }

    private fun onLoginSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            events.postValue(Event.NOTIFY_LOGIN_SUCCESS)
        }
    }

    private fun onLoginFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            events.postValue(Event.NOTIFY_LOGIN_FAILURE)
        }
    }


    /**
     * Enum class representing events which should cause some action on the view-layer.
     */
    enum class Event {
        NAV_CLOSE,
        NAV_FEATURE_EXTRACTION,
        NOTIFY_LOGIN_SUCCESS,
        NOTIFY_LOGIN_FAILURE
    }

}