package com.hermes.studyvault.domain.monetization

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdPlacementPolicyTest {
    @Test
    fun defaultPolicyHidesEveryPlacement() {
        val policy = AdPlacementPolicy()

        AdPlacement.entries.forEach { placement ->
            assertFalse(policy.shouldShow(placement))
        }
    }

    @Test
    fun enabledPolicyAllowsOnlyPassiveContentPlacements() {
        val policy = AdPlacementPolicy(adsEnabled = true)

        assertTrue(policy.shouldShow(AdPlacement.TodayAfterOverview))
        assertTrue(policy.shouldShow(AdPlacement.VaultAfterHeader))
        assertTrue(policy.shouldShow(AdPlacement.ReviewAfterHeader))
        assertFalse(policy.shouldShow(AdPlacement.CalendarScheduleForm))
        assertFalse(policy.shouldShow(AdPlacement.WriteEditor))
        assertFalse(policy.shouldShow(AdPlacement.Settings))
    }
}
