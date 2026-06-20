package com.hermes.studyvault.ui.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.domain.settings.AppLanguage
import com.hermes.studyvault.notifications.DeadlineReminderScheduler

@Composable
fun SettingsScreen(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.settings_body),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(R.string.language),
            style = MaterialTheme.typography.titleMedium,
        )

        AppLanguage.entries.forEach { language ->
            LanguageRow(
                language = language,
                selected = selectedLanguage == language,
                onClick = { onLanguageSelected(language) },
            )
        }

        ReminderPermissionSection()
    }
}

@Composable
private fun ReminderPermissionSection() {
    val context = LocalContext.current
    var refreshKey by remember { mutableIntStateOf(0) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        refreshKey += 1
    }

    val canPostNotifications = remember(refreshKey) {
        DeadlineReminderScheduler.canPostNotifications(context)
    }
    val canScheduleExactAlarms = remember(refreshKey) {
        DeadlineReminderScheduler.canScheduleExactAlarms(context)
    }

    Text(
        text = stringResource(R.string.reminders_title),
        modifier = Modifier.padding(top = 28.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = stringResource(R.string.reminders_body),
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
        style = MaterialTheme.typography.bodyMedium,
    )
    Text(
        text = if (canPostNotifications) {
            stringResource(R.string.notification_permission_on)
        } else {
            stringResource(R.string.notification_permission_off)
        },
        style = MaterialTheme.typography.bodyMedium,
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag("notification_permission_button"),
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                refreshKey += 1
            }
        },
    ) {
        Text(stringResource(R.string.enable_notifications))
    }

    Text(
        text = if (canScheduleExactAlarms) {
            stringResource(R.string.exact_alarm_permission_on)
        } else {
            stringResource(R.string.exact_alarm_permission_off)
        },
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.bodyMedium,
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag("exact_alarm_permission_button"),
        onClick = {
            runCatching {
                context.startActivity(DeadlineReminderScheduler.exactAlarmSettingsIntent(context))
            }
            refreshKey += 1
        },
    ) {
        Text(stringResource(R.string.enable_exact_alarms))
    }
    Text(
        text = stringResource(R.string.exact_alarm_fallback),
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun LanguageRow(
    language: AppLanguage,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("language_${language.code}")
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(
            text = if (language == AppLanguage.SystemDefault) {
                stringResource(R.string.system_default)
            } else {
                language.displayName
            },
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
