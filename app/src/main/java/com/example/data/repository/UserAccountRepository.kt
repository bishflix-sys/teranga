package com.example.data.repository

import android.content.Context
import android.net.Uri
import java.security.MessageDigest

class UserAccountRepository(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    val hasAccount: Boolean
        get() = preferences.contains(KEY_IDENTIFIER)

    val isLoggedIn: Boolean
        get() = preferences.getBoolean(KEY_SESSION, false)

    val identifier: String
        get() = preferences.getString(KEY_IDENTIFIER, "") ?: ""

    val displayName: String
        get() = preferences.getString(KEY_DISPLAY_NAME, "") ?: ""

    val profilePhoto: Uri?
        get() = preferences.getString(KEY_PROFILE_PHOTO, null)?.let(Uri::parse)

    val hasPin: Boolean
        get() = preferences.contains(KEY_PIN_HASH)

    fun createAccount(name: String, identifier: String, password: String): Boolean {
        if (name.isBlank() || identifier.isBlank() || password.length < 6 || hasAccount) return false
        preferences.edit()
            .putString(KEY_DISPLAY_NAME, name.trim())
            .putString(KEY_IDENTIFIER, identifier.trim())
            .putString(KEY_PASSWORD_HASH, hash(password))
            .putBoolean(KEY_SESSION, true)
            .apply()
        return true
    }

    fun login(identifier: String, password: String): Boolean {
        val valid = identifier.trim() == this.identifier && hash(password) == preferences.getString(KEY_PASSWORD_HASH, "")
        if (valid) preferences.edit().putBoolean(KEY_SESSION, true).apply()
        return valid
    }

    fun logout() {
        preferences.edit().putBoolean(KEY_SESSION, false).apply()
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

    private companion object {
        const val PREFERENCES = "teranga_user_account"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_DISPLAY_NAME = "display_name"
        const val KEY_PASSWORD_HASH = "password_hash"
        const val KEY_SESSION = "session"
        const val KEY_PROFILE_PHOTO = "profile_photo"
        const val KEY_PIN_HASH = "pin_hash"
    }
}