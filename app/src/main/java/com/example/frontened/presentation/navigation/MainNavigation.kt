package com.example.frontened.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.PathHitTester
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontened.presentation.SignupScreen.SignUpScreen
import com.example.frontened.presentation.loginScreen.LoginScreen
import com.example.frontened.presentation.patientScreen.AppointmentScreenOfDoctor
import com.example.frontened.presentation.patientScreen.patientScreen


@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = navBackStackEntry?.destination?.route

    val context = LocalContext.current


    Scaffold()
    {innerPadding->

        val startScreen = AppRoutes.Login.route

        NavHost(navController, startDestination = startScreen){

            composable(AppRoutes.SignUp.route){
                SignUpScreen(navController)
            }

            composable(AppRoutes.Login.route){
                LoginScreen(navController)
            }

            composable(AppRoutes.PatientScreen.route){

                patientScreen(navController)
            }

            composable(
                route = AppRoutes.AppointmentScreenOfDoctor.route,
                arguments = listOf(
                    navArgument("doctorName"){ type = NavType.StringType}
                )
            ){backStackEntry->
                val doctorName = backStackEntry.arguments?.getString("doctorName")!!
                AppointmentScreenOfDoctor(doctorName)
            }
        }

    }
}