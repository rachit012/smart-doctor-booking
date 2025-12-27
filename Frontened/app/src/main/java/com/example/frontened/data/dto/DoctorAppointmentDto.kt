package com.example.frontened.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class DoctorAppointmentDto(
    val _id: String,
    val doctorId: String,
    val patientId: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class DoctorAppointmentResponse(
    val success: Boolean,
    val data: List<DoctorAppointmentDto>
)

