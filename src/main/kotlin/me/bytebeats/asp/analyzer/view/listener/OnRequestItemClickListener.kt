package me.bytebeats.asp.analyzer.view.listener

import me.bytebeats.asp.analyzer.data.DebugRequest

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 21:04
 * @Version 1.0
 * @Description TO-DO
 */

interface OnRequestItemClickListener {
    fun onClick(request: DebugRequest)
}