package com.example.frontened.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class DoctorDto(
    val name: String,
    val email: String,
    val speciality: String,
    val fee: Int,
    val location: LocationDto
): Parcelable

@Parcelize
data class LocationDto(
    val type: String,
    val coordinates: List<Double> // [lng, lat]
): Parcelable


data class NearbyDoctorResponse(
    val success: Boolean,
    val data: List<DoctorDto>
)
