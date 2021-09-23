package me.bytebeats.asp.analyzer.data.generic

import me.bytebeats.asp.analyzer.data.DebugRequest
import me.bytebeats.asp.analyzer.util.*
import java.util.*

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 17:26
 * @Version 1.0
 * @Description TO-DO
 */

class CurlRequest(private val debugRequest: DebugRequest) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder
            .append(CURL)
            .append(SPACE)
        debugRequest.method?.let {
            builder
                .append(METHOD)
                .append(SPACE)
                .append(it.uppercase(Locale.getDefault()))
                .append(SPACE)
        }
        debugRequest.requestHeaders.forEach { header ->
            builder
                .append(HEADER_PARAM)
                .append(SPACE)
                .append(STRING_WRAPPER)
                .append(header)
                .append(STRING_WRAPPER)
                .append(SPACE)
        }
        val requestBodyString = debugRequest.requestBodyString()
        if (requestBodyString.isNotEmpty()) {
            builder
                .append(DATA_BINARY)
                .append(SPACE)
                .append(STRING_WRAPPER)
                .append(requestBodyString)
                .append(STRING_WRAPPER)
                .append(SPACE)
        }

        builder.append(COMPRESSED)
        builder.append(SPACE)
        builder.append(STRING_WRAPPER)
        builder.append(debugRequest.url)
        builder.append(STRING_WRAPPER)

        return builder.toString()
    }
}