package com.hermes.studyvault.ui.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.data.local.StudyVaultDatabaseProvider
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.ui.ads.AdBannerSlot
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TodayScreen(
    adPolicy: AdPlacementPolicy,
    bannerAdUnitId: String,
) {
    val context = LocalContext.current
    val database = remember(context) { StudyVaultDatabaseProvider.get(context) }
    val sources by database.sourceDao().observeAll().collectAsState(initial = emptyList())
    val notes by database.noteDao().observeAll().collectAsState(initial = emptyList())
    val deadlines by database.deadlineDao().observeAll().collectAsState(initial = emptyList())

    val openTasks = sources.filter { it.status != DoneStatus }.sortedByDescending { it.updatedAt }
    val completedTasks = sources.filter { it.status == DoneStatus }
    val activeSchedules = deadlines.filter { it.completedAt == null }.sortedBy { it.dueAt }
    val recentNotes = notes.sortedByDescending { it.updatedAt }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.today_focus_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.today_focus_body),
            modifier = Modifier.padding(top = 8.dp),
        )

        Text(
            text = stringResource(R.string.today_overview),
            modifier = Modifier.padding(top = 22.dp, bottom = 10.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                label = stringResource(R.string.today_open_tasks),
                value = openTasks.size,
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = stringResource(R.string.today_schedules),
                value = activeSchedules.size,
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = stringResource(R.string.today_notes),
                value = notes.size,
                modifier = Modifier.weight(1f),
            )
        }

        Text(
            text = stringResource(R.string.today_focus_list),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        if (openTasks.isEmpty() && activeSchedules.isEmpty()) {
            Text(stringResource(R.string.today_no_focus))
        } else {
            openTasks.take(3).forEach { source ->
                SourceSummaryCard(source = source)
            }
            activeSchedules.take(2).forEach { deadline ->
                DeadlineSummaryCard(deadline = deadline)
            }
        }

        if (recentNotes.isNotEmpty()) {
            Text(
                text = stringResource(R.string.today_recent_notes),
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            recentNotes.take(2).forEach { note ->
                NoteSummaryCard(note = note)
            }
        }

        AdBannerSlot(
            placement = AdPlacement.TodayAfterOverview,
            policy = adPolicy,
            adUnitId = bannerAdUnitId,
            modifier = Modifier.padding(top = 16.dp),
        )

        if (completedTasks.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "${completedTasks.size} ${stringResource(R.string.completed_tasks)}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(value.toString(), style = MaterialTheme.typography.headlineSmall)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SourceSummaryCard(source: SourceEntity) {
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
        }
    }
}

@Composable
private fun DeadlineSummaryCard(deadline: DeadlineEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(deadline.title, style = MaterialTheme.typography.titleSmall)
            Text(
                text = formatDeadline(deadline),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun NoteSummaryCard(note: NoteEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleSmall)
            Text(
                text = note.bodyBlocksJson.toPlainNoteText(),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun formatDeadline(deadline: DeadlineEntity): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm").withZone(ZoneId.systemDefault())
    return formatter.format(deadline.dueAt)
}

private fun String.toPlainNoteText(): String {
    return replace("[", "")
        .replace("]", "")
        .replace("{", "")
        .replace("}", "")
        .replace("\"type\":\"paragraph\",", "")
        .replace("\"text\":\"", "")
        .replace("\"", "")
}

private const val DoneStatus = "done"
