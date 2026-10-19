package com.allianz.util

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

object PkceUtil {

    /**
     * Genera un code_verifier criptográficamente seguro (entre 43 y 128 caracteres).
     */
    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

    /**
     * Genera el code_challenge aplicando SHA-256 sobre el code_verifier.
     */
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(StandardCharsets.US_ASCII)
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }
}
