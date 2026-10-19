package com.allianz.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    @SerialName("scope")
    val scope: String
)

@Serializable
data class DiscordUser(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("global_name")
    val globalName: String? = null,
    @SerialName("avatar")
    val avatar: String? = null,
    @SerialName("email")
    val email: String? = null
) {
    /**
     * Construye la URL pública del avatar de Discord.
     * Si el usuario tiene avatar personalizado: https://cdn.discordapp.com/avatars/{userId}/{avatarHash}.png
     * Si no tiene avatar: URL por defecto de Discord.
     */
    fun getAvatarUrl(): String? {
        return if (avatar != null) {
            "https://cdn.discordapp.com/avatars/$id/$avatar.png"
        } else {
            // Avatar por defecto de Discord basado en el ID
            val index = (id.toLongOrNull() ?: 0L) % 5
            "https://cdn.discordapp.com/embed/avatars/$index.png"
        }
    }
}
