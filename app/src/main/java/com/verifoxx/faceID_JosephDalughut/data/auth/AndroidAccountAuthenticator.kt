/*
 * Created by Joseph Dalughut on 21/10/2021, 21:15
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.data.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.verifoxx.faceID_JosephDalughut.domain.auth.Authenticator
import com.verifoxx.faceID_JosephDalughut.domain.model.User

/**
 * An [Authenticator] backed by Android's own [AbstractAccountAuthenticator] system.
 */
class AndroidAccountAuthenticator(context: Context) :
    AbstractAccountAuthenticator(context), Authenticator {

    companion object Constants {
        const val ACCOUNT_TYPE = "Lint"
        const val ACCESS_TOKEN_KEY = "access_token"
        const val PASSCODE_KEY = "passcode"

        const val LOG_TAG = "AndroidAuth"
    }

    private val accountManager: AccountManager by lazy {
        AccountManager.get(context)
    }

    private var _currentUser: User? = null
    override var currentUser: User?
        get() = _currentUser
        set(value) { _currentUser = value }

    override suspend fun loadUserAccount(): User? {
        accountManager.getAccountsByType(ACCOUNT_TYPE)
            .also {
                if (it.isEmpty()) {
                    return null
                }
                it.first().also { account ->
                    val data = accountManager.getUserData(account, "data")
                    Log.d(LOG_TAG, "User data: $data")
                    return data.let { jsonData ->
                        currentUser = Gson().fromJson(jsonData, User::class.java)
                        currentUser
                    }
                }
            }
    }

    override suspend fun setUserAccount(user: User) {
        Account(user.id, ACCOUNT_TYPE).also {
            val data = Gson().toJson(user)
            val dataBundle = Bundle().apply {
                putString("data", data)
            }
            accountManager.addAccountExplicitly(it, "", dataBundle )
        }
        currentUser = user
    }

    override suspend fun updateUserAccount(user: User) {
        Account(user.id, ACCOUNT_TYPE).also {
            val data = Gson().toJson(user)
            accountManager.setUserData(it, "data", data)
        }
        currentUser = user
    }

    override suspend fun removeUserAccount() {
        loadUserAccount()?.let {
            Account(it.id, ACCOUNT_TYPE).also { account ->
                accountManager.removeAccountExplicitly(account)
            }
        }.also {
            this.currentUser = null
        }
    }

    // UNUSED

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        TODO("Not yet implemented") }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?,
                            requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        TODO("Not yet implemented") }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?,
                                    options: Bundle?): Bundle { TODO("Not yet implemented") }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?,
                              options: Bundle?): Bundle {
        TODO("Not yet implemented") }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("Not yet implemented") }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?,
                                   options: Bundle?): Bundle {
        TODO("Not yet implemented") }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?
    ): Bundle { TODO("Not yet implemented") }

}