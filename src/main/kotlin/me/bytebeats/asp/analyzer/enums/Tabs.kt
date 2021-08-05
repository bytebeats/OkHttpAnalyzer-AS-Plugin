package me.bytebeats.asp.analyzer.enums

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 21:27
 * @Version 1.0
 * @Description TO-DO
 */

enum class Tabs(val resName: String) {
    TAB_REQ_RAW("tab_raw_request"),
    TAB_RESP_RAW("tab_raw_response"),
    TAB_REQ_JSON("tab_json_request"),
    TAB_RESP_JSON("tab_json_response"),
    TAB_REQ_FORMATTED("tab_request_formatted"),
    TAB_RESP_FORMATTED("tab_response_formatted"),
    TAB_REQ_HEADERS("tab_request_headers"),
    TAB_RESP_HEADERS("tab_response_headers"),
    TAB_ERROR_MESSAGE("tab_error_message");
}