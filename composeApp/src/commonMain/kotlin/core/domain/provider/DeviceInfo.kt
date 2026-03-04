package core.domain.provider

/**
 * Encapsulates the specific telemetry and identifying dimensions of the user's host device.
 */
data class DeviceInfo(
    val osName: String,
    val osVersion: String,
    val deviceModel: String,
    val appVersion: String
)
