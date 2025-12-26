package com.example.frontened.presentation.patientScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AppointmentScreenOfDoctor(doctorName: String) {

    val selectedSlot = remember { mutableStateOf<String?>(null) }

    val slots = remember {
        mutableStateOf(
            listOf(
                TimeSlot("10:00 AM"),
                TimeSlot("11:00 AM"),
                TimeSlot("12:00 PM"),
                TimeSlot("02:00 PM"),
                TimeSlot("03:00 PM")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🟦 Doctor Card
        DoctorInfoCard(doctorName)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Appointment Slot",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🟦 Time Slots
        SlotGrid(
            slots = slots.value,
            selectedSlot = selectedSlot.value,
            onSlotClick = { slot ->
                if (!slot.isBooked) {
                    selectedSlot.value = slot.time
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🟦 Book Now Button
        Button(
            onClick = {
                selectedSlot.let { bookedTime ->
                    slots.value = slots.value.map {
                        if (it.time == bookedTime.value) {
                            it.copy(isBooked = true)
                        } else it
                    }
                    selectedSlot.value = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedSlot.value != null
        ) {
            Text("Book Now")
        }
    }
}


data class TimeSlot(
    val time: String,
    val isBooked: Boolean = false
)


@Composable
fun DoctorInfoCard(doctorName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = doctorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text("General Practitioner")
                Text("⭐ 4.9")
            }
        }
    }
}


@Composable
fun SlotGrid(
    slots: List<TimeSlot>,
    selectedSlot: String?,
    onSlotClick: (TimeSlot) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(slots) { slot ->
            val isSelected = slot.time == selectedSlot

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            slot.isBooked -> Color.DarkGray
                            isSelected -> Color(0xFF2196F3)
                            else -> Color(0xFFE3F2FD)
                        }
                    )
                    .clickable(
                        enabled = !slot.isBooked
                    ) {
                        onSlotClick(slot)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slot.time,
                    color = when {
                        slot.isBooked -> Color.DarkGray
                        isSelected -> Color.White
                        else -> Color.Black
                    }
                )
            }
        }
    }
}
