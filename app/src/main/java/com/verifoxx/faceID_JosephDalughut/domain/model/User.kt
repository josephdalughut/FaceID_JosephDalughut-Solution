/*
 * Created by Joseph Dalughut on 06/05/2021, 13:28
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.domain.model

import org.opencv.core.MatOfPoint


/**
 * A common user of the Lint Application
 */
data class User(
    var id: String,
    var contours: List<MatOfPoint>

)
