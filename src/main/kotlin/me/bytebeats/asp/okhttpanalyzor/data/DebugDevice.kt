package me.bytebeats.asp.okhttpanalyzor.data

import com.android.ddmlib.IDevice

data class DebugDevice(val device: IDevice) {
    override fun toString(): String {
        val buildVersion = device.getProperty(IDevice.PROP_BUILD_VERSION)
        val apiLevel = device.getProperty(IDevice.PROP_BUILD_API_LEVEL)
        return if (device.isEmulator) {
            "Emulator ${device.avdName} Android $buildVersion, API $apiLevel"
        } else {
            val name = device.name.orEmpty().replace("_", " ")
            val shortEnd = if (name.length > 20) {
                name.substring(20)
            } else {
                name
            }
            "${shortEnd.capitalize()} Android $buildVersion, API $apiLevel"
        }
    }
}
