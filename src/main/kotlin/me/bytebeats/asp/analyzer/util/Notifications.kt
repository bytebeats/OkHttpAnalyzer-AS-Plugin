package me.bytebeats.asp.analyzer.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ex.ProjectManagerEx


/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2022/3/4 14:50
 * @Version 1.0
 * @Description TO-DO
 */
private const val NOTIFICATION_TITLE = "OkHttp Analyzer"
private val LOG_NOTIFICATION_GROUP = NotificationGroup.logOnlyGroup("$NOTIFICATION_TITLE Log")
private val BALLOON_NOTIFICATION_GROUP = NotificationGroup.balloonGroup("$NOTIFICATION_TITLE Balloon")
private val TOOL_WINDOW_NOTIFICATION_GROUP =
    NotificationGroup.toolWindowGroup("$NOTIFICATION_TITLE Tool Window", "$NOTIFICATION_TITLE Tool Window")

/**
 * messages on Event Log Window
 *
 * @param message
 */
fun info(message: String) {
    LOG_NOTIFICATION_GROUP.createNotification(NOTIFICATION_TITLE, message, NotificationType.INFORMATION, null)
        .notify(ProjectManagerEx.getInstance().defaultProject)
}

/**
 * messages on Event Log Window in balloon style
 *
 * @param message
 */
fun infoBalloon(message: String) {
    BALLOON_NOTIFICATION_GROUP.createNotification(NOTIFICATION_TITLE, message, NotificationType.WARNING, null)
        .notify(ProjectManagerEx.getInstance().defaultProject)
}

/**
 * messages on Tool Window
 *
 * @param message
 */
fun infoToolWindow(message: String) {
    TOOL_WINDOW_NOTIFICATION_GROUP.createNotification(NOTIFICATION_TITLE, message, NotificationType.ERROR, null)
        .notify(ProjectManagerEx.getInstance().defaultProject)
}