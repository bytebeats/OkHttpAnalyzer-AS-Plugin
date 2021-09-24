package me.bytebeats.asp.analyzer

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/5 12:20
 * @Version 1.0
 * @Description TO-DO
 */

class AdbToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val preferences = Preferences(PropertiesComponent.getInstance(project))
        val mainForm = MainForm()
        val adbManager = AdbManager(mainForm, project, preferences)
        toolWindow.component.add(mainForm.panel)
    }
}