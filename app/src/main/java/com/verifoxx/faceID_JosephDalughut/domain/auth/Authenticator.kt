/*
 * Created by Joseph Dalughut on 06/05/2021, 13:40
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.domain.auth

import com.verifoxx.faceID_JosephDalughut.domain.model.User

/**
 * Helper class for managing and authenticating the current [User] account for the application.
 */
interface Authenticator {

    /**
     * The current [User] of the application.
     */
    var currentUser: User?

    /**
     * Loads the current [User]. After this completes successfully, you'll be able to
     * access the user from the [Authenticator.currentUser] value.
     */
    suspend fun loadUserAccount(): User?

    /**
     * Saves a [User] as the current account for the application.
     */
    suspend fun setUserAccount(user: User)

    /**
     * Updates the current [User]'s account.
     */
    suspend fun updateUserAccount(user: User)

    /**
     * Removes the current [User] account on the application. This is equivalent to logging-out
     * the current user.
     */
    suspend fun removeUserAccount()

    /**
     * Fetches the users access token.
     */
    fun getAccessToken(): String?

    /**
     * Sets the users access token
     * @param token The users access token.
     */
    fun setAccessToken(token: String?)

    /**
     * Fetches the users passcode.
     */
    fun getPasscode(): String?

    /**
     * Sets the users passcode.
     * @param passcode The users passcode.
     */
    fun setPasscode(passcode: String?)

    /**
     * Checks if the user has a valid passcode tied to their account.
     * @return true if the user has a valid passcode.
     */
    fun hasValidPasscode(): Boolean

}