package me.bytebeats.asp.analyzer.data

import me.bytebeats.asp.analyzer.enums.MessageType
import java.util.concurrent.locks.ReentrantLock

object RequestDataSource {
    private val requestMapById = HashMap<String, DebugRequest>()
    private val reentrantLock = ReentrantLock()

    fun getRequestFromMessage(id: String, type: MessageType, message: String): DebugRequest? {
        try {
            if (type != MessageType.UNKNOWN) {
                try {
                    reentrantLock.lock()
                    val request = requestMapById[id]
                    return if (request == null) {
                        val newRequest = DebugRequest(id)
                        fillRequest(type, newRequest, message)
                        requestMapById[id] = newRequest
                        newRequest
                    } else {
                        fillRequest(type, request, message)
                        request
                    }
                } finally {
                    reentrantLock.unlock()
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return null
    }

    private fun fillRequest(messageType: MessageType, request: DebugRequest, message: String) {
        when (messageType) {
            MessageType.REQ_URL -> request.url = message
            MessageType.REQ_METHOD -> request.method = message
            MessageType.REQ_TIME -> request.requestTime = message
            MessageType.REQ_BODY -> request.addBody(message, true)
            MessageType.REQ_HEADER -> request.addHeader(message, true)
            MessageType.REQ_END -> request.addHeader(message, true)
            MessageType.RESP_TIME -> request.duration = message
            MessageType.RESP_STATUS -> {
                try {
                    request.responseCode = message.toInt()
                } catch (_: NumberFormatException) {
                }
            }
            MessageType.RESP_HEADER -> request.addHeader(message, false)
            MessageType.RESP_BODY -> request.addBody(message, false)
            MessageType.RESP_ERROR -> request.error = message
            MessageType.RESP_END -> request.closeResponse()
            else -> {
                request.trash(message)
            }
        }
    }

    fun clear() {
        requestMapById.clear()
    }
}