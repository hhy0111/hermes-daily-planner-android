package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.repository.VaultRepository
import java.time.Duration
import java.time.Instant

class GetTodayOverviewUseCase(
    private val repository: VaultRepository
) {
    suspend fun get(now: Instant): TodayOverview {
        val deadlineWindowEnd = now.plus(Duration.ofDays(7))
        return TodayOverview(
            unreadSources = repository.getSourcesOnce().filter { it.status == "unread" },
            dueReviewCards = repository.getReviewCardsOnce().filter { !it.nextReviewAt.isAfter(now) },
            upcomingDeadlines = repository.getDeadlinesOnce()
                .filter { it.completedAt == null && !it.dueAt.isBefore(now) && !it.dueAt.isAfter(deadlineWindowEnd) }
                .sortedBy { it.dueAt }
        )
    }
}
