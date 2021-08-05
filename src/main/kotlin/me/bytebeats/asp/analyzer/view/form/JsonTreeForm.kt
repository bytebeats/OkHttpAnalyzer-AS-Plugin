package me.bytebeats.asp.analyzer.view.form

import com.intellij.ui.treeStructure.Tree
import me.bytebeats.asp.analyzer.view.FrameScrollPanel
import java.awt.Font

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:52
 * @Version 1.0
 * @Description TO-DO
 */

class JsonTreeForm {
    val tree = Tree()
    val treePanel = FrameScrollPanel(tree)

    init {
        tree.font = Font(Font.MONOSPACED, Font.PLAIN, 12)
    }
}