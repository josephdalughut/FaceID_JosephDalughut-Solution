package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val sharedPreferences: SecureSharedPreferences
                                                ): ViewModel() {

    companion object {

        const val LOG_TAG = "RegistrationViewModel"

    }

    val events = MutableLiveData<Event>()
    val registrationImageUri = MutableLiveData<Uri>()

    /**
     * Called when the user clicks the close button. This would trigger an [Event] which closes the app.
     */
    fun onClickCloseButton() {
        events.postValue(Event.NAV_CLOSE)
    }

    /**
     * Called when the user clicks the button to begin the registration process. This would trigger
     * an [Event] which navigates to the registration page.
     */
    fun onClickRegistrationButton() {
        events.postValue(Event.NAV_REGISTRATION)
    }

    /**
     * Called when the user clicks the button to cancel the registration process. This would trigger
     * an [Event] which navigates back to the landing page.
     */
    fun onClickRegistrationCancelButton() {
        events.postValue(Event.NAV_LANDING)
    }

    /**
     * Called when the user clicks the button to cancel feature extraction. This would trigger an [Event]
     * which navigates back to the registration page.
     */
    fun onClickFeatureExtractingCancelButton() {
        events.postValue(Event.NAV_REGISTRATION)
    }

    /**
     * Called when the user clicks the login button. This would trigger an [Event] which navigates to
     * the login page.
     */
    fun onClickLoginButton() {
        events.postValue(Event.NAV_LOGIN)
    }

    /**
     * Called when a image is captured for registration.
     *
     * @param context A [Context] object, needed for the operation.
     * @param uri A [Uri] object housing the path to the iimage.
     */
    fun onSelectImage(context: Context, uri: Uri) {
        events.postValue(Event.NAV_FEATURE_EXTRACTION)
        registrationImageUri.postValue(uri)
        val inputImage = InputImage.fromFilePath(context, uri)
        detectFaces(inputImage)
    }

    /**
     * Called when a video is captured for registration
     *
     * @param uri A [Uri] object housing the path to the video.
     */
    fun onSelectVideo(context: Context, uri: Uri){
        // for videos MLKit doesn't seem to support that, so we'll take frames from the video and parse them as images.
        events.postValue(Event.NAV_FEATURE_EXTRACTION)
        registrationImageUri.postValue(uri)

        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()

        // we'll do these in batches
        val inputImages = ArrayList<InputImage>()
        duration?.let { duration ->
            // the duration will be in milliseconds so we'll split them by one millisecond to find chunks
            // we can loop
            val chunks = duration / 1000

            for (i in 1..chunks) {
                // fetch a bitmap
                bitmap = retriever.getFrameAtTime((i * 1000).toLong())
                bitmap?.let {
                   inputImages.add(InputImage.fromBitmap(it, 0))
                }
            }
        }
        detectFaces(inputImages, ArrayList<Face>())
    }

    /**
     * Detects the faces on an [InputImage] which is in fact our photo.
     */
    private fun detectFaces(inputImage: InputImage) {
        val detector = FaceDetection.getClient(getFaceDetectorOptions())
        detector.process(inputImage)
            .addOnSuccessListener { faces ->

                val foundFaces = ArrayList<Face>()
                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                    val face = Face(bounds, rotY, rotZ, leftEar, leftEyeContour, upperLipBottomContour)
                    foundFaces.add(face)
                }
                val usersJson = Gson().toJson(foundFaces)
                sharedPreferences.edit().putString(FACES, usersJson).apply()
                onRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                onFaceDetectionFailure()
            }
    }

    /**
     * Detects the faces in an array of [InputImage] recursively.
     */
    private fun detectFaces(inputImages: ArrayList<InputImage>, foundFaces: ArrayList<Face>) {
        Log.d(LOG_TAG, "Detecting images in ${inputImages.size}")
        if (inputImages.isEmpty()){
            val usersJson = Gson().toJson(foundFaces)
            sharedPreferences.edit().putString(FACES, usersJson).apply()
            onRegistrationSuccess()
            return
        }
        val detector = FaceDetection.getClient(getFaceDetectorOptions())
        val inputImage = inputImages.removeAt(0)
        detector.process(inputImage)
            .addOnSuccessListener { faces ->

                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                    val face = Face(bounds, rotY, rotZ, leftEar, leftEyeContour, upperLipBottomContour)
                    foundFaces.add(face)
                }
                detectFaces(inputImages, foundFaces)
            }
            .addOnFailureListener { e ->
                detectFaces(inputImages, foundFaces)
            }
    }



    private fun getFaceDetectorOptions(): FaceDetectorOptions {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()
        return  highAccuracyOpts
    }

    private fun onRegistrationSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            events.postValue(Event.NOTIFY_REGISTRATION_SUCCESS)
        }
    }

    private fun onFaceDetectionFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            events.postValue(Event.NOTIFY_REGISTRATION_FAILURE)
        }
    }


    /**
     * Enum class representing events which should cause some action on the view-layer.
     */
    enum class Event {
        NAV_CLOSE,
        NAV_REGISTRATION,
        NAV_FEATURE_EXTRACTION,
        NAV_LOGIN,
        NAV_LANDING,
        NOTIFY_REGISTRATION_SUCCESS,
        NOTIFY_REGISTRATION_FAILURE
    }

}