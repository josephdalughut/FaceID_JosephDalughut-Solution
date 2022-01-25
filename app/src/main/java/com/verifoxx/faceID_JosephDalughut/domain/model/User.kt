/*
 * Created by Joseph Dalughut on 06/05/2021, 13:28
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * A common user of the Lint Application
 */
@Parcelize
data class User(var id: String,
                var name: String,
                var email: String
): Parcelable
