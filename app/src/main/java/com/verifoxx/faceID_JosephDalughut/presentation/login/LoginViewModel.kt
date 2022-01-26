package com.verifoxx.faceID_JosephDalughut.presentation.login

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tzutalin.dlib.FaceRec
import com.verifoxx.faceID_JosephDalughut.core.FACE_REC_DIR
import com.verifoxx.faceID_JosephDalughut.core.USER_KEY
import com.verifoxx.faceID_JosephDalughut.domain.auth.Authenticator
import com.verifoxx.faceID_JosephDalughut.domain.model.User
import com.verifoxx.faceID_JosephDalughut.domain.util.FeatureExtractor
import com.verifoxx.faceID_JosephDalughut.preferences.SecureSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.File
import javax.inject.Inject
import org.opencv.imgproc.Imgproc

import org.opencv.core.MatOfPoint

import org.opencv.core.Core.BORDER_DEFAULT
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc.Canny
import org.opencv.imgproc.Imgproc.cvtColor


@HiltViewModel
class LoginViewModel @Inject constructor(private val sharedPreferences: SecureSharedPreferences,
                                         private val featureExtractor: FeatureExtractor): ViewModel() {

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
    fun onImageCapture(file: File) {
        events.postValue(Event.NAV_FEATURE_EXTRACTION)
        registrationImageFile.postValue(file)
        detectFace(file)
    }

    private fun detectFace(file: File) {
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            // handle error
            onLoginFailure()
        }) {
            val contours = featureExtractor.extractImageFeatures(file)
            val gson = Gson()
            sharedPreferences.get().getString(USER_KEY, null)?.let {
                val user = gson.fromJson(it, User::class.java)
                val userContours = user.contours
                val distance = featureExtractor.compareContours(contours, userContours)
                Log.d(LOG_TAG, "Distance $distance")
                if (distance < 5) {
                    onLoginSuccess()
                } else {
                    onLoginFailure()
                }
            } ?: run {
                onLoginFailure()
            }
        }
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