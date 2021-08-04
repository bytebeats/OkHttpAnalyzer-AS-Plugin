package me.bytebeats.asp.okhttpanalyzor.view.form

import com.intellij.openapi.ui.ComboBox
import com.intellij.uiDesigner.core.GridConstraints
import me.bytebeats.asp.okhttpanalyzor.data.DebugDevice
import me.bytebeats.asp.okhttpanalyzor.data.DebugProcess
import me.bytebeats.asp.okhttpanalyzor.util.Resources
import me.bytebeats.asp.okhttpanalyzor.view.FrameScrollPanel
import me.bytebeats.asp.okhttpanalyzor.view.JBKPanel
import java.awt.Desktop
import java.awt.Dimension
import java.awt.GridLayout
import java.io.IOException
import java.net.URISyntaxException
import javax.swing.JButton
import javax.swing.JEditorPane
import javax.swing.event.HyperlinkEvent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 20:23
 * @Version 1.0
 * @Description TO-DO
 */

class KForm {
    val panel = JBKPanel(GridLayout(2, 1))
    val deviceList = ComboBox<DebugDevice>()
    val processList = ComboBox<DebugProcess>()
    val scrollToBottomBtn = JButton()
    val clearBtn = JButton()

    private val initialHtml = JEditorPane()
    val mainContainer = FrameScrollPanel(initialHtml)

    private val topPanel = JBKPanel(GridLayout(1, 4))

    init {
        scrollToBottomBtn.icon = Resources.getIcon("scroll.png")
        clearBtn.icon = Resources.getIcon("delete.png")

        val preferredSize = Dimension(200, 30)

        deviceList.preferredSize = preferredSize
        processList.preferredSize = preferredSize
        scrollToBottomBtn.preferredSize = preferredSize
        clearBtn.preferredSize = preferredSize

        val constraints1 = GridConstraints()
        constraints1.column = 0
        constraints1.row = 0
        topPanel.add(deviceList, constraints1)

        val constraints2 = GridConstraints()
        constraints2.column = 1
        constraints2.row = 0
        topPanel.add(processList, constraints2)

        val constraints3 = GridConstraints()
        constraints3.column = 2
        constraints3.row = 0
        topPanel.add(scrollToBottomBtn, constraints3)

        val constraints4 = GridConstraints()
        constraints4.column = 3
        constraints4.row = 0
        topPanel.add(clearBtn, constraints4)

        val constraints5 = GridConstraints()
        constraints5.column = 0
        constraints5.row = 0
        panel.add(topPanel, constraints5)

        val constraints6 = GridConstraints()
        constraints6.column = 1
        constraints6.row = 0
        panel.add(mainContainer, constraints6)

        initialHtml.editorKit = JEditorPane.createEditorKitForContentType("text/html")
        initialHtml.isEditable = false

        initialHtml.addHyperlinkListener { e ->
            if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.url.toURI())
                } catch (ignore: IOException) {
                    ignore.printStackTrace()
                } catch (ignore: URISyntaxException) {
                    ignore.printStackTrace()
                }
            }
        }

        try {
            javaClass.classLoader.getResource("initial.html")?.let {
                initialHtml.page = it
            }
        } catch (io: IOException) {
            io.printStackTrace()
        }
    }
}