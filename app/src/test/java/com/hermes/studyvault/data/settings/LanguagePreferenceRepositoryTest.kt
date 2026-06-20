package com.hermes.studyvault.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.hermes.studyvault.domain.settings.AppLanguage
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguagePreferenceRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var preferencesFile: File
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        preferencesFile = File.createTempFile("language-settings", ".preferences_pb")
        preferencesFile.delete()
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { preferencesFile },
        )
    }

    @After
    fun tearDown() {
        preferencesFile.delete()
    }

    @Test
    fun selectedLanguageDefaultsToSystemDefault() = testScope.runTest {
        val repository = LanguagePreferenceRepository(dataStore)

        assertEquals(AppLanguage.SystemDefault, repository.selectedLanguage.first())
    }

    @Test
    fun saveLanguagePersistsSelectedLanguage() = testScope.runTest {
        val repository = LanguagePreferenceRepository(dataStore)

        repository.saveLanguage(AppLanguage.Korean)

        assertEquals(AppLanguage.Korean, repository.selectedLanguage.first())
    }
}
