package com.app.budgetnote


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import core.theme.AppTheme
import feature.dashboard.ui.DashboardScreen
import feature.onboarding.domain.repository.AuthRepository
import feature.onboarding.ui.screen.LoginScreen
import feature.settings.ui.SettingsAction
import feature.settings.ui.button.ButtonScreen
import feature.settings.ui.color.ColorScreen
import feature.settings.ui.form.FormScreen
import feature.settings.ui.navbar.NavbarScreen
import feature.settings.ui.text.TextScreen
import org.koin.compose.koinInject
import androidx.navigation.toRoute
import com.app.budgetnote.navigation.AppRoute
import core.domain.repository.SessionRepository
import core.domain.repository.SessionState
import kotlinx.coroutines.launch

@Composable
fun App() {
    val authRepository = koinInject<AuthRepository>()
    val sessionRepository = koinInject<SessionRepository>()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    
    // Session Source of Truth
    val sessionState by sessionRepository.sessionState.collectAsState()
    
    AppTheme {
        // Handle Session State Changes
        LaunchedEffect(sessionState) {
            when (sessionState) {
                is SessionState.Valid -> {
                    navController.navigate(AppRoute.Dashboard) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is SessionState.Invalid -> {
                    // Only logout if we were previously logged in or checking (optimization)
                    // But effectively, Invalid means "Show Login"
                    authRepository.logout()
                    navController.navigate(AppRoute.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = AppRoute.Login
        ) {
            composable<AppRoute.Login> {
                LoginScreen()
            }

            composable<AppRoute.Dashboard> {
                DashboardScreen(
                    onSettingsAction = { action ->
                        when (action) {
                            SettingsAction.OpenColor -> navController.navigate(AppRoute.SettingsColor)
                            SettingsAction.OpenText -> navController.navigate(AppRoute.SettingsText)
                            SettingsAction.OpenButtons -> navController.navigate(AppRoute.SettingsButton)
                            SettingsAction.OpenForm -> navController.navigate(AppRoute.SettingsForm)
                            SettingsAction.OpenNavBar -> navController.navigate(AppRoute.SettingsNavbar)
                            SettingsAction.Logout -> {
                                coroutineScope.launch {
                                    sessionRepository.invalidateSession()
                                }
                            }
                        }
                    }
                )
            }

            composable<AppRoute.SettingsColor> {
                ColorScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<AppRoute.SettingsText> {
                TextScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<AppRoute.SettingsButton> {
                ButtonScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<AppRoute.SettingsForm> {
                FormScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<AppRoute.SettingsNavbar> {
                NavbarScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
