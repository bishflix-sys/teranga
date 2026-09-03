package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.NationalLanguage
import com.example.data.repository.LanguagePreferencesRepository
import com.example.ui.language.LanguageSelectionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LanguageSelectionTest {

    private lateinit var context: Application
    private lateinit var repository: LanguagePreferencesRepository
    private lateinit var viewModel: LanguageSelectionViewModel

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = LanguagePreferencesRepository(context)
        viewModel = LanguageSelectionViewModel(context, repository)
    }

    @Test
    fun testNationalLanguagesCount() {
        // Senegal has 20 national languages + French
        assertEquals(20, NationalLanguage.TWENTY_NATIONAL_LANGUAGES.size)
        assertEquals(21, NationalLanguage.ALL_NATIONAL_LANGUAGES.size)
    }

    @Test
    fun testLanguagePersistence() {
        // Default starts as French or previous
        repository.setLanguage(NationalLanguage.WOLOF)
        assertEquals(NationalLanguage.WOLOF, repository.getSelectedLanguage())

        // Recreate repository from context to verify persistent read from SharedPreferences
        val newRepoInstance = LanguagePreferencesRepository(context)
        assertEquals(NationalLanguage.WOLOF, newRepoInstance.getSelectedLanguage())
    }

    @Test
    fun testToggleNextLanguage() {
        repository.setLanguage(NationalLanguage.FRENCH)
        val next = repository.toggleNextLanguage()
        assertEquals(NationalLanguage.WOLOF, next)
        assertEquals(NationalLanguage.WOLOF, repository.getSelectedLanguage())
    }

    @Test
    fun testLanguageSelectionViewModel() {
        viewModel.selectLanguage(NationalLanguage.PULAAR)
        assertEquals(NationalLanguage.PULAAR, viewModel.selectedLanguage.value)

        // Test search query filtering
        viewModel.setSearchQuery("Sérère")
        val filtered = viewModel.filteredLanguages.value
        assertTrue(filtered.any { it == NationalLanguage.SERERE })
    }

    @Test
    fun testFromCodeLookup() {
        assertEquals(NationalLanguage.WOLOF, NationalLanguage.fromCode("wo"))
        assertEquals(NationalLanguage.PULAAR, NationalLanguage.fromCode("ff"))
        assertEquals(NationalLanguage.MANDINKA, NationalLanguage.fromCode("mn"))
        assertEquals(NationalLanguage.FRENCH, NationalLanguage.fromCode("unknown_code"))
    }
}
