/*
 * Created by Joseph Dalughut on 13/06/2021, 17:30
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.presentation.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.verifoxx.faceID_JosephDalughut.R
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Hides the keyboard in a specific [Activity]
 */
fun hideKeyboard(context: Activity) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        context.window.decorView.windowToken, 0
    )
}

/**
 * Displays a snackber message in a specific [Activity].
 *
 * @param view The [View] to anchor the snackbar, usually the root-view of the activity.
 * @param message The resource-id of the message to display.
 */
fun FragmentActivity.showSnackbarMessage(@NonNull view: View, @StringRes message: Int) {
    hideKeyboard(this)
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

/**
 * Displays a snackber message in a specific [Activity].
 *
 * @param view The [View] to anchor the snackbar, usually the root-view of the activity.
 * @param message The message to display.
 */
fun FragmentActivity.showSnackbarMessage(@NonNull view: View, message: String) {
    hideKeyboard(this)
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

/**
 * Default animations for Fragment transitions.
 */
fun FragmentTransaction.defaultAnimation(): FragmentTransaction {
    return this.setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out,
        R.anim.fragment_pop_slide_in,
        R.anim.fragment_pop_slide_out)
}

/**
 * Adds a fragment to an [Activity] by stacking it atop any existing in the provided container view.
 *
 * Using this would allow the user to go back to the previous fragment in the container-view by pressing the phone's back button.
 *
 * @param containerViewId The ID of the container-view this fragment should be added to in the current activity.
 * @param fragment The fragment to be added to the current activity.
 */
fun FragmentActivity.addFragment(@IdRes containerViewId: Int, fragment: Fragment) {
    this.supportFragmentManager.beginTransaction().add(containerViewId, fragment)
        .defaultAnimation()
        .addToBackStack(null)
        .commit()
}

/**
 * Adds a fragment to an [Activity] by replacing the previous one in a specific container view.
 *
 * @param containerViewId The ID of the container-view this fragment should be added to in the current activity.
 * @param fragment The fragment to be added to the current activity.
 */
fun FragmentActivity.replaceFragment(@IdRes containerViewId: Int, fragment: Fragment) {
    this.supportFragmentManager.beginTransaction().add(containerViewId, fragment)
        .defaultAnimation()
        .addToBackStack(null)
        .commit()
}

/**
 * Casts a live-data instance to a mutable one.
 */
fun <T> LiveData<T>.mutable(): MutableLiveData<T>? {
    return this as MutableLiveData<T>
}
