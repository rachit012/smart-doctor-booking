package com.example.frontened.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val success: Boolean,
    val data: ProfileDto
)

@Serializable
data class ProfileDto(
    val image: String?,
    val name: String,
    val email: String,
    val role: String,

    // Doctor-only (nullable for patient)
    val speciality: String? = null,
    val fee: Int? = null,
    val location: String? = null

)




