package com.example.frontened.domain.Network

import android.content.Context
import android.content.SharedPreferences
import com.example.frontened.data.repository.Auth.AuthRepoImpl
import com.example.frontened.data.repository.fake.FakeAuthRepository
import com.example.frontened.domain.di.AuthApi
import com.example.frontened.domain.repo.AuthRepository
import com.example.frontened.utils.TokenManager
import com.example.frontened.utils.TokenRefresh
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, tokenManager: TokenManager): AuthRepository =
        AuthRepoImpl(api,tokenManager)

    @Provides
    @Singleton
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideTokenManager(
        sharedPreferences: SharedPreferences
    ): TokenManager = TokenManager(sharedPreferences)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenRefresh: TokenRefresh
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenRefresh)
            .build()
    }




}