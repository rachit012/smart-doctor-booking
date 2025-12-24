package com.example.frontened.presentation.patientScreen

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontened.presentation.components.ExpandableSearchBar
import com.example.frontened.presentation.navigation.AppRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun patientScreen(
    navController: NavController
){

    var searchQuery by remember { mutableStateOf("") }

    val filteredDoctors = doctors.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.specialization.contains(searchQuery, ignoreCase = true)
    }


    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {}) {
                        Icons.Default.Home
                    }

                    IconButton(onClick = {}) {
                        Icons.Default.Bookmark
                    }

                    IconButton(onClick = {}) {
                        Icons.Default.VideoCall
                    }

                    IconButton(onClick = {}) {
                        Icons.Default.Person
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            HeaderSection(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategorySection()
            Spacer(modifier = Modifier.height(16.dp))
            DoctorSection(filteredDoctors,
                onBookNowClick = {doctor->
                    navController.navigate(
                        AppRoutes.AppointmentScreenOfDoctor.createRoute(doctor.name)
                    )

                })
        }
    }
}

@Composable
fun HeaderSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Hello, Chloe F.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpandableSearchBar(
            query = searchQuery,
            onQueryChange = onSearchChange
        )
    }
}

@Composable
fun CategorySection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categories", fontWeight = FontWeight.Bold)
            Text("View All")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(categories) { category ->
                CategoryItem(category)
            }
        }
    }
}


@Composable
fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(90.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(category.icon, contentDescription = null)
            Spacer(modifier = Modifier.height(4.dp))
            Text(category.name, fontSize = 12.sp)
        }
    }
}


@Composable
fun DoctorSection(filteredDoctors: List<Doctor>, onBookNowClick: (Doctor) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Our doctors", fontWeight = FontWeight.Bold)
            Text("View All")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredDoctors) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onBookNowClick = { onBookNowClick(doctor) }
                )
            }
        }
    }
}


@Composable
fun DoctorCard(doctor: Doctor,
               onBookNowClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(doctor.name, fontWeight = FontWeight.Bold)
            Text(doctor.specialization)

            Text("⭐ ${doctor.rating}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onBookNowClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Book Now")
            }
        }
    }
}

data class Category(
    val name: String,
    val icon: ImageVector
)

data class Doctor(
    val name: String,
    val specialization: String,
    val rating: Double
)

val categories = listOf(
    Category("Check-up", Icons.Default.Favorite),
    Category("Dental", Icons.Default.Face),
    Category("Cardiology", Icons.Default.MonitorHeart)
)

val doctors = listOf(
    Doctor("Helena Ricci", "General Practitioner", 4.9),
    Doctor("Salman Pacheco", "Cardiologist", 4.8)
)


