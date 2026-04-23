package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.RequireAdminAuth

@Composable
fun AdminHomeScreen(navController: NavHostController) {
    RequireAdminAuth(navController) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Admin Dashboard", style = MaterialTheme.typography.headlineLarge)
            Text("Welcome to the admin panel.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
