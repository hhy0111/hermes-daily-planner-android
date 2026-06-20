package com.hermes.studyvault.domain.monetization

class AdPlacementPolicy(
    private val adsEnabled: Boolean = false,
) {
    fun shouldShow(placement: AdPlacement): Boolean {
        return adsEnabled && placement in PassivePlacements
    }

    private companion object {
        val PassivePlacements = setOf(
            AdPlacement.TodayAfterOverview,
            AdPlacement.VaultAfterHeader,
            AdPlacement.ReviewAfterHeader,
        )
    }
}
