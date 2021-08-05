package me.bytebeats.asp.analyzer.view.adapter

import me.bytebeats.asp.analyzer.util.Resources
import me.bytebeats.asp.analyzer.view.json.JsonMutableTreeNode
import me.bytebeats.asp.analyzer.view.listener.TreeNodeMenuListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 11:58
 * @Version 1.0
 * @Description TO-DO
 */

class TreeNodeMouseAdapter(private val listener: TreeNodeMenuListener) : MouseAdapter() {
    private fun popupEvent(me: MouseEvent) {
        val tree = me.source as JTree
        val path = tree.getPathForLocation(me.x, me.y) ?: return
        val rightClickedNode = path.lastPathComponent as DefaultMutableTreeNode
        val selectionPaths = tree.selectionPaths

        val isSelected = selectionPaths?.contains(path) ?: false
        if (!isSelected) {
            tree.selectionPath = path
        }
        val treeNode = rightClickedNode as JsonMutableTreeNode
        val popup = JPopupMenu()
        val copy = JMenuItem(Resources.getString("jtree_popup_copy_to_clipboard"))
        copy.addActionListener {
            listener.copyToClipboard(treeNode)
        }
        val openItem = JMenuItem(Resources.getString("jtree_popup_open_in_editor"))
        openItem.addActionListener {
            listener.openInEditor(treeNode)
        }
        val javaClassItem = JMenuItem(Resources.getString("jtree_popup_create_java_class"))
        javaClassItem.addActionListener {
            listener.createJavaModel(treeNode)
        }
        popup.add(javaClassItem)
        val kotlinClassItem = JMenuItem(Resources.getString("jtree_popup_create_kotlin_class"))
        kotlinClassItem.addActionListener {
            listener.createKotlinModel(treeNode)
        }
        popup.add(copy)
        popup.add(openItem)
        popup.add(javaClassItem)
        popup.add(kotlinClassItem)
        popup.show(tree, me.x, me.y)
    }

    override fun mousePressed(me: MouseEvent?) {
        if (me?.isPopupTrigger == true) {
            popupEvent(me)
        }
    }

    override fun mouseReleased(me: MouseEvent?) {
        if (me?.isPopupTrigger == true) {
            popupEvent(me)
        }
    }
}