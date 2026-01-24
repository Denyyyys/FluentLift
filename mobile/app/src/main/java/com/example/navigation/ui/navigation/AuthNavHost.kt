package com.example.navigation.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.screen.DeckStudyScreen
import com.example.navigation.ui.screen.DecksScreen
import com.example.navigation.ui.screen.HomeScreen
import com.example.navigation.ui.screen.LoginScreen
import com.example.navigation.ui.screen.SignUpScreen
import com.example.navigation.viewmodel.AuthViewModel
import com.example.navigation.viewmodel.DeckStudyViewModel
import com.example.navigation.viewmodel.DecksViewModel
import com.example.navigation.viewmodel.UserProfileViewModel

@Composable
fun AuthNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserProfileViewModel = viewModel()
    val decksViewModel: DecksViewModel = viewModel()
    val deckStudyViewModel: DeckStudyViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        fun logout() {
            authViewModel.logout()
            userViewModel.clearUser()
            navController.navigate("login")
        }
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    navController.navigate("home")
                },
                onSignInClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    navController.navigate("home")
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("home") {
            HomeScreen(
                authViewModel,
                userViewModel,
                decksViewModel,
                onDeckClick = { deckId ->
                    val deck = decksViewModel.decks.firstOrNull { it.id == deckId }
                    Log.d("deckStudy", deck.toString())
                    if (deck != null) {
                        deckStudyViewModel.loadDeck(deck)
                        navController.navigate("deckStudy")
                    }
                },
                onLogout = {logout()}
            )
        }

        composable("decks") {
            DecksScreen(
                authViewModel = authViewModel,
                decksViewModel = decksViewModel,
                onDeckClick = { deckId ->
                    val deck = decksViewModel.decks.firstOrNull { it.id == deckId }
                    Log.d("deckStudy", deck.toString())
                    if (deck != null) {
                        deckStudyViewModel.loadDeck(deck)
                        navController.navigate("deckStudy")
                    }
                }
            )
        }

        composable("deckStudy") {
            DeckStudyScreen(deckStudyViewModel, onGoBack = {navController.navigate("decks")})
        }
    }
}
