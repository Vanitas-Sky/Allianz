package com.allianz.data.remote

import android.net.Uri

object DiscordAuthConfig {
    // =========================================================================
    // CONFIGURA AQUÍ TUS CREDENCIALES DEL DISCORD DEVELOPER PORTAL:
    // https://discord.com/developers/applications
    // =========================================================================
    const val CLIENT_ID = "1561802384827482173"
    const val CLIENT_SECRET = "1df_hJifKnbj4qnHwBijiuv2YNfvgQZN"
    const val REDIRECT_URI = "miapp://oauth/discord"
    const val SCOPE = "identify email"

    const val AUTHORIZATION_BASE_URL = "https://discord.com/oauth2/authorize"
    const val TOKEN_URL = "https://discord.com/api/oauth2/token"
    const val USER_PROFILE_URL = "https://discord.com/api/users/@me"

    /**
     * Construye la URL completa de autorización de Discord con PKCE para lanzar en la Custom Tab.
     */
    fun buildAuthorizationUrl(codeChallenge: String): String {
        return Uri.parse(AUTHORIZATION_BASE_URL).buildUpon()
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", SCOPE)
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .build()
            .toString()
    }
}
