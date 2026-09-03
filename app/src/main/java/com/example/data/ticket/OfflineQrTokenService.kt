package com.example.data.ticket

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** Encrypts locally verifiable ticket payloads without requiring network access. */
class OfflineQrTokenService(context: Context) {
    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
    private val secretKey: SecretKey = getOrCreateKey()
    private val consumedTokens = context.getSharedPreferences(CONSUMED_PREFS, Context.MODE_PRIVATE)

    fun generate(ticketNumber: String, fareCfa: Int, issuedAt: Long = System.currentTimeMillis()): String {
        val expiresAt = issuedAt + VALIDITY_MS
        val payload = JSONObject()
            .put("ticket", ticketNumber)
            .put("fareCfa", fareCfa)
            .put("issuedAt", issuedAt)
            .put("expiresAt", expiresAt)
            .toString()
        val cipher = Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, secretKey) }
        val encrypted = cipher.doFinal(payload.toByteArray(StandardCharsets.UTF_8))
        val packed = ByteArray(cipher.iv.size + encrypted.size)
        cipher.iv.copyInto(packed)
        encrypted.copyInto(packed, cipher.iv.size)
        return PREFIX + Base64.encodeToString(packed, Base64.NO_WRAP)
    }

    fun isValid(token: String, now: Long = System.currentTimeMillis()): Boolean {
        return runCatching {
            val payload = decrypt(token)
            now in payload.getLong("issuedAt")..payload.getLong("expiresAt") &&
                payload.getString("ticket").isNotBlank()
        }.getOrDefault(false)
    }

    @Synchronized
    fun consume(token: String, now: Long = System.currentTimeMillis()): Boolean {
        if (!isValid(token, now)) return false
        val tokenId = tokenHash(token)
        if (consumedTokens.getBoolean(tokenId, false)) return false
        consumedTokens.edit().putBoolean(tokenId, true).apply()
        return true
    }

    private fun decrypt(token: String): JSONObject {
        require(token.startsWith(PREFIX))
        val packed = Base64.decode(token.removePrefix(PREFIX), Base64.NO_WRAP)
        require(packed.size > IV_LENGTH)
        val iv = packed.copyOfRange(0, IV_LENGTH)
        val encrypted = packed.copyOfRange(IV_LENGTH, packed.size)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(TAG_BITS, iv))
        }
        return JSONObject(String(cipher.doFinal(encrypted), StandardCharsets.UTF_8))
    }

    private fun tokenHash(token: String): String = MessageDigest
        .getInstance("SHA-256")
        .digest(token.toByteArray(StandardCharsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private fun getOrCreateKey(): SecretKey {
        (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "teranga_moov_offline_qr"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val TAG_BITS = 128
        private const val IV_LENGTH = 12
        private const val VALIDITY_MS = 15 * 60 * 1000L
        private const val PREFIX = "TM1:"
        private const val CONSUMED_PREFS = "teranga_moov_consumed_qr"
    }
}
