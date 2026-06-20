package com.hermes.studyvault.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hermes.studyvault.data.local.StudyVaultDatabaseProvider
import java.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_MY_PACKAGE_REPLACED) {
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val now = Instant.now()
                StudyVaultDatabaseProvider.get(context)
                    .deadlineDao()
                    .getAllOnce()
                    .filter { it.completedAt == null && it.dueAt.isAfter(now) }
                    .forEach { deadline ->
                        DeadlineReminderScheduler.schedule(
                            context = context,
                            deadline = deadline,
                            reminderAt = deadline.dueAt,
                            preferExact = true,
                        )
                    }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
