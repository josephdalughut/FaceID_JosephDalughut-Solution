package com.verifoxx.faceID_JosephDalughut.util

import android.graphics.BitmapFactory
import android.util.Log
import com.verifoxx.faceID_JosephDalughut.domain.util.FeatureExtractor
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import java.util.*

/**
 * Implementation of a [FeatureExtractor] which extracts the features from images/videos supplied.
 */
class FeatureExtractorImpl: FeatureExtractor {

    override suspend fun extractImageFeatures(imageFile: File): List<MatOfPoint> {
        val src = Mat()

        // Read the input
        Utils.bitmapToMat(BitmapFactory.decodeFile(imageFile.path), src)

        // Creating an empty matrix to store the result
        val gray = Mat()

        // Converting the image from color to Gray
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY, 1)

        Imgproc.GaussianBlur(src, gray, Size(3.0, 3.0), 5.0, 10.0, Core.BORDER_DEFAULT)

        val edges = Mat()

        Imgproc.Canny(
            gray,
            edges,
            5.0,
            5.0 * 3,
            3,
            true
        ) // edge detection using canny edge detection algorithm

        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(
            edges,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours.take(5)
    }

    override suspend fun extractVideoFeatures(imageFile: File): List<MatOfPoint> {
        TODO("Not yet implemented")
    }

    override suspend fun compareContours(c1: List<MatOfPoint>, c2: List<MatOfPoint>): Double {
        Log.d("FeatureExtractor", "Fetchhing conntours for ${c1.first()} and ${c2.first()}")
        return Imgproc.matchShapes(c1.first(), c2.first(), Imgproc.CV_CONTOURS_MATCH_I3, 0.0)
    }


}