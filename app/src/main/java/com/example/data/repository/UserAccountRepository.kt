package com.example.data.repository

import android.content.Context
import android.net.Uri
import com.example.BuildConfig
import com.example.data.remote.TerangaApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserAccountRepository(
    context: Context,
    private val apiClient: TerangaApiClient = TerangaApiClient(BuildConfig.TERANGA_API_URL)
) {
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    val hasAccount: Boolean
        get() = preferences.contains(KEY_IDENTIFIER)

    val isLoggedIn: Boolean
        get() = preferences.getBoolean(KEY_SESSION, false) && !authToken.isNullOrBlank()

    private val authToken: String?
        get() = preferences.getString(KEY_AUTH_TOKEN, null)

    val identifier: String
        get() = preferences.getString(KEY_IDENTIFIER, "") ?: ""

    val displayName: String
        get() = preferences.getString(KEY_DISPLAY_NAME, "") ?: ""

    val profilePhoto: Uri?
        get() = preferences.getString(KEY_PROFILE_PHOTO, null)?.let(Uri::parse)

    val hasPin: Boolean
        get() = preferences.contains(KEY_PIN_HASH)

    suspend fun createAccount(name: String, identifier: String, password: String): Boolean = withContext(Dispatchers.IO) {
        if (name.isBlank() || !isEmail(identifier) || password.length < 12) return@withContext false
        val response = apiClient.register(identifier.trim(), password)
        saveSession(name, identifier, password, response.token)
        true
    }

    suspend fun login(identifier: String, password: String): Boolean = withContext(Dispatchers.IO) {
        if (!isEmail(identifier) || password.isBlank()) return@withContext false
        val response = apiClient.login(identifier.trim(), password)
        saveSession(displayName.ifBlank { response.user.email.substringBefore('@') }, identifier, password, response.token)
        true
    }

    fun logout() {
        preferences.edit().remove(KEY_AUTH_TOKEN).putBoolean(KEY_SESSION, false).apply()
    }

    fun updatePassword(currentPassword: String, newPassword: String): Boolean {
        val currentHash = preferences.getString(KEY_PASSWORD_HASH, "")
        if (hash(currentPassword) != currentHash || newPassword.length < 6) return false
        preferences.edit().putString(KEY_PASSWORD_HASH, hash(newPassword)).apply()
        return true
    }

    fun setPin(pin: String): Boolean {
        if (!pin.matches(Regex("\\d{4}"))) return false
        preferences.edit().putString(KEY_PIN_HASH, hash(pin)).apply()
        return true
    }

    fun updateProfilePhoto(uri: Uri?) {
        preferences.edit().putString(KEY_PROFILE_PHOTO, uri?.toString()).apply()
    }

    private fun hash(value: String): String = MessageDigest
        .getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { byte -> "%02x".format(byte) }

    private fun saveSession(name: String, identifier: String, password: String, token: String) {
        preferences.edit()
            .putString(KEY_DISPLAY_NAME, name.trim())
            .putString(KEY_IDENTIFIER, identifier.trim().lowercase())
            .putString(KEY_PASSWORD_HASH, hash(password))
            .putString(KEY_AUTH_TOKEN, token)
            .putBoolean(KEY_SESSION, true)
            .apply()
    }

    private fun isEmail(value: String): Boolean = Regex("^\\S+@\\S+\\.\\S+$").matches(value.trim())

    private companion object {
        const val PREFERENCES = "teranga_user_account"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_DISPLAY_NAME = "display_name"
        const val KEY_PASSWORD_HASH = "password_hash"
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_SESSION = "session"
        const val KEY_PROFILE_PHOTO = "profile_photo"
        const val KEY_PIN_HASH = "pin_hash"
    }
}