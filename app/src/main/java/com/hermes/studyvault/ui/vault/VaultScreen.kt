package com.hermes.studyvault.ui.vault

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.ui.ads.AdBannerSlot

@Composable
fun VaultScreen(
    adPolicy: AdPlacementPolicy,
    bannerAdUnitId: String,
) {
    Column(Modifier.padding(16.dp)) {
        Text(stringResource(R.string.vault_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.vault_body))
        AdBannerSlot(
            placement = AdPlacement.VaultAfterHeader,
            policy = adPolicy,
            adUnitId = bannerAdUnitId,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
