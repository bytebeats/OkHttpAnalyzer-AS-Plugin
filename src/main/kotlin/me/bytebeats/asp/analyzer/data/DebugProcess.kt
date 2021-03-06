package me.bytebeats.asp.analyzer.data

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/2 20:11
 * @Version 1.0
 * @Description TO-DO
 */

class DebugProcess(val pid: Int, var process: String?, var clientDescription: String?) {
    fun getClientKey(): String {
        return "$process$clientDescription"
    }

    override fun toString(): String {
        return if (process == null && clientDescription == null) {
            "Process [$pid]"
        } else if (!clientDescription.isNullOrEmpty()) {
            "$clientDescription [$pid]"
        } else {
            "$process [$pid]"
        }
    }
}