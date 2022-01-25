/*
 * Created by Joseph Dalughut on 03/06/2021, 11:24
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.hilt

import android.content.Context
import com.verifoxx.faceID_JosephDalughut.data.auth.AndroidAccountAuthenticator
import com.verifoxx.faceID_JosephDalughut.core.Application
import com.verifoxx.faceID_JosephDalughut.preferences.SecureSharedPreferences
import com.verifoxx.faceID_JosephDalughut.domain.auth.Authenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = Application.instance

    @Provides
    @Singleton
    fun provideSharedPreferences(): SecureSharedPreferences = SecureSharedPreferences

    @Provides
    @Singleton
    fun provideAuthenticator(context: Context): Authenticator = AndroidAccountAuthenticator(context)

}
