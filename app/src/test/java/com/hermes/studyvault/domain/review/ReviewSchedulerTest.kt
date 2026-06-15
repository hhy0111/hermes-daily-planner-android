package com.hermes.studyvault.domain.review

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class ReviewSchedulerTest {
    private val now = Instant.parse("2026-06-16T00:00:00Z")

    @Test
    fun againSchedulesNextReviewToday() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Again, reviewCount = 2)
        assertEquals(Instant.parse("2026-06-16T00:30:00Z"), result)
    }

    @Test
    fun hardSchedulesNextReviewTomorrow() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Hard, reviewCount = 2)
        assertEquals(Instant.parse("2026-06-17T00:00:00Z"), result)
    }

    @Test
    fun goodSchedulesLongerIntervalsAsReviewCountGrows() {
        val first = ReviewScheduler.nextReviewAt(now, ReviewGrade.Good, reviewCount = 0)
        val fourth = ReviewScheduler.nextReviewAt(now, ReviewGrade.Good, reviewCount = 3)

        assertEquals(Instant.parse("2026-06-19T00:00:00Z"), first)
        assertEquals(Instant.parse("2026-07-06T00:00:00Z"), fourth)
    }

    @Test
    fun easySchedulesOneMonthWhenCardHasHistory() {
        val result = ReviewScheduler.nextReviewAt(now, ReviewGrade.Easy, reviewCount = 5)
        assertEquals(Instant.parse("2026-07-16T00:00:00Z"), result)
    }
}
