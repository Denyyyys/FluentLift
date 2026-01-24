package com.example.navigation.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.navigation.R
import com.example.navigation.viewmodel.AuthViewModel
import com.example.navigation.viewmodel.DecksViewModel
import com.example.navigation.viewmodel.UserProfileViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel, userViewModel: UserProfileViewModel, decksViewModel: DecksViewModel, onDeckClick: (deckId: Long) -> Unit, onLogout: () -> Unit) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Decks) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = { Icon(
                        painter = painterResource(id = destination.iconRes),
                        contentDescription = destination.label
                    ) },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).padding(start = 20.dp)) {
                when (currentDestination) {
                    AppDestinations.Decks -> DecksScreen(authViewModel, decksViewModel, onDeckClick)
                    AppDestinations.PROFILE -> ProfileScreen(userViewModel, onLogout)
                }
            }
        }
    }
}


enum class AppDestinations(
    val label: String,
    @DrawableRes val iconRes: Int
) {
    Decks("Decks", R.drawable.outline_cards_stack_24),
    PROFILE("Profile", R.drawable.outline_account_circle_24),
}
