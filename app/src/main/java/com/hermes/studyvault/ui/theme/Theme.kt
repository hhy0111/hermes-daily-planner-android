package com.hermes.studyvault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = VaultGreen,
    secondary = VaultAmber,
    background = VaultPaper,
    onBackground = VaultInk
)

@Composable
fun HermesStudyVaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightScheme,
        content = content
    )
}
