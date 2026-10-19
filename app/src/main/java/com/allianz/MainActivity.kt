package com.allianz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.allianz.ui.auth.AuthScreen
import com.allianz.ui.auth.AuthViewModel
import com.allianz.ui.theme.AllianzTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Captura el Deep Link si la app fue abierta desde el navegador mediante un Intent inicial
        authViewModel.handleIntentUri(intent?.data)

        setContent {
            AllianzTheme {
                AuthScreen(viewModel = authViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Captura el Deep Link cuando la actividad ya está creada (singleTask) y recibe el callback OAuth
        authViewModel.handleIntentUri(intent.data)
    }
}
