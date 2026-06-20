package com.hermes.studyvault.ui.settings

import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.hermes.studyvault.domain.settings.AppLanguage
import java.util.Locale

@Composable
fun LanguageContext(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    val baseContext = LocalContext.current
    val baseConfiguration = LocalConfiguration.current
    val localizedConfiguration = remember(baseConfiguration, language) {
        Configuration(baseConfiguration).apply {
            if (language != AppLanguage.SystemDefault) {
                val locale = Locale.forLanguageTag(language.localeTag)
                setLocales(LocaleList(locale))
            }
        }
    }
    val localizedContext = remember(baseContext, localizedConfiguration) {
        baseContext.createConfigurationContext(localizedConfiguration)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedConfiguration,
        content = content,
    )
}
