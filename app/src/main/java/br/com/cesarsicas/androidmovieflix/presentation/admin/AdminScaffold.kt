package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.AdminTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.RequireAdminAuth
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun AdminScaffold(
    navController: NavHostController,
    viewModel: AdminScaffoldViewModel = hiltViewModel(),
    content: @Composable () -> Unit,
) {
    RequireAdminAuth(navController) {
        LaunchedEffect(Unit) {
            viewModel.logoutEvent.collect {
                navController.navigate(Routes.ADMIN_LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        Scaffold(
            topBar = {
                AdminTopBar(
                    onLogoClick = {
                        navController.navigate(Routes.ADMIN_HOME) {
                            popUpTo(Routes.ADMIN_HOME) { inclusive = true }
                        }
                    },
                    onWatchPartyClick = { navController.navigate(Routes.ADMIN_WATCH_PARTY) },
                    onLogout = { viewModel.logout() },
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}
