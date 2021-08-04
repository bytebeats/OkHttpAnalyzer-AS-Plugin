package me.bytebeats.asp.okhttpanalyzor.view

import com.intellij.ui.components.JBTabbedPane
import me.bytebeats.asp.okhttpanalyzor.data.DebugRequest
import me.bytebeats.asp.okhttpanalyzor.view.form.HeaderForm
import me.bytebeats.asp.okhttpanalyzor.view.form.JsonPlainTextForm
import me.bytebeats.asp.okhttpanalyzor.view.form.JsonTreeForm
import me.bytebeats.asp.okhttpanalyzor.view.form.RawForm
import me.bytebeats.asp.okhttpanalyzor.view.listener.TreeNodeMenuListener
import java.awt.Color
import java.util.concurrent.Executors

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
    private val requestHeadersForm = HeaderForm()
    private val requestRawForm = RawForm()
    private val requestJsonTreeForm = JsonTreeForm()
    private val requestFormattedJsonForm = JsonPlainTextForm()

    //response
    private val responseHeadersForm = HeaderForm()
    private val responseRawForm = RawForm()
    private val responseJsonTreeForm = JsonTreeForm()
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


    fun initialize() {

    }
}