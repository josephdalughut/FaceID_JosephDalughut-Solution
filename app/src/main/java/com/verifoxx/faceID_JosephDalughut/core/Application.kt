/*
 * Created by Joseph Dalughut on 02/06/2021, 02:59
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.core

import android.app.Application
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

/**
 * Extension of the android [Application] class which serves as the main enntry-point to our application.
 */

@HiltAndroidApp
class Application: Application() {

    companion object {

        /**
         * Singleton instance of the [Application] class.
         */
        lateinit var instance: Application
    }

    init {  instance = this }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }

}