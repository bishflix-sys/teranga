package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.NationalLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Data layer repository managing persistent user preferences for Senegal's national languages.
 * Backed by SharedPreferences for instantaneous read on startup and persistent offline storage.
 */
class LanguagePreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _selectedLanguage = MutableStateFlow(loadInitialLanguage())
    val selectedLanguage: StateFlow<NationalLanguage> = _selectedLanguage.asStateFlow()

    private fun loadInitialLanguage(): NationalLanguage {
        val savedCode = prefs.getString(KEY_SELECTED_LANGUAGE, NationalLanguage.FRENCH.code)
        return NationalLanguage.fromCode(savedCode)
    }

    fun setLanguage(language: NationalLanguage) {
        prefs.edit()
            .putString(KEY_SELECTED_LANGUAGE, language.code)
            .apply()
        _selectedLanguage.value = language
    }

    fun getSelectedLanguage(): NationalLanguage {
        return _selectedLanguage.value
    }

    /**
     * Cycles to the next national language among Senegal's 20 national languages + French
     */
    fun toggleNextLanguage(): NationalLanguage {
        val all = NationalLanguage.ALL_NATIONAL_LANGUAGES
        val currentIndex = all.indexOf(_selectedLanguage.value)
        val nextIndex = if (currentIndex < 0 || currentIndex == all.lastIndex) 0 else currentIndex + 1
        val nextLang = all[nextIndex]
        setLanguage(nextLang)
        return nextLang
    }

    companion object {
        private const val PREFS_NAME = "teranga_moov_language_prefs"
        private const val KEY_SELECTED_LANGUAGE = "selected_language_code"

        @Volatile
        private var INSTANCE: LanguagePreferencesRepository? = null

        fun getInstance(context: Context): LanguagePreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LanguagePreferencesRepository(context).also { INSTANCE = it }
            }
        }
    }
}
