package com.hermes.studyvault.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.hermes.studyvault.navigation.VaultRoute

@Composable
fun VaultBottomBar(
    currentRoute: String?,
    onRouteSelected: (VaultRoute) -> Unit
) {
    NavigationBar {
        VaultRoute.bottomDestinations.forEach { route ->
            val label = stringResource(route.labelRes)
            NavigationBarItem(
                modifier = Modifier.testTag("nav_${route.route}"),
                selected = currentRoute == route.route,
                onClick = { onRouteSelected(route) },
                icon = { Text(label.take(1)) },
                label = { Text(label) }
            )
        }
    }
}
