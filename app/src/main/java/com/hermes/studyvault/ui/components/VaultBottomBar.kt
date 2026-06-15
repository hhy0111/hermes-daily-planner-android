package com.hermes.studyvault.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hermes.studyvault.navigation.VaultRoute

@Composable
fun VaultBottomBar(
    currentRoute: String?,
    onRouteSelected: (VaultRoute) -> Unit
) {
    NavigationBar {
        VaultRoute.entries.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route.route,
                onClick = { onRouteSelected(route) },
                icon = { Text(route.label.take(1)) },
                label = { Text(route.label) }
            )
        }
    }
}
