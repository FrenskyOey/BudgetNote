package feature.settings.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import core.components.CoreBasicAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.theme.Dimens
import core.theme.getOnPrimaryColor
import feature.settings.ui.SettingsAction

data class SettingItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Suppress("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit
) {
    val settingsItems = listOf(
        SettingItem(
            title = "Color Schema",
            subtitle = "Dynamic colors and tokens",
            icon = Icons.Default.Palette,
            onClick = { onAction(SettingsAction.OpenColor) }
        ),
        SettingItem(
            title = "Typography",
            subtitle = "Scales and fonts",
            icon = Icons.Default.TextFields,
            onClick = { onAction(SettingsAction.OpenText) }
        ),
        SettingItem(
            title = "Buttons",
            subtitle = "FABs, filled, and outlined",
            icon = Icons.Default.SmartButton,
            onClick = { onAction(SettingsAction.OpenButtons) }
        ),
        SettingItem(
            title = "Form Inputs",
            subtitle = "Fields and validation",
            icon = Icons.Default.CommentBank,
            onClick = { onAction(SettingsAction.OpenForm) }
        ),
        SettingItem(
            title = "App Nav Bar",
            subtitle = "Nav Bar Design",
            icon = Icons.Default.Group,
            onClick = { onAction(SettingsAction.OpenNavBar) }
        ),
        SettingItem(
            title = "Logout",
            subtitle = "Sign out of your account",
            icon = Icons.Default.Logout,
            onClick = { onAction(SettingsAction.Logout) }
        )
    )


        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            CoreBasicAppBar(title = "Settings")
            LazyColumn(
                modifier = Modifier.padding(horizontal = Dimens.M),
                verticalArrangement = Arrangement.spacedBy(Dimens.M)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Dimens.S))
                }

                items(settingsItems) { item ->
                    SettingItemCard(item = item)
                }
            }
        }
}

@Composable
fun SettingItemCard(
    item: SettingItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { item.onClick() },
        colors = CardDefaults.cardColors(
            containerColor = getOnPrimaryColor(),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}