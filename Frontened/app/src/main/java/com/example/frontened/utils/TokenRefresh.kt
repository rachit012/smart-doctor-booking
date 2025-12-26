package com.example.frontened.utils

import com.example.frontened.domain.di.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenRefresh @Inject constructor(
    private val tokenManager: TokenManager,
    private val api: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        // avoid infinite loop
        if (responseCount(response) >= 2) return null

        val newToken = try {
            runBlocking {
                api.refreshToken().data?.accessToken
            }
        } catch (e: Exception) {
            null
        }

        newToken ?: return null

        tokenManager.saveAccessToken(newToken)

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }
}

private fun responseCount(response: Response): Int {
    var count = 1
    var prior = response.priorResponse
    while (prior != null) {
        count++
        prior = prior.priorResponse
    }
    return count
}

