package com.hermes.studyvault.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Today Focus", style = MaterialTheme.typography.headlineMedium)
        Text("Reviews, readings, deadlines, and recent captures.")
    }
}
