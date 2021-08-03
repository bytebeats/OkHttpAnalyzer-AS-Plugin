package me.bytebeats.asp.okhttpanalyzor.view.form

import me.bytebeats.asp.okhttpanalyzor.view.FrameScrollPanel
import javax.swing.JTextPane

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:54
 * @Version 1.0
 * @Description TO-DO
 */

class RawForm {
    private val editor = JTextPane()
    val panel = FrameScrollPanel(editor)

    init {
        editor.isEditable = false
        editor.text = ""
        editor.caretPosition = 0
    }

    fun setText(text: String?) {
        editor.text = text
        editor.caretPosition = 0
    }
}