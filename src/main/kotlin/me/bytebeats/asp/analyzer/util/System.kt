package me.bytebeats.asp.analyzer.util

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

object System {
    fun copyToClipBoard(text: String?) {
        text?.let {
            val stringSelection = StringSelection(it)
            val clipBoard = Toolkit.getDefaultToolkit().systemClipboard
            clipBoard.setContents(stringSelection, null)
        }
    }

    fun openUrlInBrowser(url: String?) {
        url?.let {
            try {
                Desktop.getDesktop().browse(URI.create(it))
            } catch (ignored: IllegalArgumentException) {

            }
        }
    }
}