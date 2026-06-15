package com.hermes.studyvault.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hermes.studyvault.ui.components.VaultBottomBar
import com.hermes.studyvault.ui.inbox.InboxScreen
import com.hermes.studyvault.ui.review.ReviewScreen
import com.hermes.studyvault.ui.today.TodayScreen
import com.hermes.studyvault.ui.vault.VaultScreen
import com.hermes.studyvault.ui.write.WriteScreen

@Composable
fun VaultNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            VaultBottomBar(currentRoute = currentRoute) { route ->
                navController.navigate(route.route) {
                    popUpTo(VaultRoute.Today.route)
                    launchSingleTop = true
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = VaultRoute.Today.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(VaultRoute.Today.route) { TodayScreen() }
            composable(VaultRoute.Inbox.route) { InboxScreen() }
            composable(VaultRoute.Vault.route) { VaultScreen() }
            composable(VaultRoute.Write.route) { WriteScreen() }
            composable(VaultRoute.Review.route) { ReviewScreen() }
        }
    }
}
