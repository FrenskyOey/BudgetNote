package core.domain.provider

import android.os.Build
import core.data.local.database.appContext

private class AndroidDeviceMetadataProvider : DeviceMetadataProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val appVersion = try {
            val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
        return DeviceInfo(
            osName = "Android",
            osVersion = Build.VERSION.RELEASE,
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            appVersion = appVersion
        )
    }
}

actual fun createDeviceMetadataProvider(): DeviceMetadataProvider = AndroidDeviceMetadataProvider()
