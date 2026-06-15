package com.hermes.studyvault.ui.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Review Queue", style = MaterialTheme.typography.headlineMedium)
        Text("Study due cards and deadline-linked material.")
    }
}
