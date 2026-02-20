package com.app.budgetnote.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {

    @Serializable
    data object Login : AppRoute()

    @Serializable
    data object Dashboard : AppRoute()

    @Serializable
    data object SettingsColor : AppRoute()

    @Serializable
    data object SettingsText : AppRoute()

    @Serializable
    data object SettingsButton : AppRoute()

    @Serializable
    data object SettingsForm : AppRoute()

    @Serializable
    data object SettingsNavbar : AppRoute()
}
