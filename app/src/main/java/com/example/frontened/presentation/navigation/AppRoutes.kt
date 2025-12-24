package com.example.frontened.presentation.navigation

sealed class AppRoutes(val route: String){

    object SignUp : AppRoutes("SignUpScreen")
    object Login : AppRoutes("LoginScreen")
    object PatientScreen: AppRoutes("PatientScreen")
    object AppointmentScreenOfDoctor : AppRoutes("AppointmentScreenOfDoctor/{doctorName}") {
        fun createRoute(doctorName: String)= "AppointmentScreenOfDoctor/$doctorName"
    }
}