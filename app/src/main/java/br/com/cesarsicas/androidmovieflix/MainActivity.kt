package br.com.cesarsicas.androidmovieflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.cesarsicas.androidmovieflix.presentation.common.ChatFab
import br.com.cesarsicas.androidmovieflix.presentation.navigation.AppNavHost
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import br.com.cesarsicas.androidmovieflix.ui.theme.AndroidMovieFlixTheme
import dagger.hilt.android.AndroidEntryPoint

private data class NavTab(val route: String, val icon: String, val label: String)

private val BOTTOM_NAV_TABS = listOf(
    NavTab(Routes.HOME, "⌂", "Home"),
    NavTab(Routes.BROWSE, "▦", "Browse"),
    NavTab(Routes.WATCH_PARTY, "●", "Live"),
    NavTab(Routes.CHAT, "✦", "Flix"),
    NavTab(Routes.PROFILE, "◉", "Me"),
)

private val TOP_LEVEL_ROUTES = BOTTOM_NAV_TABS.map { it.route }.toSet()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMovieFlixTheme {
                val navController = rememberNavController()
                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route

                val showBottomNav = currentRoute in TOP_LEVEL_ROUTES
                val showFab = currentRoute == Routes.HOME || currentRoute == Routes.BROWSE

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        if (showFab) {
                            ChatFab(onClick = { navController.navigate(Routes.CHAT) })
                        }
                    },
                    bottomBar = {
                        if (showBottomNav) {
                            VhsBottomNav(navController = navController, currentRoute = currentRoute)
                        }
                    },
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun VhsBottomNav(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        BOTTOM_NAV_TABS.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != tab.route) {
                        navController.navigate(tab.route) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Box(
                        modifier = if (selected) Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(0.dp),
                            )
                        else Modifier.size(width = 64.dp, height = 32.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center,
                    ) {
                        Text(
                            text = tab.icon,
                            fontSize = 18.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.label.uppercase(),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 10.sp,
                        letterSpacing = 0.14.sp,
                        color = if (selected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        }
    }
}
