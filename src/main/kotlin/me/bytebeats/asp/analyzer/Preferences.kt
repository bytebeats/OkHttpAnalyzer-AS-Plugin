package me.bytebeats.asp.analyzer

import com.intellij.ide.util.PropertiesComponent
import me.bytebeats.asp.analyzer.util.SELECTED_DEVICE
import me.bytebeats.asp.analyzer.util.SELECTED_PID
import me.bytebeats.asp.analyzer.util.SELECTED_TAB

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/2 19:44
 * @Version 1.0
 * @Description Records local preference
 */

class Preferences(private val properties: PropertiesComponent) {
    var selectedDevice: String? = properties.getValue(SELECTED_DEVICE)
        set(value) {
            field = value
            properties.setValue(SELECTED_DEVICE, field)
        }

    var selectedProcess: String? = properties.getValue(SELECTED_PID)
        set(value) {
            field = value
            properties.setValue(SELECTED_PID, field)
        }
    var selectedTabName: String? = properties.getValue(SELECTED_TAB)
        set(value) {
            field = value
            properties.setValue(SELECTED_TAB, field)
        }
}