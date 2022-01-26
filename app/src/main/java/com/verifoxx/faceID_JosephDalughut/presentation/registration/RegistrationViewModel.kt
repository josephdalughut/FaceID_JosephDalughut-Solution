package com.verifoxx.faceID_JosephDalughut.presentation.registration

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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
import java.io.File
import java.util.*
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val sharedPreferences: SecureSharedPreferences,
                                                private val featureExtractor: FeatureExtractor
                                                ): ViewModel() {

    companion object {

        const val LOG_TAG = "RegistrationViewModel"

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
     * Called when an image is captured for registration.
     */
    fun onImageCapture(file: File) {
        events.postValue(Event.NAV_FEATURE_EXTRACTION)
        registrationImageFile.postValue(file)
        extractFaceFeatures(file)
    }

    private fun extractFaceFeatures(file: File) {
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            // handle error
            onFaceDetectionFailure()
        }) {
            val contours = featureExtractor.extractImageFeatures(file)
            val user = User(UUID.randomUUID().toString(), contours)
            val userJson = Gson().toJson(user)
            // save the user.
            sharedPreferences.edit().putString(USER_KEY, userJson).apply()
            onRegistrationSuccess()
        }
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