package com.example.navigation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.R
import com.example.navigation.ui.theme.Primary600
import com.example.navigation.ui.theme.Primary900
import com.example.navigation.viewmodel.AuthViewModel


@Composable
fun SignUpScreen(authViewModel: AuthViewModel, onSuccess: () -> Unit, onSignInClick: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val jwt = authViewModel.jwtToken
    val errorMessage = authViewModel.errorMessage

    LaunchedEffect(jwt) {
        jwt?.let {
            onSuccess()
        }
    }

    val icon = if(passwordVisible)
        painterResource(id = R.drawable.design_ic_visibility)
    else
        painterResource(id = R.drawable.design_ic_visibility_off)

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "FluentLift",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 24.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Sign Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Primary600,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Name",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp
            )

            OutlinedTextField(
                value=name,
                onValueChange = {
                    name = it
                },
                singleLine = true,
                placeholder = {Text(text = "Name")},
                label =  {Text(text = "Name")},
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_account_circle_24),
                            contentDescription = "Name"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Email",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp
            )

            OutlinedTextField(
                value=email,
                onValueChange = {
                    email = it
                },
                singleLine = true,
                placeholder = {Text(text = "Email")},
                label =  {Text(text = "Email")},
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_alternate_email_24),
                            contentDescription = "Email"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
            )


            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Password",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value=password,
                onValueChange = {
                    password = it
                },
                singleLine = true,
                placeholder = {Text(text = "Password")},
                label =  {Text(text = "Password")},
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            painter = icon,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
            )

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    authViewModel.signUp(name, email, password)
                },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary900)
            ) {
                Text(text = if (authViewModel.isLoading) "Signing up..." else "Sign Up", color = Color.White)
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(text = "Have an Account? ")
                Text(
                    text = "Sign In",
                    color = Primary900,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable() {
                        onSignInClick()
                    }
                )
            }
        }
    }
}
