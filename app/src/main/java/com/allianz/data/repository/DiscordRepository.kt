package com.allianz.data.repository

import com.allianz.data.model.DiscordTokenResponse
import com.allianz.data.model.DiscordUser
import com.allianz.data.remote.DiscordAuthConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class DiscordRepository(
    private val client: HttpClient = createHttpClient()
) {

    /**
     * Obtiene la URL de autorización para el inicio de sesión OAuth2 con PKCE.
     */
    fun getAuthorizationUrl(codeChallenge: String): String {
        return DiscordAuthConfig.buildAuthorizationUrl(codeChallenge)
    }

    /**
     * Intercambia el código de autorización recibido por el token de acceso en Discord enviando el code_verifier.
     */
    suspend fun exchangeCodeForToken(code: String, codeVerifier: String): DiscordTokenResponse {
        return client.submitForm(
            url = DiscordAuthConfig.TOKEN_URL,
            formParameters = parameters {
                append("client_id", DiscordAuthConfig.CLIENT_ID)
                append("client_secret", DiscordAuthConfig.CLIENT_SECRET)
                append("grant_type", "authorization_code")
                append("code", code)
                append("redirect_uri", DiscordAuthConfig.REDIRECT_URI)
                append("code_verifier", codeVerifier)
            }
        ).body()
    }

    /**
     * Consulta la información del perfil del usuario en Discord (`/users/@me`).
     */
    suspend fun getUserProfile(accessToken: String): DiscordUser {
        return client.get(DiscordAuthConfig.USER_PROFILE_URL) {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    companion object {
        fun createHttpClient(): HttpClient {
            return HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    })
                }
            }
        }
    }
}
