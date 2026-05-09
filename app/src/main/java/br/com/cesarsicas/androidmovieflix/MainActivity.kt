package br.com.cesarsicas.androidmovieflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.cesarsicas.androidmovieflix.presentation.common.ChatFab
import br.com.cesarsicas.androidmovieflix.presentation.navigation.AppNavHost
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import br.com.cesarsicas.androidmovieflix.ui.theme.AndroidMovieFlixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMovieFlixTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        ChatFab(onClick = { navController.navigate(Routes.CHAT) })
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
