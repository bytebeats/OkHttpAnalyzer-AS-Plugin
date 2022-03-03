package me.bytebeats.asp.analyzer.util

import com.intellij.openapi.util.IconLoader
import java.io.FileNotFoundException
import java.util.*
import javax.swing.Icon

fun getString(key: String): String {
    return ResourceBundle.getBundle(BUNDLE_STRINGS).getString(key)
}

fun getIcon(key: String): Icon {
    return IconLoader.findIcon("/icons/$key") ?: throw FileNotFoundException("Icon $key not exists")
}