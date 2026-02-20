package feature.settings.ui.navbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import core.components.CoreAppBarAction
import core.components.CoreBackStackAppBar
import core.components.CoreBasicAppBar
import core.components.CoreTopAppBar
import core.theme.Dimens
import core.theme.Spacing
import core.theme.getSurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavbarScreen(modifier : Modifier = Modifier,
               onBackClick: () -> Unit = {}) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CoreBackStackAppBar(
                title = "Nav Bar",
                onBackPress = onBackClick
            )
        },
        containerColor = getSurfaceColor()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = Dimens.M)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                // Header
                Spacer(modifier = modifier.height(Dimens.M))
                Text(
                    text = "Updated App Bar Showcase",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Material Design 3 App Bar Variants",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Spacing.Tiny))

                // Variant 1: Simple Center-Aligned
                AppBarVariantSection(
                    number = "01",
                    title = "CENTER-ALIGNED SIMPLE",
                    badge = "Standard"
                ) {
                    CoreBasicAppBar(title = "Home")
                }

                Spacer(modifier = Modifier.height(Spacing.Tiny))

                // Variant 2: Navigation with Back Button
                AppBarVariantSection(
                    number = "02",
                    title = "NAVIGATION VARIANT",
                    badge = "Back Button"
                ) {
                    CoreBackStackAppBar(
                        title = "Details",
                        onBackPress = { /* Navigate back */ }
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.Tiny))

                // Variant 3: Actions Variant
                AppBarVariantSection(
                    number = "03",
                    title = "ACTIONS VARIANT",
                    badge = "Trailing Icons"
                ) {
                    CoreTopAppBar(
                        title = "Messages",
                        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                        onNavigationClick = {},
                        actions = {
                            CoreAppBarAction(Icons.Default.Search, "Search", { })
                            CoreAppBarAction(Icons.Default.Settings, "Settings", { })
                        },
                        centerTitle = false
                    )
                }
        }
    }
}


@Composable
private fun AppBarVariantSection(
    number: String,
    title: String,
    badge: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$number $title",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(
                        horizontal = Spacing.Small,
                        vertical = Spacing.Tiny
                    )
                )
            }
        }
        content()
    }
}