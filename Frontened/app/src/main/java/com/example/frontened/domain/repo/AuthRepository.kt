package com.example.frontened.domain.repo

import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.LoginRequestData
import com.example.frontened.data.dto.RegisterRequestDto
import com.example.frontened.data.dto.RegisterResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun registerUser(request: RegisterRequestDto): Flow<ResultState<String>>

    fun loginUser(request: LoginRequestData): Flow<ResultState<String>>
}