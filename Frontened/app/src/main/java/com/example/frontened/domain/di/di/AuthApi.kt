package com.example.frontened.domain.di

import com.example.frontened.data.dto.LoginRequestData
import com.example.frontened.data.dto.LoginResponseDto
import com.example.frontened.data.dto.RegisterRequestDto
import com.example.frontened.data.dto.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body requestData: LoginRequestData
    ): LoginResponseDto

    @POST("api/auth/refresh")
    suspend fun refreshToken(): LoginResponseDto

}