package com.allianz.ui.auth

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.allianz.data.model.DiscordUser
import com.allianz.ui.dashboard.MusicDashboard

// Color oficial de Discord
val DiscordBlurple = Color(0xFF5865F2)

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (val state = uiState) {
        is AuthUiState.Success -> {
            MusicDashboard(
                user = state.user,
                onLogout = { viewModel.logout() }
            )
        }

        else -> {
            Scaffold(
                modifier = modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (state) {
                        is AuthUiState.Idle -> {
                            IdleStateContent(
                                onLoginClick = {
                                    val authUrl = viewModel.getAuthorizationUrl()
                                    val customTabsIntent = CustomTabsIntent.Builder().build()
                                    customTabsIntent.launchUrl(context, Uri.parse(authUrl))
                                }
                            )
                        }

                        is AuthUiState.Loading -> {
                            LoadingStateContent()
                        }

                        is AuthUiState.Error -> {
                            ErrorStateContent(
                                message = state.message,
                                onRetryClick = { viewModel.retry() }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun IdleStateContent(
    onLoginClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Inicia sesión con tu cuenta de Discord para continuar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = DiscordBlurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Iniciar sesión con Discord",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LoadingStateContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = DiscordBlurple,
            strokeWidth = 4.dp,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Autenticando con Discord...",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}



@Composable
private fun ErrorStateContent(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Error de autenticación",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetryClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DiscordBlurple
            )
        ) {
            Text(text = "Reintentar")
        }
    }
}
