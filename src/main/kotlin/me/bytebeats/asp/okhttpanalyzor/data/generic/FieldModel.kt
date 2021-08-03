package me.bytebeats.asp.okhttpanalyzor.data.generic

import java.util.concurrent.atomic.AtomicInteger

data class FieldModel(
    val name: String,
    val originalName: String,
    val type: FieldType,
    val typeClassName: String? = null,
    val genericType: FieldType? = null,
    val nestingLevel: AtomicInteger? = null
)
