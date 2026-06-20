package com.hermes.studyvault.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hermes.studyvault.MainActivity
import com.hermes.studyvault.R

class DeadlineReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        DeadlineReminderScheduler.createNotificationChannel(context)
        if (!DeadlineReminderScheduler.canPostNotifications(context)) {
            return
        }

        val deadlineId = intent.getStringExtra(DeadlineReminderScheduler.EXTRA_DEADLINE_ID) ?: return
        val title = intent.getStringExtra(DeadlineReminderScheduler.EXTRA_DEADLINE_TITLE)
            ?: context.getString(R.string.reminder_notification_title)
        val dueAt = intent.getStringExtra(DeadlineReminderScheduler.EXTRA_DUE_AT).orEmpty()

        val openAppIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, DeadlineReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.reminder_notification_body, dueAt))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openAppIntent)
            .build()

        NotificationManagerCompat.from(context).notify(deadlineId.hashCode(), notification)
    }
}
