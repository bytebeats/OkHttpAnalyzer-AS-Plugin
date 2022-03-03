package me.bytebeats.asp.analyzer.view.adapter

import me.bytebeats.asp.analyzer.data.generic.CurlRequest
import me.bytebeats.asp.analyzer.util.copyToClipBoard
import me.bytebeats.asp.analyzer.util.getString
import me.bytebeats.asp.analyzer.util.openUrlInBrowser
import me.bytebeats.asp.analyzer.view.list.RequestTableModel
import me.bytebeats.asp.analyzer.view.listener.OnRequestItemClickListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTable

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 21:05
 * @Version 1.0
 * @Description TO-DO
 */

class TableMouseAdapter(private val listener: OnRequestItemClickListener?) : MouseAdapter() {
    private fun popupEvent(me: MouseEvent) {
        val source = me.source as JTable
        val row = source.rowAtPoint(me.point)
        val request = (source.model as RequestTableModel).getRequestAt(row)
        val popupMenu = JPopupMenu()
        val copyUrl = JMenuItem(getString("jtable_popup_copy_url"))
        copyUrl.addActionListener {
            request?.let { copyToClipBoard(it.url) }
        }

        val openUrl = JMenuItem(getString("jtable_popup_open_url_in_browser"))
        openUrl.addActionListener {
            request?.let { openUrlInBrowser(it.url) }
        }

        val copyResponse = JMenuItem(getString("jtable_popup_copy_response"))
        copyResponse.addActionListener {
            request?.let { copyToClipBoard(it.responseBodyString()) }
        }

        val copyCurlRequest = JMenuItem(getString("copy_curl_request"))
        copyCurlRequest.addActionListener {
            request?.let { copyToClipBoard(CurlRequest(it).toString()) }
        }
        popupMenu.add(copyUrl)
        popupMenu.add(openUrl)
        popupMenu.add(copyResponse)
        popupMenu.add(copyCurlRequest)
        popupMenu.show(source, me.x, me.y)
    }

    override fun mousePressed(me: MouseEvent?) {
        if (me?.isPopupTrigger == true) {
            popupEvent(me)
        } else {
            mouseEvent(me)
        }
    }

    private fun mouseEvent(me: MouseEvent?) {
        me?.let {
            val source = it.source as JTable
            val row = source.rowAtPoint(it.point)
            (source.model as RequestTableModel).getRequestAt(row)?.let { request ->
                listener?.onClick(request)
            }
        }
    }

    override fun mouseReleased(me: MouseEvent?) {
        if (me?.isPopupTrigger == true) {
            popupEvent(me)
        }
    }
}