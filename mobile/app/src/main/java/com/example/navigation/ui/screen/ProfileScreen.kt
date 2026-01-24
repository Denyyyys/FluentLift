package com.example.navigation.ui.screen

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.viewmodel.AuthViewModel
import com.example.navigation.viewmodel.UserProfileViewModel
import com.example.navigation.R
import com.example.navigation.data.auth.TokenProvider
import com.example.navigation.ui.component.ProfileRow

@Composable
fun ProfileScreen(
    userViewModel: UserProfileViewModel,
    onLogout: () -> Unit
) {
    val user = userViewModel.user
    val isLoading = userViewModel.isLoading
    val errorMessage = userViewModel.errorMessage

    val profileIconColor = Color(0xFF4CAF50)

    LaunchedEffect(Unit) {
        userViewModel.loadUserInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            return@Column
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red
                )
            }
            return@Column
        }

        user?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    painter = painterResource(id = R.drawable.outline_account_circle_24),
                    contentDescription = "Profile",
                    tint = profileIconColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileRow(
                iconRes = R.drawable.outline_alternate_email_24,
                text = it.email
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileRow(
                iconRes = R.drawable.outline_lock_open_24,
                text = "Change password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileRow(
                iconRes = R.drawable.outline_settings_24,
                text = "Settings"
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button (
                onClick = {
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Log out")
            }
        }
    }
}
