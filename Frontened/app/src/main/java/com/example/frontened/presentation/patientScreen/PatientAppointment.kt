package com.example.frontened.presentation.patientScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.AppointmentDto

@Composable
fun PatientAppointmentScreen(
    navController: NavController,
    viewModel: PatientViewModel = hiltViewModel()
) {
    val state by viewModel.appointmentsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyAppointments()
    }

    when (state) {
        is ResultState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ResultState.Error -> {
            Text(
                text = (state as ResultState.Error<List<AppointmentDto>>).message,
                modifier = Modifier.padding(16.dp),

                )
        }

        is ResultState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items((state as ResultState.Success<List<AppointmentDto>>).data) { appointment ->
                    AppointmentItem(appointment)
                }
            }
        }
    }
}


@Composable
fun AppointmentItem(appointment: AppointmentDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${appointment.date}", fontWeight = FontWeight.Bold)
            Text("Time: ${appointment.startTime} - ${appointment.endTime}")
            Text("Status: ${appointment.status}", color = Color(0xFF4CAF50))
        }
    }
}
