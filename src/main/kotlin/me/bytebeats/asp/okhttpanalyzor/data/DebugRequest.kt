package me.bytebeats.asp.okhttpanalyzor.data

import me.bytebeats.asp.okhttpanalyzor.util.MAX_BODY_LENGTH
import me.bytebeats.asp.okhttpanalyzor.util.NEW_LINE
import me.bytebeats.asp.okhttpanalyzor.util.Resources
import me.bytebeats.asp.okhttpanalyzor.util.SPACE
import java.text.SimpleDateFormat
import java.util.*

data class DebugRequest(val id: String) {
    var url: String? = null
    var method: String? = null
    val requestHeaders = mutableListOf<String>()
    private val requestBody = StringBuilder()
    var duration: String? = null
    var responseCode: Int? = null
    var requestTime: String? = null
        set(value) {
            field = if (value.isNullOrEmpty()) {
                null
            } else {
                try {
                    SimpleDateFormat("HH:mm:ss").format(Date(value.toLong()))
                } catch (ignore: Exception) {
                    null
                }
            }
        }
    var isClosed = false
    val responseHeaders = mutableListOf<String>()
    private val responseBody = StringBuilder()
    var error: String? = null


    private val trash = StringBuilder()
    private var isRequestBodyLimitAchieved = false
    private var isResponseBodyLimitAchieved = false

    fun addHeader(header: String, isRequest: Boolean) {
        if (isRequest) {
            requestHeaders.add(header)
        } else {
            responseHeaders.add(header)
        }
    }

    fun addBody(bodyPart: String, isRequest: Boolean) {
        if (isRequest) {
            if (!isRequestBodyLimitAchieved && requestBody.length < MAX_BODY_LENGTH) {
                requestBody.append(bodyPart)
            } else {
                requestBody.clear()
                requestBody.append(Resources.getString("max_length"))
                isRequestBodyLimitAchieved = true
            }
        } else {
            if (!isResponseBodyLimitAchieved && responseBody.length < MAX_BODY_LENGTH) {
                responseBody.append(bodyPart)
            } else {
                responseBody.clear()
                responseBody.append(Resources.getString("max_length"))
                isResponseBodyLimitAchieved = true
            }
        }
    }

    fun trash(message: String) {
        trash.append(message)
    }

    fun requestBodyString(): String = requestBody.toString()
    fun responseBodyString(): String = responseBody.toString()

    override fun toString(): String {
        return "$id $url $duration"
    }

    fun rawRequest(): String {
        return rawDataString(requestHeaders, requestBody).toString()
    }

    fun rawResponse(): String {
        return rawDataString(responseHeaders, responseBody).toString()
    }

    private fun rawDataString(headers: Collection<String>, body: StringBuilder): StringBuilder {
        val builder = StringBuilder()
        builder.append(method).append(SPACE).append(url).append(NEW_LINE).append(NEW_LINE)
        for (header in headers) {
            builder.append(header).append(NEW_LINE)
        }
        builder.append(NEW_LINE)
        builder.append(body)
        return builder
    }

    fun closeResponse() {
        isClosed = true
    }

    fun isFallenDown(): Boolean {
        return responseCode?.compareTo(400) == 1 || isClosed && error != null
    }

    fun isValid(): Boolean = method != null
}
