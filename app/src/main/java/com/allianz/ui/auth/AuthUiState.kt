package com.allianz.ui.auth

import com.allianz.data.model.DiscordUser

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: DiscordUser) : AuthUiState
    data class Error(val message: String) : AuthUiState
}
