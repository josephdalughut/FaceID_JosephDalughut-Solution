/*
 * Created by Joseph Dalughut.
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.domain.model

import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.FaceLandmark

/**
 * A model representing a face, and containing features detected by MLKit.
 */
data class Face(
    var bounds: Rect,
    var rotY: Float,
    var rotZ: Float,
    var leftEar: FaceLandmark?,
    var leftEyeContour: List<PointF>?,
    var upperLipBottomContour: List<PointF>?

) {
    override fun equals(other: Any?): Boolean {
        if (other is Face) {
            var equals = bounds.equals(other.bounds)
                    && rotY == other.rotY &&
                    rotZ == other.rotZ
            leftEar?.let {
                equals = equals && it == other.leftEar
            }
            leftEyeContour?.let {
                equals = equals && it == other.leftEyeContour
            }
            upperLipBottomContour?.let {
                equals = equals && it == other.upperLipBottomContour
            }
            return equals
        } else {
            return false
        }
    }
}
