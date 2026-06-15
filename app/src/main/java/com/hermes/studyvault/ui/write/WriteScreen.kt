package com.hermes.studyvault.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WriteScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Write Note", style = MaterialTheme.typography.headlineMedium)
        Text("Draft notes and insert evidence blocks.")
    }
}
