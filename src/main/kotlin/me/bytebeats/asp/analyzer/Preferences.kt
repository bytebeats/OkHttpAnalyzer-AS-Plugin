package me.bytebeats.asp.analyzer

import com.intellij.ide.util.PropertiesComponent
import me.bytebeats.asp.analyzer.util.SELECTED_DEVICE
import me.bytebeats.asp.analyzer.util.SELECTED_METHOD
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
    var selectedDevice: String?
        get() = properties.getValue(SELECTED_DEVICE)
        set(value) {
            properties.setValue(SELECTED_DEVICE, value)
        }

    var selectedProcess: String?
        get() = properties.getValue(SELECTED_PID)
        set(value) {
            properties.setValue(SELECTED_PID, value)
        }

    var selectedTabName: String?
        get() = properties.getValue(SELECTED_TAB)
        set(value) {
            properties.setValue(SELECTED_TAB, value)
        }

    var selectedMethod: String?
        get() = properties.getValue(SELECTED_METHOD)
        set(value) {
            properties.setValue(SELECTED_METHOD, value)
        }
}