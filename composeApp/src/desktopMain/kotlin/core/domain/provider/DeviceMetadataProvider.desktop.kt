package core.domain.provider

private class DesktopDeviceMetadataProvider : DeviceMetadataProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val osName = System.getProperty("os.name") ?: "Desktop"
        val osVersion = System.getProperty("os.version") ?: "Unknown"
        val arch = System.getProperty("os.arch") ?: "Unknown"
        val appVersion = DesktopDeviceMetadataProvider::class.java
            .`package`?.implementationVersion ?: "Unknown"

        return DeviceInfo(
            osName = osName,
            osVersion = osVersion,
            deviceModel = arch,
            appVersion = appVersion
        )
    }
}

actual fun createDeviceMetadataProvider(): DeviceMetadataProvider = DesktopDeviceMetadataProvider()
