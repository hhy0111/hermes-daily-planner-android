package com.hermes.studyvault.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R

@Composable
fun WriteScreen() {
    Column(Modifier.padding(16.dp)) {
        Text(stringResource(R.string.write_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.write_body))
    }
}
