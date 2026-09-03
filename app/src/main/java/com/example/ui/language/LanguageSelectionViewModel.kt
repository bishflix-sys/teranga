package com.example.ui.language

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.NationalLanguage
import com.example.data.repository.LanguagePreferencesRepository
import com.example.ui.util.VoiceAnnouncer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel managing the selection and persistence of Senegal's 20 national languages
 * and French, coordinating with LanguagePreferencesRepository and VoiceAnnouncer.
 */
class LanguageSelectionViewModel @JvmOverloads constructor(
    application: Application,
    val preferencesRepository: LanguagePreferencesRepository = LanguagePreferencesRepository.getInstance(application)
) : AndroidViewModel(application) {

    val voiceAnnouncer = VoiceAnnouncer(application)
    val isSpeaking: StateFlow<Boolean> = voiceAnnouncer.isSpeaking

    // Selected language observed directly from the data layer
    val selectedLanguage: StateFlow<NationalLanguage> = preferencesRepository.selectedLanguage

    // UI state for dropdown menu and bottom sheet
    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded: StateFlow<Boolean> = _isDropdownExpanded.asStateFlow()

    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen: StateFlow<Boolean> = _isSheetOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 20 National Languages + French filtered by search query
    val filteredLanguages: StateFlow<List<NationalLanguage>> = _searchQuery
        .combine(selectedLanguage) { query, _ ->
            if (query.isBlank()) {
                NationalLanguage.ALL_NATIONAL_LANGUAGES
            } else {
                NationalLanguage.ALL_NATIONAL_LANGUAGES.filter {
                    it.displayName.contains(query, ignoreCase = true) ||
                            it.regionOrGroup.contains(query, ignoreCase = true) ||
                            it.greeting.contains(query, ignoreCase = true) ||
                            it.code.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NationalLanguage.ALL_NATIONAL_LANGUAGES
        )

    fun selectLanguage(language: NationalLanguage, announce: Boolean = false) {
        preferencesRepository.setLanguage(language)
        _isDropdownExpanded.value = false
        _isSheetOpen.value = false
        if (announce) {
            announceLanguage(language)
        }
    }

    fun toggleNextLanguage(): NationalLanguage {
        return preferencesRepository.toggleNextLanguage()
    }

    fun openDropdown() {
        _isDropdownExpanded.value = true
    }

    fun closeDropdown() {
        _isDropdownExpanded.value = false
    }

    fun openSheet() {
        _isSheetOpen.value = true
    }

    fun closeSheet() {
        _isSheetOpen.value = false
        _searchQuery.value = ""
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun announceLanguage(language: NationalLanguage) {
        val text = "${language.greeting}. ${language.trafficAlertVocalSummary}"
        voiceAnnouncer.speak(text)
    }

    fun announceCurrentGreeting() {
        val current = selectedLanguage.value
        announceLanguage(current)
    }

    fun stopVoice() {
        voiceAnnouncer.stop()
    }

    override fun onCleared() {
        super.onCleared()
        voiceAnnouncer.shutdown()
    }
}
