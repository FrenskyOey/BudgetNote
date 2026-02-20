package feature.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.theme.getOnPrimaryColor
import core.theme.getOnPrimaryColor
import feature.settings.ui.SettingsAction
import feature.settings.ui.main.SettingScreen

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onSettingsAction: (SettingsAction) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(DashboardTab.Home) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets =  WindowInsets(0.dp),
        bottomBar = {
            NavigationBar(
                containerColor = getOnPrimaryColor()
            ) {
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.Home,
                    onClick = { selectedTab = DashboardTab.Home },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.Settings,
                    onClick = { selectedTab = DashboardTab.Settings },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                DashboardTab.Home -> HomeScreen()
                DashboardTab.Settings -> SettingScreen(
                    onAction = onSettingsAction
                )
            }
        }
    }
}

enum class DashboardTab { Home, Settings }