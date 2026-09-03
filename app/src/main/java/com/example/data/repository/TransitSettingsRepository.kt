package com.example.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransitSettingsRepository(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private val _dataSaverEnabled = MutableStateFlow(
        preferences.getBoolean(DATA_SAVER_KEY, false)
    )
    val dataSaverEnabled: StateFlow<Boolean> = _dataSaverEnabled.asStateFlow()

    fun setDataSaverEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(DATA_SAVER_KEY, enabled).apply()
        _dataSaverEnabled.value = enabled
    }

    companion object {
        private const val PREFERENCES = "teranga_moov_settings"
        private const val DATA_SAVER_KEY = "data_saver_enabled"
    }
}
