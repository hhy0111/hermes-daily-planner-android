package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity

data class TodayOverview(
    val unreadSources: List<SourceEntity>,
    val dueReviewCards: List<ReviewCardEntity>,
    val upcomingDeadlines: List<DeadlineEntity>
)
