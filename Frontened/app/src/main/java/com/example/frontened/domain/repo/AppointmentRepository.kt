package com.example.frontened.domain.repo

import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.AppointmentDto
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun bookAppointment(
        doctorId: String,
        date: String,
        startTime: String,
        endTime: String
    ): Flow<ResultState<String>>

    fun getMyAppointments(): Flow<ResultState<List<AppointmentDto>>>
}