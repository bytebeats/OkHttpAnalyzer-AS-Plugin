package me.bytebeats.asp.okhttpanalyzor.view

import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Component

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:43
 * @Version 1.0
 * @Description TO-DO
 */

class FrameScrollPanel(component: Component) : JBPanel<FrameScrollPanel>(BorderLayout()) {
    init {
        super.add(JBScrollPane(component), BorderLayout.CENTER)
    }
}