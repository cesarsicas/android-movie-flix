package br.com.cesarsicas.androidmovieflix.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun AuthScreen(
    mode: String,
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var isSignup by remember { mutableStateOf(mode == "signup") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loginSuccess.collect {
            navController.navigate(Routes.PROFILE) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // VHS tape logo
        VhsTapeLogo()

        Spacer(Modifier.height(14.dp))

        // REACTFLIX heading
        Text(
            text = "REACT",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            letterSpacing = 0.04.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        // CRT subtitle
        Text(
            text = if (isSignup) "◉ CREATE ACCOUNT · CH 01" else "◉ MEMBER LOGIN · CH 01",
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            letterSpacing = 0.15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp, bottom = 36.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Name field (signup only)
            if (isSignup) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            "NAME",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            letterSpacing = 0.18.sp,
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(0.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "EMAIL",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.18.sp,
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "PASSWORD",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.18.sp,
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(0.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            if (!isSignup) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {}) {
                        Text(
                            "FORGOT?",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.15.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Primary action button
            Button(
                onClick = {
                    viewModel.clearError()
                    if (isSignup) viewModel.signup(name, email, password)
                    else viewModel.login(email, password)
                },
                enabled = !state.isLoading,
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = if (state.isLoading) "SUBMITTING…"
                    else if (isSignup) "▶ SIGN UP"
                    else "▶ SIGN IN",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    letterSpacing = 0.15.sp,
                )
            }

            // OR divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Text(
                    "OR",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 0.18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }

            // Google placeholder
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(0.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    "G   CONTINUE WITH GOOGLE",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    letterSpacing = 0.15.sp,
                )
            }

            // Toggle link
            TextButton(
                onClick = {
                    isSignup = !isSignup
                    viewModel.clearError()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (isSignup) "Already have an account? SIGN IN"
                    else "No account? SIGN UP",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun VhsTapeLogo() {
    Box(
        modifier = Modifier
            .border(2.5.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(0.dp))
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VhsReel()
            Text(
                "FLIX",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.04.sp,
            )
            VhsReel()
        }
    }
}

@Composable
private fun VhsReel() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
            .padding(8.dp),
    ) {
        Text(
            "◎",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
