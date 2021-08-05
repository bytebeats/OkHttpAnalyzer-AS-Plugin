package me.bytebeats.asp.analyzer.view.form

import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.table.JBTable
import me.bytebeats.asp.analyzer.view.FrameScrollPanel
import me.bytebeats.asp.analyzer.view.SimpleJBPanel
import java.awt.BorderLayout

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:46
 * @Version 1.0
 * @Description TO-DO
 */

class DataForm {
    val panel = SimpleJBPanel()
    val table = JBTable()
    val tabbedPane = JBTabbedPane()

    init {
        val scrollPanel = FrameScrollPanel(table)
        val simplePanel = SimpleJBPanel()
        simplePanel.add(tabbedPane, BorderLayout.CENTER)
        val splitter = JBSplitter(true)
        splitter.proportion = 0.5f
        splitter.firstComponent = scrollPanel
        splitter.secondComponent = simplePanel
        panel.add(splitter)
    }
}