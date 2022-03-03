package me.bytebeats.asp.analyzer.data

import com.android.ddmlib.IDevice
import java.util.*

data class DebugDevice(val device: IDevice) {
    override fun toString(): String {
        val buildVersion = device.getProperty(IDevice.PROP_BUILD_VERSION)
        val apiLevel = device.getProperty(IDevice.PROP_BUILD_API_LEVEL)
        return if (device.isEmulator) {
            "Emulator ${device.avdData.get().name} Android $buildVersion, API $apiLevel"
        } else {
            val name = device.name.orEmpty().replace("_", " ")
            val shortEnd = if (name.length > 20) {
                name.substring(0, 20)
            } else {
                name
            }
            "${shortEnd.toUpperCase(Locale.getDefault())} Android $buildVersion, API $apiLevel"
        }
    }
}
