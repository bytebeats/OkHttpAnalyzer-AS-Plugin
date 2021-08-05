package me.bytebeats.asp.analyzer.enums

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/2 19:51
 * @Version 1.0
 * @Description TO-DO
 */

enum class MessageType(val `val`: String) {
    REQ_URL("RQU"),
    REQ_TIME("RQT"),
    REQ_METHOD("RQM"),
    REQ_HEADER("RQH"),
    REQ_BODY("RQB"),
    REQ_END("RQD"),
    RESP_TIME("RST"),
    RESP_STATUS("RSS"),
    RESP_HEADER("RSH"),
    RESP_BODY("RSB"),
    RESP_END("RSD"),
    RESP_ERROR("REE"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun from(type: String): MessageType {
            for (value in values()) {
                if (value.`val` == type) {
                    return value
                }
            }
            return UNKNOWN
        }
    }
}