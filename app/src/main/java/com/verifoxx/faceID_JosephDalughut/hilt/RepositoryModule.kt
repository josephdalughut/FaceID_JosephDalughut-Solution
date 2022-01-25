/*
 * Created by Joseph Dalughut on 03/06/2021, 11:24
 * Copyright (c) 2021 . All rights reserved.
 */

package com.verifoxx.faceID_JosephDalughut.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideAuthenticationRepository(
//        authenticator: Authenticator,
//        retrofitApiHelper: RetrofitApiHelper
//    ): AuthenticationRepository =
//        AuthenticationDataRepository(
//            authenticator,
//            retrofitApiHelper)
//
//    @Provides
//    @Singleton
//    fun provideUserRepository(authenticator: Authenticator): UserRepository =
//        UserDataRepository(authenticator)
//
//    @Provides
//    @Singleton
//    fun provideWalletRepository(retrofitApiHelper: RetrofitApiHelper): WalletRepository {
//        val authApi = retrofitApiHelper.getRetrofit(true).create(WalletApi::class.java)
//        return WalletDataRepository(MockWalletDataSource(), RemoteWalletDataSource(authApi))
//    }
//
//    @Provides
//    @Singleton
//    fun provideBillerRepository(authenticator: Authenticator, retrofitApiHelper: RetrofitApiHelper): BillerRepository {
//        val remoteDataSource = RemoteBillerDataSource(retrofitApiHelper)
//        return BillerDataRepository(authenticator, remoteDataSource)
//    }
//
//    @Provides
//    @Singleton
//    fun provideMiscRepository(retrofitApiHelper: RetrofitApiHelper): MiscRepository {
//        val remoteDataSource = RemoteMiscDataSource(retrofitApiHelper)
//        return MiscDataRepository(remoteDataSource)
//    }

}
