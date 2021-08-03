package me.bytebeats.asp.okhttpanalyzor.data.generic.printer

import me.bytebeats.asp.okhttpanalyzor.data.generic.FieldModel
import me.bytebeats.asp.okhttpanalyzor.data.generic.FieldType
import me.bytebeats.asp.okhttpanalyzor.util.*

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 19:56
 * @Version 1.0
 * @Description TO-DO
 */

abstract class BaseClassModelPrinter {
    protected val builder = StringBuilder()

    protected open fun addImports() {
        builder.append(IMPORT_GSON)
        builder.append(LINE_BREAK)
    }

    protected open fun addSerializationAnnotation(name: String) {
        builder.append(TABULATION, SERIALIZED_TAG_START, name, SERIALIZED_TAG_END, LINE_BREAK)
    }

    protected fun addListDeclaration(field: FieldModel) {
        val nestingLevel = field.nestingLevel?.get() ?: 1
        for (i in 0 until nestingLevel) {
            builder.append(FieldType.LIST.javaType)
            builder.append(GENERIC_START)
        }
        if (field.typeClassName != null) {
            builder.append(field.typeClassName)
        } else {
            builder.append(obtainListType(field))
        }
        for (i in 0 until nestingLevel) {
            builder.append(GENERIC_END)
        }
    }

    abstract fun obtainListType(filed: FieldModel): String
    abstract fun build(): StringBuilder
    protected abstract fun addField(field: FieldModel)
}