package core.domain.provider

/**
 * Abstraction responsible for surfacing environment and platform-specific metrics.
 */
interface DeviceMetadataProvider {
    fun getDeviceInfo(): DeviceInfo
}

/**
 * Factory function that returns a platform-specific DeviceMetadataProvider.
 * Each platform source set provides its own `actual` implementation.
 */
expect fun createDeviceMetadataProvider(): DeviceMetadataProvider
