package core.domain.provider

import platform.UIKit.UIDevice
import platform.Foundation.NSBundle

private class IosDeviceMetadataProvider : DeviceMetadataProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val currentDevice = UIDevice.currentDevice
        val appVersion = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"

        return DeviceInfo(
            osName = currentDevice.systemName,
            osVersion = currentDevice.systemVersion,
            deviceModel = currentDevice.model,
            appVersion = appVersion
        )
    }
}

actual fun createDeviceMetadataProvider(): DeviceMetadataProvider = IosDeviceMetadataProvider()
