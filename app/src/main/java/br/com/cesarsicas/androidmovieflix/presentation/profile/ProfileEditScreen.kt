package br.com.cesarsicas.androidmovieflix.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.presentation.common.RequireUserAuth
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState

@Composable
fun ProfileEditScreen(
    navController: NavHostController,
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    RequireUserAuth(navController = navController) {
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.saveSuccess.collect { navController.popBackStack() }
        }

        when (val s = state) {
            is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(s.message, modifier = Modifier.padding(16.dp))
            }
            is UiState.Success -> ProfileEditForm(
                user = s.data,
                onSave = { name, avatarUrl -> viewModel.save(name, avatarUrl) },
            )
        }
    }
}

@Composable
private fun ProfileEditForm(
    user: UserModel,
    onSave: (name: String, avatarUrl: String?) -> Unit,
) {
    var name by remember { mutableStateOf(user.name) }
    var avatarUrl by remember { mutableStateOf(user.avatarUrl.orEmpty()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = avatarUrl,
            onValueChange = { avatarUrl = it },
            label = { Text("Avatar URL (optional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { onSave(name, avatarUrl.ifBlank { null }) },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
    }
}
