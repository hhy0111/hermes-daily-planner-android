package com.hermes.studyvault.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.notifications.DeadlineReminderScheduler
import com.hermes.studyvault.notifications.ScheduleResult
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    val database = remember(context) { StudyVaultDatabaseProvider.get(context) }
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var time by remember { mutableStateOf("09:00") }
    var selectedReminder by remember { mutableStateOf(ReminderOption.AtTime) }
    var feedback by remember { mutableStateOf("") }
    var deadlines by remember { mutableStateOf<List<DeadlineEntity>>(emptyList()) }
    var refreshKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(refreshKey) {
        deadlines = withContext(Dispatchers.IO) {
            database.deadlineDao().getAllOnce()
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.calendar_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.calendar_body),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.schedule_title)) },
            singleLine = true,
        )
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            label = { Text(stringResource(R.string.schedule_date_hint)) },
            singleLine = true,
        )
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            label = { Text(stringResource(R.string.schedule_time_hint)) },
            singleLine = true,
        )

        Text(
            text = stringResource(R.string.schedule_reminder),
            modifier = Modifier.padding(top = 18.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        ReminderOption.entries.forEach { option ->
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                onClick = { selectedReminder = option },
                enabled = selectedReminder != option,
            ) {
                Text(stringResource(option.labelRes))
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            onClick = {
                scope.launch {
                    val parsed = parseSchedule(date, time)
                    if (title.isBlank() || parsed == null) {
                        feedback = context.getString(R.string.schedule_invalid)
                        return@launch
                    }
                    val dueAt = parsed
                    val deadline = DeadlineEntity(
                        id = UUID.randomUUID().toString(),
                        title = title.trim(),
                        dueAt = dueAt,
                        type = "schedule",
                        priority = 0,
                        linkedSourceIdsCsv = "",
                        linkedNoteIdsCsv = "",
                        linkedEvidenceBlockIdsCsv = "",
                        completedAt = null,
                    )
                    val reminderAt = selectedReminder.reminderAt(dueAt)
                    val result = withContext(Dispatchers.IO) {
                        database.deadlineDao().insert(deadline)
                        reminderAt?.let {
                            DeadlineReminderScheduler.schedule(context, deadline, it, preferExact = true)
                        }
                    }
                    feedback = when (result) {
                        ScheduleResult.Exact -> context.getString(R.string.schedule_saved_exact)
                        ScheduleResult.Inexact -> context.getString(R.string.schedule_saved_inexact)
                        ScheduleResult.SkippedPastOrCompleted -> context.getString(R.string.schedule_saved_past)
                        null -> context.getString(R.string.schedule_saved_no_reminder)
                    }
                    title = ""
                    refreshKey += 1
                }
            },
        ) {
            Text(stringResource(R.string.save_schedule))
        }

        if (feedback.isNotBlank()) {
            Text(
                text = feedback,
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = stringResource(R.string.saved_schedules),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        deadlines.take(6).forEach { deadline ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Column(Modifier.weight(1f)) {
                    Text(deadline.title, style = MaterialTheme.typography.titleSmall)
                    Text(deadline.dueAt.toString(), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.padding(4.dp))
            }
        }
        if (deadlines.isEmpty()) {
            Text(stringResource(R.string.empty_schedules))
        }
    }
}

private fun parseSchedule(date: String, time: String) = try {
    val localDate = LocalDate.parse(date)
    val localTime = LocalTime.parse(time)
    localDate.atTime(localTime).atZone(ZoneId.systemDefault()).toInstant()
} catch (_: DateTimeParseException) {
    null
}

private enum class ReminderOption(
    val minutesBefore: Long?,
    val labelRes: Int,
) {
    None(null, R.string.reminder_none),
    AtTime(0, R.string.reminder_at_time),
    TenMinutes(10, R.string.reminder_10),
    ThirtyMinutes(30, R.string.reminder_30),
    OneHour(60, R.string.reminder_60);

    fun reminderAt(dueAt: java.time.Instant): java.time.Instant? {
        return minutesBefore?.let { dueAt.minus(Duration.ofMinutes(it)) }
    }
}
