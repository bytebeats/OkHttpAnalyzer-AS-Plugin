package me.bytebeats.asp.analyzer.view.json

import com.fasterxml.jackson.databind.JsonNode
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.tree.DefaultTreeModel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 12:08
 * @Version 1.0
 * @Description TO-DO
 */

class JsonTreeModel(node: JsonNode) : DefaultTreeModel(buildTree("", node)) {
    companion object {
        private fun buildTree(
            name: String,
            node: JsonNode,
            maxValueLength: AtomicInteger = AtomicInteger(0),
            isArrayElement: Boolean = false
        ): JsonMutableTreeNode {
            val parent = if (node.isArray) JsonMutableTreeNode.NodeType.ARRAY else JsonMutableTreeNode.NodeType.OBJECT
            val treeNode = JsonMutableTreeNode(name, node, parent, maxValueLength, isArrayElement)
            val fields = node.fields()
            while (fields.hasNext()) {
                val entry = fields.next()
                if (entry.value.isValueNode) {
                    if (maxValueLength.get().compareTo(entry.key.length) == -1) {
                        maxValueLength.set(entry.key.length)
                    }
                    treeNode.add(JsonMutableTreeNode(entry.key, entry.value, maxValueLength))
                } else {
                    treeNode.add(buildTree(entry.key, entry.value, maxValueLength))
                }
            }
            if (node.isArray) {
                for (i in 0 until node.size()) {
                    val element = node[i]
                    val idx = "[$i]"
                    if (maxValueLength.get().compareTo(name.length) == -1) {
                        maxValueLength.set(name.length)
                    }
                    if (element.isValueNode) {
                        treeNode.add(JsonMutableTreeNode(idx, element, maxValueLength, true))
                    } else {
                        treeNode.add(buildTree(idx, element, maxValueLength, true))
                    }
                }
            } else if (node.isValueNode) {
                treeNode.add(JsonMutableTreeNode(name, node, maxValueLength))
            }
            return treeNode
        }
    }
}