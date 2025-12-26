package com.example.frontened.presentation.DoctorDashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.SlotDto

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DoctorDashboardScreen(
    navController: NavController,
    viewModel: DoctorDashboardViewModel = hiltViewModel()
) {
    var date by remember { mutableStateOf("") }
    var slots by remember { mutableStateOf(mutableListOf<SlotDto>()) }

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Doctor Dashboard",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                slots.add(
                    SlotDto(
                        startTime = "09:00",
                        endTime = "09:30"
                    )
                )
            }
        ) {
            Text("Add Slot")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(slots) { slot ->
                Text("${slot.startTime} - ${slot.endTime}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.addAvailability(date, slots)
            }
        ) {
            Text("Save Availability")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ResultState.Loading -> CircularProgressIndicator()
            is ResultState.Success ->
                Text((state as ResultState.Success).data)
            is ResultState.Error ->
                Text((state as ResultState.Error).message)
        }
    }
}
