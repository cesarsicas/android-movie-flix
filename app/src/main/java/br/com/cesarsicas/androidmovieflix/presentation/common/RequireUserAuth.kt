package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun RequireUserAuth(
    navController: NavHostController,
    viewModel: AuthGuardViewModel = hiltViewModel(),
    content: @Composable () -> Unit,
) {
    val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    when (isLoggedIn) {
        null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        false -> LaunchedEffect(Unit) {
            navController.navigate(Routes.auth()) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
            }
        }
        true -> content()
    }
}
