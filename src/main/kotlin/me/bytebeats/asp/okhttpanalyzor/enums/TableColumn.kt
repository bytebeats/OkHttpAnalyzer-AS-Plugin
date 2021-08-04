package me.bytebeats.asp.okhttpanalyzor.enums

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 20:48
 * @Version 1.0
 * @Description TO-DO
 */

enum class TableColumn(val text: String, val widthPercent: Int) {
    STATUS("Status", 5), METHOD("Method", 5), REQUEST("Request", 75), DURATION("Duration", 5), TIME("Time", 10);
}