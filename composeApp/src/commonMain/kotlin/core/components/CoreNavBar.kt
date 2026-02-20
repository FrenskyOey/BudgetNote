package core.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.theme.ComponentDimens
import core.theme.getOnBackgroundColor
import core.theme.getSurfaceColor
import core.theme.getSurfaceVariantColor
import core.theme.getTextTitleLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreTopBarColor() : TopAppBarColors{
    return TopAppBarDefaults.topAppBarColors(
        containerColor = getSurfaceVariantColor(),
        titleContentColor = getOnBackgroundColor(),
        navigationIconContentColor = getOnBackgroundColor()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    centerTitle: Boolean = true,
    colors : TopAppBarColors = CoreTopBarColor(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = getTextTitleLarge().copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                if (navigationIcon != null && onNavigationClick != null) {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = "Navigation",
                            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                        )
                    }
                }
            },
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior,
            /*windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            ),*/
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = getTextTitleLarge().copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                if (navigationIcon != null && onNavigationClick != null) {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = "Navigation",
                            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                        )
                    }
                }
            },
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior,
            /*windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            ),*/
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreBasicAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    colors : TopAppBarColors = CoreTopBarColor(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CoreTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        onNavigationClick = onNavigationClick,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreBackStackAppBar(
    title: String,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    colors : TopAppBarColors = CoreTopBarColor(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CoreTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        onNavigationClick = onBackPress,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreSearchTopAppBar(
    title: String,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors : TopAppBarColors = CoreTopBarColor(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CoreTopAppBar(
        title = title,
        modifier = modifier,
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                )
            }
        },
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}

/**
 * Custom Action Icon Button
 * Helper composable for creating action icons in app bars
 *
 * @param icon Icon to display
 * @param contentDescription Accessibility description
 * @param onClick Click callback
 */
@Composable
fun CoreAppBarAction(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreContentTopAppBar(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    colors : TopAppBarColors = CoreTopBarColor(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = content,
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigation",
                        modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                    )
                }
            }
        },
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}
