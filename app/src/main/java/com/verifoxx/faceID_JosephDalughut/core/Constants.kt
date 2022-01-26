/*
 * Created by Joseph Dalughut on 13/06/2021, 21:36
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.core

import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import com.verifoxx.faceID_JosephDalughut.R


const val FACE_REC_DIR = "Face ID App"
const val USER_KEY = "user"

fun showError(textInputLayout: TextInputLayout, editText: EditText, @StringRes messageRes: Int) {
    textInputLayout.error = textInputLayout.resources.getString(messageRes)
    editText.requestFocus()
}

fun FragmentTransaction.defaultAnimation(): FragmentTransaction {
    return this.setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out,
        R.anim.fragment_pop_slide_in,
        R.anim.fragment_pop_slide_out)
}