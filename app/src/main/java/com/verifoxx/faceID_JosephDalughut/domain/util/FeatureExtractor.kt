package com.verifoxx.faceID_JosephDalughut.domain.util

import org.opencv.core.MatOfPoint
import java.io.File

/**
 * Blueprint for helper class which extracts features from the image/video supplied.
 */
interface FeatureExtractor {

    /**
     * Extracts the contours from an image.
     * @return An array containing the contours.
     */
    suspend fun extractImageFeatures(imageFile: File): List<MatOfPoint>

    /**
     * Extracts the contours from a video.
     * @return An array containing the contours.
     */
    suspend fun extractVideoFeatures(imageFile: File): List<MatOfPoint>

    /**
     * Compares two list of contours to find out their dissimilarity.
     */
    suspend fun compareContours(c1: List<MatOfPoint>, c2: List<MatOfPoint>): Double

}