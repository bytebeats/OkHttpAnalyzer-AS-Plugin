package me.bytebeats.asp.analyzer.view

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBTabbedPane
import me.bytebeats.asp.analyzer.data.DebugRequest
import me.bytebeats.asp.analyzer.enums.Tabs
import me.bytebeats.asp.analyzer.util.Resources
import me.bytebeats.asp.analyzer.view.adapter.TreeNodeMouseAdapter
import me.bytebeats.asp.analyzer.view.form.HeaderForm
import me.bytebeats.asp.analyzer.view.form.JsonPlainTextForm
import me.bytebeats.asp.analyzer.view.form.JsonTreeForm
import me.bytebeats.asp.analyzer.view.form.RawForm
import me.bytebeats.asp.analyzer.view.json.JsonTreeModel
import me.bytebeats.asp.analyzer.view.listener.TreeNodeMenuListener
import java.awt.Color
import java.awt.Dimension
import java.io.IOException
import java.util.concurrent.Executors
import javax.swing.JLabel
import javax.swing.SwingUtilities

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 21:29
 * @Version 1.0
 * @Description TO-DO
 */

class TabHelper(private val tabbedPane: JBTabbedPane, private val menuListener: TreeNodeMenuListener) {
    private val executor = Executors.newFixedThreadPool(3)

    @Volatile
    private var curRequest: DebugRequest? = null

    private val tabTitles = mutableMapOf<Int, String>()

    //request
    private val requestHeaderForm = HeaderForm()
    private val requestRawForm = RawForm()
    private val requestJsonForm = JsonTreeForm()
    private val requestFormattedJsonForm = JsonPlainTextForm()

    //response
    private val responseHeaderForm = HeaderForm()
    private val responseRawForm = RawForm()
    private val responseJsonForm = JsonTreeForm()
    private val responseFormattedJsonForm = JsonPlainTextForm()

    private val errorRawForm = RawForm()

    private var requestRawTabIdx = 0
    private var requestHeaderTabIdx = 0
    private var requestJsonTabIdx = 0
    private var requestFormattedJsonTabIdx = 0

    private var responseRawTabIdx = 0
    private var responseHeaderTabIdx = 0
    private var responseJsonTabIdx = 0
    private var responseFormattedJsonTabIdx = 0

    private var errorTabIdx = 0

    private var defaultBackground = Color.GRAY

    private val acceptedContentTypes = listOf("text/plain", "text/html", "application/json")
    private val contentTypeHeader = "content-type:"

    fun initialize() {
        tabbedPane.minimumSize = Dimension(100, 50)

        requestRawTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQ_RAW.resName), requestRawForm.panel)
        requestHeaderTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQ_HEADERS.resName), requestHeaderForm.panel)
        requestJsonTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQ_JSON.resName), requestJsonForm.treePanel)
        requestFormattedJsonTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQ_FORMATTED.resName), requestFormattedJsonForm.panel)
        requestJsonForm.tree.addMouseListener(TreeNodeMouseAdapter(menuListener))

        responseRawTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESP_RAW.resName), responseRawForm.panel)
        responseHeaderTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESP_HEADERS.resName), responseHeaderForm.panel)
        responseJsonTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESP_JSON.resName), responseJsonForm.treePanel)
        responseFormattedJsonTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESP_FORMATTED.resName), responseFormattedJsonForm.panel)

        errorTabIdx = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_ERROR_MESSAGE.resName), errorRawForm.panel)

        responseJsonForm.tree.addMouseListener(TreeNodeMouseAdapter(menuListener))
        defaultBackground = tabbedPane.getForegroundAt(0)

        clearTabs()
    }

    private fun isAcceptedHeaders(headers: Collection<String>): Boolean {
        return headers.isEmpty() || headers.find {
            var res = false
            for (type in acceptedContentTypes) {
                val lowercaseHeader = it.toLowerCase()
                if (lowercaseHeader.startsWith(type) && lowercaseHeader.contains(type)) {
                    res = true
                }
            }
            return res
        } != null
    }

    fun fill(request: DebugRequest) {
        curRequest = request
        clearTabs(Resources.getString("processing"))
        executor.execute {
            val isTextRequest = isAcceptedHeaders(request.requestHeaders)
            val isTextResponse = isAcceptedHeaders(request.responseHeaders)

            val requestJson = if (isTextRequest) {
                request.requestBodyString()
            } else null
            val requestJsonPair = getTreeModelPrettifyPair(requestJson)
            val responseJson = if (isTextResponse) {
                request.responseBodyString()
            } else null
            val responseJsonPair = getTreeModelPrettifyPair(responseJson)

            SwingUtilities.invokeLater {
                if (curRequest != request) return@invokeLater
                requestRawForm.setText(request.rawRequest())
                enableTab(requestRawTabIdx)
                if (request.requestHeaders.isNotEmpty()) {
                    updateRequestHeaderTabs(request.requestHeaders)
                    enableTab(requestHeaderTabIdx)
                } else {
                    disableTab(requestHeaderTabIdx)
                }
                if (request.isClosed) {
                    responseRawForm.setText(request.rawResponse())
                    enableTab(responseRawTabIdx)

                    if (request.responseHeaders.isNotEmpty()) {
                        updateResponseHeaderTabs(request.responseHeaders)
                        enableTab(responseHeaderTabIdx)
                    } else {
                        disableTab(responseHeaderTabIdx)
                    }

                    if (requestJsonPair?.first != null) {
                        requestJsonForm.tree.model = requestJsonPair.first
                        enableTab(requestJsonTabIdx)
                    } else {
                        disableTab(requestJsonTabIdx)
                    }
                    if (requestJsonPair?.second != null && requestJsonPair.second != "") {
                        requestFormattedJsonForm.setText(requestJsonPair.second)
                        enableTab(requestFormattedJsonTabIdx)
                    } else {
                        disableTab(requestFormattedJsonTabIdx)
                    }
                    if (responseJsonPair?.first != null) {
                        responseJsonForm.tree.model = responseJsonPair.first
                        enableTab(responseJsonTabIdx)
                    } else {
                        disableTab(responseJsonTabIdx)
                    }
                    if (responseJsonPair?.second != null && responseJsonPair.second != "") {
                        responseFormattedJsonForm.setText(responseJsonPair.second)
                        enableTab(responseFormattedJsonTabIdx)
                    } else {
                        disableTab(responseFormattedJsonTabIdx)
                    }
                    if (request.error.isNullOrBlank()) {
                        disableTab(errorTabIdx)
                    } else {
                        enableTab(errorTabIdx)
                        errorRawForm.setText(request.error)
                    }
                }
            }
        }
    }

    fun clearTabs(process: String = "") {
        disableTab(requestRawTabIdx)
        disableTab(requestHeaderTabIdx)
        disableTab(requestJsonTabIdx)
        disableTab(requestFormattedJsonTabIdx)
        disableTab(responseRawTabIdx)
        disableTab(responseHeaderTabIdx)
        disableTab(responseJsonTabIdx)
        disableTab(responseFormattedJsonTabIdx)
        disableTab(errorTabIdx)

        responseRawForm.setText(process)
        updateResponseHeaderTabs(listOf(process))
        requestFormattedJsonForm.setText(process)
        requestJsonForm.tree.model = null

        requestRawForm.setText(process)
        updateResponseHeaderTabs(listOf(process))
        responseFormattedJsonForm.setText(process)
        responseJsonForm.tree.model = null

        errorRawForm.setText(process)
    }

    private fun updateResponseHeaderTabs(headers: Collection<String>) {
        responseHeaderForm.headerList.model = CollectionListModel(headers)
    }

    private fun updateRequestHeaderTabs(headers: Collection<String>) {
        requestHeaderForm.headerList.model = CollectionListModel(headers)
    }

    private fun disableTab(tabIndex: Int) {
        val label = tabbedPane.getTabComponentAt(tabIndex)
        if (label is JLabel) {
            if (tabTitles[tabIndex] == null) {
                tabTitles[tabIndex] = label.text
            }
            label.text = ""
        }

        tabbedPane.setEnabledAt(tabIndex, false)
        tabbedPane.setForegroundAt(tabIndex, Color.GRAY)
        tabbedPane.setToolTipTextAt(tabIndex, Resources.getString("empty"))
        if (tabbedPane.selectedIndex == tabIndex) {
            tabbedPane.selectedIndex = 0
        }
    }

    private fun enableTab(tabIndex: Int) {
        val title = tabTitles[tabIndex]
        if (title != null) {
            val label = tabbedPane.getTabComponentAt(tabIndex)
            if (label is JLabel) {
                label.text = title
            }
        }
        tabbedPane.setEnabledAt(tabIndex, true)
        tabbedPane.setForegroundAt(tabIndex, defaultBackground)
        tabbedPane.setToolTipTextAt(tabIndex, null)
    }

    private fun parseModel(json: String): JsonNode? {
        val trimmed = json.trim()
        return if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            val mapper = ObjectMapper()
            try {
                mapper.readTree(trimmed)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    private fun prettifyNode(node: JsonNode): String? {
        return try {
            val mapper = ObjectMapper()
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getTreeModelPrettifyPair(json: String?): Pair<JsonTreeModel, String?>? {
        return if (json != null) {
            val node = parseModel(json)
            node?.let {
                val prettyJson = prettifyNode(it)
                val model = JsonTreeModel(it)
                Pair(model, prettyJson)
            }
        } else null
    }
}