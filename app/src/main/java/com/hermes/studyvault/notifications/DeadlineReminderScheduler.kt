package com.hermes.studyvault.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import java.time.Instant
import kotlin.math.absoluteValue

object DeadlineReminderScheduler {
    const val CHANNEL_ID = "deadline_reminders"
    const val EXTRA_DEADLINE_ID = "deadline_id"
    const val EXTRA_DEADLINE_TITLE = "deadline_title"
    const val EXTRA_DUE_AT = "due_at"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Schedule reminders",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Reminders for saved schedules and tasks."
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun canPostNotifications(context: Context): Boolean {
        val permissionGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        return permissionGranted && NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun canScheduleExactAlarms(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
        return context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
    }

    fun exactAlarmSettingsIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
            Uri.parse("package:${context.packageName}"),
        )
    }

    fun schedule(
        context: Context,
        deadline: DeadlineEntity,
        reminderAt: Instant = deadline.dueAt,
        preferExact: Boolean = true,
    ): ScheduleResult {
        if (deadline.completedAt != null || reminderAt.isBefore(Instant.now())) {
            cancel(context, deadline.id)
            return ScheduleResult.SkippedPastOrCompleted
        }

        createNotificationChannel(context)

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = pendingIntent(context, deadline)
        val triggerAtMillis = reminderAt.toEpochMilli()

        if (preferExact && canScheduleExactAlarms(context)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                }
                return ScheduleResult.Exact
            } catch (_: SecurityException) {
                // Fall back below if the special access changed between the check and scheduling.
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
        return ScheduleResult.Inexact
    }

    fun cancel(context: Context, deadlineId: String) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.cancel(pendingIntent(context, deadlineId))
    }

    private fun pendingIntent(context: Context, deadline: DeadlineEntity): PendingIntent {
        val intent = Intent(context, DeadlineReminderReceiver::class.java).apply {
            putExtra(EXTRA_DEADLINE_ID, deadline.id)
            putExtra(EXTRA_DEADLINE_TITLE, deadline.title)
            putExtra(EXTRA_DUE_AT, deadline.dueAt.toString())
        }
        return PendingIntent.getBroadcast(
            context,
            requestCodeFor(deadline.id),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun pendingIntent(context: Context, deadlineId: String): PendingIntent {
        val intent = Intent(context, DeadlineReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            requestCodeFor(deadlineId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun requestCodeFor(deadlineId: String): Int {
        return deadlineId.hashCode().absoluteValue
    }
}

enum class ScheduleResult {
    Exact,
    Inexact,
    SkippedPastOrCompleted,
}
