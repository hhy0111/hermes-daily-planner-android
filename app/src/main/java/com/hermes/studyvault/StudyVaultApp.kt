package com.hermes.studyvault

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.data.settings.LanguagePreferenceRepository
import com.hermes.studyvault.data.settings.languageDataStore
import com.hermes.studyvault.domain.settings.AppLanguage
import com.hermes.studyvault.navigation.VaultNavHost
import com.hermes.studyvault.ui.settings.LanguageContext
import com.hermes.studyvault.ui.theme.HermesStudyVaultTheme
import kotlinx.coroutines.launch

@Composable
fun StudyVaultApp(
    languageRepository: LanguagePreferenceRepository? = null,
    adsEnabled: Boolean = false,
    bannerAdUnitId: String = "",
) {
    val context = LocalContext.current
    val repository = remember(languageRepository, context) {
        languageRepository ?: LanguagePreferenceRepository(context.applicationContext.languageDataStore)
    }
    val selectedLanguage by repository.selectedLanguage.collectAsState(initial = AppLanguage.SystemDefault)
    val adPolicy = remember(adsEnabled) {
        AdPlacementPolicy(adsEnabled = adsEnabled)
    }
    val scope = rememberCoroutineScope()

    HermesStudyVaultTheme {
        LanguageContext(language = selectedLanguage) {
            VaultNavHost(
                selectedLanguage = selectedLanguage,
                adPolicy = adPolicy,
                bannerAdUnitId = bannerAdUnitId,
                onLanguageSelected = { language ->
                    scope.launch {
                        repository.saveLanguage(language)
                    }
                },
            )
        }
    }
}
