package com.hermes.studyvault.navigation

import androidx.annotation.StringRes
import com.hermes.studyvault.R

enum class VaultRoute(
    val route: String,
    @StringRes val labelRes: Int,
    val isBottomDestination: Boolean = true,
) {
    Today("today", R.string.tab_today),
    Calendar("calendar", R.string.tab_calendar),
    Inbox("inbox", R.string.tab_inbox, isBottomDestination = false),
    Vault("vault", R.string.tab_vault),
    Write("write", R.string.tab_write),
    Review("review", R.string.tab_review),
    Settings("settings", R.string.settings, isBottomDestination = false);

    companion object {
        val bottomDestinations: List<VaultRoute> = entries.filter { route ->
            route.isBottomDestination
        }
    }
}
