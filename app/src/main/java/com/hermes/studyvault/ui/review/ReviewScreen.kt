package com.hermes.studyvault.ui.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.data.local.StudyVaultDatabaseProvider
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.ui.ads.AdBannerSlot
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReviewScreen(
    adPolicy: AdPlacementPolicy,
    bannerAdUnitId: String,
) {
    val context = LocalContext.current
    val database = remember(context) { StudyVaultDatabaseProvider.get(context) }
    val sources by database.sourceDao().observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val completedTasks = sources.filter { it.status == DoneStatus }.sortedByDescending { it.updatedAt }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.review_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.review_body),
            modifier = Modifier.padding(top = 8.dp),
        )
        AdBannerSlot(
            placement = AdPlacement.ReviewAfterHeader,
            policy = adPolicy,
            adUnitId = bannerAdUnitId,
            modifier = Modifier.padding(top = 16.dp),
        )

        if (completedTasks.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_completed_tasks),
                modifier = Modifier.padding(top = 20.dp),
            )
        } else {
            completedTasks.forEach { source ->
                CompletedTaskCard(
                    source = source,
                    onReopen = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                database.sourceDao().updateStatus(source.id, "unread", Instant.now())
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CompletedTaskCard(
    source: SourceEntity,
    onReopen: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(source.title, style = MaterialTheme.typography.titleSmall)
            if (!source.capturedText.isNullOrBlank()) {
                Text(
                    text = source.capturedText,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            OutlinedButton(
                onClick = onReopen,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                Text(stringResource(R.string.reopen_task))
            }
        }
    }
}

private const val DoneStatus = "done"
