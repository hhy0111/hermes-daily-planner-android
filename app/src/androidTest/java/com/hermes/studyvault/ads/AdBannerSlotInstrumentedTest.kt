package com.hermes.studyvault.ads

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.ui.ads.AdBannerSlot
import com.hermes.studyvault.ui.theme.HermesStudyVaultTheme
import org.junit.Rule
import org.junit.Test

class AdBannerSlotInstrumentedTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun hidesAllowedPlacementWhenAdsAreDisabled() {
        composeRule.setContent {
            HermesStudyVaultTheme {
                AdBannerSlot(
                    placement = AdPlacement.TodayAfterOverview,
                    policy = AdPlacementPolicy(),
                    adUnitId = TestBannerAdUnitId,
                )
            }
        }

        composeRule.onNodeWithTag("ad_slot_TodayAfterOverview").assertDoesNotExist()
    }

    @Test
    fun showsAllowedPlacementWhenAdsAreEnabled() {
        composeRule.setContent {
            HermesStudyVaultTheme {
                AdBannerSlot(
                    placement = AdPlacement.TodayAfterOverview,
                    policy = AdPlacementPolicy(adsEnabled = true),
                    adUnitId = TestBannerAdUnitId,
                )
            }
        }

        composeRule.onNodeWithTag("ad_slot_TodayAfterOverview").assertExists()
    }

    @Test
    fun hidesBlockedPlacementEvenWhenAdsAreEnabled() {
        composeRule.setContent {
            HermesStudyVaultTheme {
                AdBannerSlot(
                    placement = AdPlacement.WriteEditor,
                    policy = AdPlacementPolicy(adsEnabled = true),
                    adUnitId = TestBannerAdUnitId,
                )
            }
        }

        composeRule.onNodeWithTag("ad_slot_WriteEditor").assertDoesNotExist()
    }

    private companion object {
        const val TestBannerAdUnitId = "ca-app-pub-3940256099942544/6300978111"
    }
}
