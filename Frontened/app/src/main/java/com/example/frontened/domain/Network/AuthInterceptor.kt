package com.example.frontened.domain.Network

import com.example.frontened.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()

        val request = chain.request().newBuilder()

        if(token != null && !tokenManager.isTokenExpired()){
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}