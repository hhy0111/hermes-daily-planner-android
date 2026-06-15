package com.hermes.studyvault.domain.review

import java.time.Duration
import java.time.Instant

object ReviewScheduler {
    fun nextReviewAt(now: Instant, grade: ReviewGrade, reviewCount: Int): Instant {
        val safeCount = reviewCount.coerceAtLeast(0)
        val delay = when (grade) {
            ReviewGrade.Again -> Duration.ofMinutes(30)
            ReviewGrade.Hard -> Duration.ofDays(1)
            ReviewGrade.Good -> Duration.ofDays(
                when {
                    safeCount == 0 -> 3
                    safeCount == 1 -> 7
                    safeCount == 2 -> 14
                    else -> 20
                }
            )
            ReviewGrade.Easy -> Duration.ofDays(
                when {
                    safeCount == 0 -> 7
                    safeCount <= 2 -> 14
                    else -> 30
                }
            )
        }
        return now.plus(delay)
    }
}
