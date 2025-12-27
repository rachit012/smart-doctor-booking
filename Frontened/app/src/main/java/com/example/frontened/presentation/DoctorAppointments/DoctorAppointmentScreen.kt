package com.example.frontened.presentation.DoctorAppointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.DoctorAppointmentDto
import com.example.frontened.presentation.DoctorAppointments.DoctorAppointmentsViewModel


@Composable
fun DoctorAppointmentsScreen(
    viewModel: DoctorAppointmentsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAppointments()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        when (state) {

            is ResultState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF1976D2)
                )
            }

            is ResultState.Error -> {
                Text(
                    text = (state as ResultState.Error).message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            is ResultState.Success -> {
                val appointments =
                    (state as ResultState.Success<List<DoctorAppointmentDto>>).data

                if (appointments.isEmpty()) {
                    EmptyAppointments()
                } else {
                    AppointmentList(appointments)
                }
            }
        }
    }
}


@Composable
fun AppointmentList(appointments: List<DoctorAppointmentDto>) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(appointments) { appointment ->
            AppointmentCard(appointment)
        }
    }
}

@Composable
fun AppointmentCard(appointment: DoctorAppointmentDto) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Patient ID
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Patient ID: ${appointment.patientId}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Divider()

            // Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF7B1FA2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.date,
                    fontSize = 14.sp
                )
            }

            // Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(0xFF388E3C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${appointment.startTime} - ${appointment.endTime}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Status badge
            StatusChip(appointment.status)
        }
    }
}


@Composable
fun StatusChip(status: String) {

    val background = when (status) {
        "BOOKED" -> Color(0xFFE3F2FD)
        "CANCELLED" -> Color(0xFFFFEBEE)
        else -> Color.LightGray
    }

    val textColor = when (status) {
        "BOOKED" -> Color(0xFF1976D2)
        "CANCELLED" -> Color(0xFFD32F2F)
        else -> Color.Black
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = background,
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(
            text = status,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}


@Composable
fun EmptyAppointments() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No appointments found",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}