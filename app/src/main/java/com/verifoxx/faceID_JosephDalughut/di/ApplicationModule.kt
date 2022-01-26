/*
 * Created by Joseph Dalughut.
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.di

import android.content.Context
import com.verifoxx.faceID_JosephDalughut.core.Application
import com.verifoxx.faceID_JosephDalughut.preferences.SecureSharedPreferences

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


}
