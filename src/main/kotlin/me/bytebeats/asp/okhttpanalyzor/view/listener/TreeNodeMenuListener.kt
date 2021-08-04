package me.bytebeats.asp.okhttpanalyzor.view.listener

import me.bytebeats.asp.okhttpanalyzor.view.json.JsonMutableTreeNode

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:58
 * @Version 1.0
 * @Description TO-DO
 */

interface TreeNodeMenuListener {
    fun copyToClipboard(node: JsonMutableTreeNode)
    fun openInEditor(node: JsonMutableTreeNode)
    fun createJavaModel(node: JsonMutableTreeNode)
    fun createKotlinModel(node: JsonMutableTreeNode)
}