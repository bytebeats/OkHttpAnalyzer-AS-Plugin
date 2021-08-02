package me.bytebeats.asp.okhttpanalyzor.util

import com.fasterxml.jackson.databind.JsonNode

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/2 19:55
 * @Version 1.0
 * @Description TO-DO
 */

fun JsonNode?.isPrimitive(): Boolean =
    if (this == null) false else !this.isNull && !this.isObject && !this.isArray && !this.isTextual && !this.isPojo