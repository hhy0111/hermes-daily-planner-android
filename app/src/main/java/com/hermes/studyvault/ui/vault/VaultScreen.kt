package com.hermes.studyvault.ui.vault

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.data.local.StudyVaultDatabaseProvider
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.domain.planner.PlannerItemFactory
import com.hermes.studyvault.ui.ads.AdBannerSlot
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun VaultScreen(
    adPolicy: AdPlacementPolicy,
    bannerAdUnitId: String,
) {
    val context = LocalContext.current
    val database = remember(context) { StudyVaultDatabaseProvider.get(context) }
    val sources by database.sourceDao().observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    val openTasks = sources.filter { it.status != DoneStatus }.sortedByDescending { it.updatedAt }
    val completedTasks = sources.filter { it.status == DoneStatus }.sortedByDescending { it.updatedAt }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.vault_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.vault_body),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.task_title)) },
            singleLine = true,
        )
        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            label = { Text(stringResource(R.string.task_details)) },
            placeholder = { Text(stringResource(R.string.task_details_hint)) },
            minLines = 3,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            onClick = {
                if (title.isBlank()) {
                    feedback = context.getString(R.string.task_title_required)
                    return@Button
                }
                val task = PlannerItemFactory.createTask(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    details = details,
                    now = Instant.now(),
                )
                scope.launch {
                    withContext(Dispatchers.IO) {
                        database.sourceDao().insert(task)
                    }
                    title = ""
                    details = ""
                    feedback = context.getString(R.string.task_saved)
                }
            },
        ) {
            Text(stringResource(R.string.save_task))
        }

        if (feedback.isNotBlank()) {
            Text(
                text = feedback,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        AdBannerSlot(
            placement = AdPlacement.VaultAfterHeader,
            policy = adPolicy,
            adUnitId = bannerAdUnitId,
            modifier = Modifier.padding(top = 16.dp),
        )

        Text(
            text = stringResource(R.string.open_tasks),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        if (openTasks.isEmpty()) {
            Text(stringResource(R.string.empty_open_tasks))
        } else {
            openTasks.forEach { source ->
                TaskCard(
                    source = source,
                    actionText = stringResource(R.string.mark_done),
                    onAction = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                database.sourceDao().updateStatus(source.id, DoneStatus, Instant.now())
                            }
                        }
                    },
                )
            }
        }

        Text(
            text = stringResource(R.string.completed_tasks),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        if (completedTasks.isEmpty()) {
            Text(stringResource(R.string.empty_completed_tasks))
        } else {
            completedTasks.take(5).forEach { source ->
                TaskCard(
                    source = source,
                    actionText = stringResource(R.string.reopen_task),
                    onAction = {
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
private fun TaskCard(
    source: SourceEntity,
    actionText: String,
    onAction: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
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
            Row(Modifier.padding(top = 10.dp)) {
                OutlinedButton(onClick = onAction) {
                    Text(actionText)
                }
            }
        }
    }
}

private const val DoneStatus = "done"
