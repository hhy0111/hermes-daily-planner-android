package com.hermes.studyvault

import androidx.compose.runtime.Composable
import com.hermes.studyvault.navigation.VaultNavHost
import com.hermes.studyvault.ui.theme.HermesStudyVaultTheme

@Composable
fun StudyVaultApp() {
    HermesStudyVaultTheme {
        VaultNavHost()
    }
}
