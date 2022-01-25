/*
 * Created by Joseph Dalughut on 21/10/2021, 23:25
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.preferences

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.verifoxx.faceID_JosephDalughut.core.Application

/**
 * A helper class for storing credentials securely.
 */
object SecureSharedPreferences {

    private const val sharedPrefsFile = "securePrefs"
    private val mainKey = MasterKey.Builder(Application.instance)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            Application.instance,
            sharedPrefsFile,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Fetches the [SharedPreferences.Editor] backing this.
     */
    fun edit(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    /**
     * Fetches the [SharedPreferences] backing this.
     */
    fun get(): SharedPreferences {
        return sharedPreferences;
    }

}