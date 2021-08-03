package me.bytebeats.asp.okhttpanalyzor.view.form

import me.bytebeats.asp.okhttpanalyzor.view.FrameScrollPanel
import javax.swing.JTextPane

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:51
 * @Version 1.0
 * @Description TO-DO
 */

class JsonPlainTextForm {
    private val editorPane = JTextPane()
    val panel = FrameScrollPanel(editorPane)

    init {
        editorPane.isEditable = false
    }

    fun setText(text: String?) {
        editorPane.text = text
        editorPane.caretPosition = 0
    }
}