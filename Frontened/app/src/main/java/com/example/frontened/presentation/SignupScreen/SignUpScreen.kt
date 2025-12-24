package com.example.frontened.presentation.SignupScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontened.presentation.components.CustomTextField
import com.example.frontened.presentation.navigation.AppRoutes


@Composable
fun SignUpScreen(
    navController: NavController
){
    Box(){
        SignUpContent(
            navController = navController
        )
    }
}

@Composable
private fun SignUpContent(
    // viewModel: LibraryViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var role by rememberSaveable { mutableStateOf("") }







    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Header Section
        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "Join our library community today",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 4.dp, bottom = 24.dp)
        )

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Personal Information Section
                SectionHeader(text = "Personal Information")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "First Name",
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    CustomTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = "Last Name",
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    color = MaterialTheme.colorScheme.onSurface
                )

                CustomTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone Number",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = Icons.Default.Phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    color = MaterialTheme.colorScheme.onSurface
                )

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = Icons.Default.Lock,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))




                CustomTextField(
                    value = role,
                    onValueChange = { role = it.lowercase() },
                    label = "Role (docter/patient)",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = Icons.Default.AccountCircle,
                    keyboardOptions = KeyboardOptions.Default,
                    color = MaterialTheme.colorScheme.onSurface
                )






            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (firstName.isNotEmpty() && lastName.isNotEmpty() &&
                    email.isNotEmpty() && password.isNotEmpty() &&
                    phoneNumber.isNotEmpty() && role.isNotEmpty() ) {



//                    viewModel.signUp(
//                        userData = UserData(
//                            firstName = firstName,
//                            lastName = lastName,
//                            email = email,
//                            password = password,
//                            course = course,
//                            branch = branch
//                        ),
//
//                    )
                    Toast.makeText(context, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate(AppRoutes.PatientScreen.route)
                } else {
                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            TextButton(
                onClick = {
                    navController.navigate(AppRoutes.Login.route){
                        popUpTo(AppRoutes.SignUp.route){inclusive = true}
                    }
                }
            ) {
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}