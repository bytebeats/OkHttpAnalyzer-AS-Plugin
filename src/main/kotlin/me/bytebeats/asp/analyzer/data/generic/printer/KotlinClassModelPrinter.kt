package me.bytebeats.asp.analyzer.data.generic.printer

import me.bytebeats.asp.analyzer.data.generic.FieldModel
import me.bytebeats.asp.analyzer.data.generic.FieldType
import me.bytebeats.asp.analyzer.data.generic.ObjClazzModel
import me.bytebeats.asp.analyzer.util.*

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:23
 * @Version 1.0
 * @Description TO-DO
 */

class KotlinClassModelPrinter(private val classModels: List<ObjClazzModel>) : BaseClassModelPrinter() {
    override fun obtainListType(field: FieldModel): String {
        return if (field.genericType != null) field.genericType.kotlinType
        else field.type.kotlinType
    }

    override fun build(): StringBuilder {
        addImports()
        classModels.forEach { classModel ->
            if (classModel.fieldModels.isEmpty()) {
                builder.append(CLASS_NAME)
                builder.append(classModel.name)
                builder.append(LINE_BREAK)
                builder.append(TODO_NULLABLE)
                builder.append(LINE_BREAK)
                builder.append(LINE_BREAK)
            } else {
                builder.append(DATA_CLASS)
                builder.append(CLASS_NAME)
                builder.append(classModel.name)
                builder.append(ARG_START)
                builder.append(LINE_BREAK)
                classModel.fieldModels.forEachIndexed { index, field ->
                    addField(field)
                    if (index != classModel.fieldModels.lastIndex) {
                        builder.append(ARG_DELIMITER)
                    }
                    builder.append(LINE_BREAK)
                }
                builder.append(ARG_END)
                builder.append(LINE_BREAK)
            }
        }
        return builder
    }

    override fun addField(field: FieldModel) {
        addSerializationAnnotation(field.originalName)
        builder.append(TABULATION, VAL_CONST)
        builder.append(field.name, VAL_DELIMITER)

        when {
            field.type == FieldType.LIST -> {
                builder.append(FieldType.LIST.kotlinType)
                builder.append(GENERIC_START)
                if (field.typeClassName != null) {
                    builder.append(field.typeClassName)
                } else {
                    builder.append(field.genericType?.kotlinType)
                }
                builder.append(GENERIC_END)
            }
            field.typeClassName != null -> builder.append(field.typeClassName)
            else -> builder.append(field.type.kotlinType)
        }
    }
}