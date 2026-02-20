package feature.settings.ui.button
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.components.CoreBackStackAppBar
import core.components.CoreButton
import core.components.CoreExtendedFloatingActionButton
import core.components.CoreFloatingActionButton
import core.components.CoreOutlinedButton
import core.components.CoreSegmentedButtonGroup
import core.components.CoreSmallFloatingActionButton
import core.components.CoreTextButton
import core.components.CoreTonalButton
import core.theme.Dimens
import core.theme.Spacing
import core.theme.getOnBackgroundColor
import core.theme.getSurfaceColor
import core.theme.getTextHeadlineSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CoreBackStackAppBar(
                title = "Button Style",
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
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {

            // Segmented Buttons Section
            Spacer(modifier = Modifier.height(Spacing.Small))
            SegmentedButtonsSection()
            Spacer(modifier = Modifier.height(Spacing.Small))
            // Filled Buttons Section
            FilledButtonsSection()
            Spacer(modifier = Modifier.height(Spacing.Small))
            // Tonal Buttons Section
            TonalButtonsSection()
            Spacer(modifier = Modifier.height(Spacing.Small))
            // Outlined Buttons Section
            OutlinedButtonsSection()
            Spacer(modifier = Modifier.height(Spacing.Small))
            // Text Buttons Section
            TextButtonsSection()
            Spacer(modifier = Modifier.height(Spacing.Small))
            // FAB Variants Section
            FABVariantsSection()
        }
    }
}


@Composable
private fun SegmentedButtonsSection() {
    var selectedPeriod by remember { mutableStateOf("Day") }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "Segmented Buttons",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        CoreSegmentedButtonGroup(
            options = listOf("Day", "Week", "Month"),
            selectedOption = selectedPeriod,
            onOptionSelected = { selectedPeriod = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FilledButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "Filled Buttons",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            modifier = Modifier.fillMaxWidth()
        ) {
            CoreButton(
                text = "Enabled",
                onClick = { /* Handle click */ }
            )

            CoreButton(
                text = "With Icon",
                onClick = { /* Handle click */ },
                icon = Icons.Default.Add
            )
        }

        CoreButton(
            text = "Disabled",
            onClick = { /* Handle click */ },
            enabled = false
        )
    }
}

@Composable
private fun TonalButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "Tonal Buttons",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            modifier = Modifier.fillMaxWidth()
        ) {
            CoreTonalButton(
                text = "Enabled",
                onClick = { /* Handle click */ }
            )

            CoreTonalButton(
                text = "Icon",
                onClick = { /* Handle click */ },
                icon = Icons.Default.Star
            )
        }
    }
}

@Composable
private fun OutlinedButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "Outlined Buttons",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            modifier = Modifier.fillMaxWidth()
        ) {
            CoreOutlinedButton(
                text = "Outline",
                onClick = { /* Handle click */ }
            )

            CoreOutlinedButton(
                text = "Edit",
                onClick = { /* Handle click */ },
                icon = Icons.Default.Edit
            )
        }
    }
}

@Composable
private fun TextButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "Text Buttons",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            modifier = Modifier.fillMaxWidth()
        ) {
            CoreTextButton(
                text = "Cancel",
                onClick = { /* Handle click */ }
            )

            CoreTextButton(
                text = "Settings",
                onClick = { /* Handle click */ },
                icon = Icons.Default.Settings
            )
        }
    }
}

@Composable
private fun FABVariantsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            text = "FAB Variants",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Tiny)
            ) {
                CoreSmallFloatingActionButton(
                    icon = Icons.Default.Add,
                    contentDescription = "Add",
                    onClick = { /* Handle click */ }
                )
                Text(
                    text = "Small",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Tiny)
            ) {
                CoreFloatingActionButton(
                    icon = Icons.Default.Edit,
                    contentDescription = "Edit",
                    onClick = { /* Handle click */ }
                )
                Text(
                    text = "Regular",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Tiny)
            ) {
                CoreExtendedFloatingActionButton(
                    text = "Compose",
                    icon = Icons.Default.Email,
                    onClick = { /* Handle click */ }
                )
                Text(
                    text = "Extended",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
