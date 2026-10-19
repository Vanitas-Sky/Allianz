package com.allianz.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allianz.data.repository.DiscordRepository
import com.allianz.util.PkceUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: DiscordRepository = DiscordRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Variable para almacenar temporalmente el code_verifier generado durante el flujo OAuth PKCE
    private var currentCodeVerifier: String? = null

    /**
     * Genera el PKCE verifier y challenge, guarda el verifier y retorna la URL de autorización para la Custom Tab.
     */
    fun getAuthorizationUrl(): String {
        val verifier = PkceUtil.generateCodeVerifier()
        currentCodeVerifier = verifier
        val challenge = PkceUtil.generateCodeChallenge(verifier)
        return repository.getAuthorizationUrl(challenge)
    }

    /**
     * Procesa la URI recibida a través del Intent/Deep Link (miapp://oauth/discord?code=...)
     */
    fun handleIntentUri(uri: Uri?) {
        if (uri == null) return

        val code = uri.getQueryParameter("code")
        val error = uri.getQueryParameter("error")
        val errorDescription = uri.getQueryParameter("error_description")

        if (code != null) {
            exchangeCodeAndFetchProfile(code)
        } else if (error != null) {
            _uiState.value = AuthUiState.Error(
                errorDescription ?: "Autorización cancelada o denegada por el usuario ($error)."
            )
        }
    }

    /**
     * Intercambia el código por el token de acceso enviando el code_verifier PKCE y consulta el perfil del usuario.
     */
    private fun exchangeCodeAndFetchProfile(code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val verifier = currentCodeVerifier ?: throw IllegalStateException("No se encontró el code_verifier de PKCE.")
                val tokenResponse = repository.exchangeCodeForToken(code, verifier)
                val user = repository.getUserProfile(tokenResponse.accessToken)
                _uiState.value = AuthUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(
                    e.localizedMessage ?: "Ocurrió un error al autenticar con Discord."
                )
            }
        }
    }

    /**
     * Reintenta el proceso de autenticación limpiando el estado de error.
     */
    fun retry() {
        _uiState.value = AuthUiState.Idle
    }

    /**
     * Cierra la sesión y regresa al estado inicial.
     */
    fun logout() {
        _uiState.value = AuthUiState.Idle
    }
}
