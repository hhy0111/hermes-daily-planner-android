package com.hermes.studyvault.ui.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBannerSlot(
    placement: AdPlacement,
    policy: AdPlacementPolicy,
    adUnitId: String,
    modifier: Modifier = Modifier,
) {
    if (!policy.shouldShow(placement) || adUnitId.isBlank()) {
        return
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ad_slot_${placement.name}"),
        factory = { context ->
            AdView(context).apply {
                this.adUnitId = adUnitId
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
            }
        },
        onRelease = { adView -> adView.destroy() },
    )
}
