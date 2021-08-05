package me.bytebeats.asp.okhttpanalyzor.view.form

import com.intellij.ui.components.JBList
import me.bytebeats.asp.okhttpanalyzor.view.FrameScrollPanel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:50
 * @Version 1.0
 * @Description TO-DO
 */

class HeaderForm {
    val headerList = JBList<String>()
    val panel = FrameScrollPanel(headerList)
}