package com.hermes.studyvault.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hermes.studyvault.R
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy
import com.hermes.studyvault.domain.settings.AppLanguage
import com.hermes.studyvault.ui.components.VaultBottomBar
import com.hermes.studyvault.ui.calendar.CalendarScreen
import com.hermes.studyvault.ui.inbox.InboxScreen
import com.hermes.studyvault.ui.review.ReviewScreen
import com.hermes.studyvault.ui.settings.SettingsScreen
import com.hermes.studyvault.ui.today.TodayScreen
import com.hermes.studyvault.ui.vault.VaultScreen
import com.hermes.studyvault.ui.write.WriteScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultNavHost(
    selectedLanguage: AppLanguage,
    adPolicy: AdPlacementPolicy,
    bannerAdUnitId: String,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    if (currentRoute == VaultRoute.Settings.route) {
                        TextButton(
                            modifier = Modifier.testTag("back_button"),
                            onClick = { navController.popBackStack() },
                        ) {
                            Text(stringResource(R.string.back))
                        }
                    } else {
                        TextButton(
                            modifier = Modifier.testTag("settings_button"),
                            onClick = { navController.navigate(VaultRoute.Settings.route) },
                        ) {
                            Text(stringResource(R.string.settings))
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (currentRoute != VaultRoute.Settings.route) {
                VaultBottomBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route.route) {
                        popUpTo(VaultRoute.Today.route)
                        launchSingleTop = true
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = VaultRoute.Today.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(VaultRoute.Today.route) {
                TodayScreen(
                    adPolicy = adPolicy,
                    bannerAdUnitId = bannerAdUnitId,
                )
            }
            composable(VaultRoute.Calendar.route) { CalendarScreen() }
            composable(VaultRoute.Inbox.route) { InboxScreen() }
            composable(VaultRoute.Vault.route) {
                VaultScreen(
                    adPolicy = adPolicy,
                    bannerAdUnitId = bannerAdUnitId,
                )
            }
            composable(VaultRoute.Write.route) { WriteScreen() }
            composable(VaultRoute.Review.route) {
                ReviewScreen(
                    adPolicy = adPolicy,
                    bannerAdUnitId = bannerAdUnitId,
                )
            }
            composable(VaultRoute.Settings.route) {
                SettingsScreen(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = onLanguageSelected,
                )
            }
        }
    }
}
