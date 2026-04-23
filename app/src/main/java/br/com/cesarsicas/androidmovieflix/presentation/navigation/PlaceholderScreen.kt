package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * Temporary screen used in Phase 1 so every declared route renders something.
 * Every destination exposes a debug button row that links to every other route,
 * so the full navigation graph is reachable from anywhere. Replaced by real
 * screens in Phases 2+.
 */
@Composable
fun PlaceholderScreen(
    navController: NavHostController,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider()
        Text(text = "Navigate to:", style = MaterialTheme.typography.titleSmall)

        NavButton(navController, "Home", Routes.HOME)
        NavButton(navController, "Auth", Routes.auth())
        NavButton(navController, "Title Details (sample)", Routes.titleDetails(12345))
        NavButton(navController, "Search (query=matrix)", Routes.titleSearch("matrix"))
        NavButton(navController, "Profile", Routes.PROFILE)
        NavButton(navController, "Profile Edit", Routes.PROFILE_EDIT)
        NavButton(navController, "Watch Party", Routes.WATCH_PARTY)

        HorizontalDivider()

        NavButton(navController, "Admin entry", Routes.ADMIN_ENTRY)
        NavButton(navController, "Admin Login", Routes.ADMIN_LOGIN)
        NavButton(navController, "Admin Home", Routes.ADMIN_HOME)
        NavButton(navController, "Admin Watch Party", Routes.ADMIN_WATCH_PARTY)
        NavButton(navController, "New Transmission", Routes.ADMIN_NEW_TRANSMISSION)
        NavButton(navController, "Upload Movie", Routes.ADMIN_UPLOAD_MOVIE)
    }
}

@Composable
private fun NavButton(navController: NavHostController, label: String, route: String) {
    Button(onClick = { navController.navigate(route) }) { Text(label) }
}
