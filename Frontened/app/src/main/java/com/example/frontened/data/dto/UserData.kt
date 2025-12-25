package com.example.frontened.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    var name: String,
    var email: String,
    var password: String,
    var mobileNumber: String,
    var role: String
)

@Serializable
data class RegisterResponseDto(
    val success: Boolean,
    val message: String
)