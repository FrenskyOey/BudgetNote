package feature.settings.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import core.components.CoreBackStackAppBar
import core.components.CUsernameInput
import core.components.CoreCheckbox
import core.components.CoreDatePickerInput
import core.components.CoreDropdownInput
import core.components.CoreEmailInput
import core.components.CoreLabelSlider
import core.components.CorePasswordInput
import core.components.CorePercentSlider
import core.components.CorePhoneInput
import core.components.CoreRadioButtonGroup
import core.components.CoreSearchInput
import core.components.CoreTimePickerInput
import core.theme.Dimens
import core.theme.Spacing
import core.theme.getOnBackgroundColor
import core.theme.getSurfaceColor
import core.theme.getTextHeadlineSmall


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(modifier : Modifier = Modifier,
               onBackClick: () -> Unit = {}) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CoreBackStackAppBar(
                title = "Form Input Style",
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
            Spacer(modifier = Modifier.height(Spacing.Tiny))
            Text(
                text = "Form Components",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(Spacing.Tiny))

            // TEXT INPUTS Section
            TextInputsSection()

            Spacer(modifier = Modifier.height(Spacing.Tiny))

            // PICKERS Section
            PickersSection()

            Spacer(modifier = Modifier.height(Spacing.Tiny))

            // SELECTION CONTROLS Section
            SelectionControlsSection()

            Spacer(modifier = Modifier.height(Spacing.Tiny))

            // SLIDERS Section
            SlidersSection()
        }
    }
}

@Composable
private fun TextInputsSection() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Text(
            text = "TEXT INPUTS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Username Input
        CUsernameInput(
            value = username,
            onValueChange = { username = it },
            helperText = "Must be unique"
        )

        // Email Input
        CoreEmailInput(
            value = email,
            onValueChange = { email = it },
            placeholder = "example@domain.com",
            onClearClick = { email = "" }
        )

        // Phone Input with Error
        CorePhoneInput(
            value = phone,
            onValueChange = { phone = it },
            errorText = "Sample Error Text"
        )

        // Password Input
        CorePasswordInput(
            value = password,
            onValueChange = { password = it }
        )

        // Search Input
        CoreSearchInput(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Search...",
            onClearClick = { searchQuery = "" }
        )
    }
}

@Composable
private fun PickersSection() {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var selectedMinute by remember { mutableStateOf<Int?>(null) }
    var selectedCountry by remember { mutableStateOf<String?>(null) }

    val countries = listOf("United States", "Canada", "United Kingdom", "Australia", "Germany")

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Text(
            text = "PICKERS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Birth Date Picker
        CoreDatePickerInput(
            selectedDate = selectedDate ?: 814320000000L, // Oct 24, 1995
            onDateSelected = { selectedDate = it },
            label = "Birth Date",
            dateFormat = "MMM dd, yyyy"
        )

        // Time Picker
        CoreTimePickerInput(
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            onTimeSelected = { hour, minute ->
                selectedHour = hour
                selectedMinute = minute
            },
            label = "Appointment Time"
        )

        // Dropdown/Spinner
        CoreDropdownInput(
            options = countries,
            selectedOption = selectedCountry,
            onOptionSelected = { selectedCountry = it },
            label = "Country",
            helperText = "Select your country"
        )
    }
}

@Composable
private fun SelectionControlsSection() {
    var emailNotifications by remember { mutableStateOf(true) }
    var smsUpdates by remember { mutableStateOf(false) }
    var accountType by remember { mutableStateOf("Personal") }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Text(
            text = "SELECTION CONTROLS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Notification Preferences (Checkboxes)
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
            Text(
                text = "Notification Preferences",
                style = MaterialTheme.typography.titleMedium
            )

            CoreCheckbox(
                checked = emailNotifications,
                onCheckedChange = { emailNotifications = it },
                label = "Email Notifications"
            )

            CoreCheckbox(
                checked = smsUpdates,
                onCheckedChange = { smsUpdates = it },
                label = "SMS Updates"
            )
        }

        // Account Category (Radio Buttons)
        CoreRadioButtonGroup(
            title = "Account Category",
            options = listOf("Personal", "Business"),
            selectedOption = accountType,
            onOptionSelected = { accountType = it }
        )
    }
}

@Composable
private fun SlidersSection() {
    var volumeValue by remember { mutableStateOf(75f) }
    var sampleValue by remember { mutableStateOf(50f) }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
        Text(
            text = "SLIDERS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CoreLabelSlider(
            value = sampleValue,
            onValueChange = { sampleValue = it },
            label = "Sample SLider"
        )

        CorePercentSlider(
            value = volumeValue,
            onValueChange = { volumeValue = it },
            label = "Percent Intensity"
        )
    }
}