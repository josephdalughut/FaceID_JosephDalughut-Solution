/*
 * Created by Joseph Dalughut on 21/10/2021, 21:15
 * Copyright (c) 2021 . All rights reserved.
 */

package com.litigy.lint.android.data.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.verifoxx.faceID_JosephDalughut.data.auth.AndroidAccountAuthenticator
import com.verifoxx.faceID_JosephDalughut.domain.auth.Authenticator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AndroidAuthenticatorService : Service() {

    @Inject
    lateinit var authenticator: Authenticator

    override fun onBind(intent: Intent): IBinder {
        return (authenticator as AndroidAccountAuthenticator).iBinder
    }
}