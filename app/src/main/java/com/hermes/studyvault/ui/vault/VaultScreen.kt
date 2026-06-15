package com.hermes.studyvault.ui.vault

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VaultScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Knowledge Vault", style = MaterialTheme.typography.headlineMedium)
        Text("Search sources, evidence, notes, tags, and collections.")
    }
}
