package me.bytebeats.asp.okhttpanalyzor.data.generic.printer

import me.bytebeats.asp.okhttpanalyzor.data.generic.FieldModel
import me.bytebeats.asp.okhttpanalyzor.data.generic.FieldType
import me.bytebeats.asp.okhttpanalyzor.data.generic.ObjClazzModel
import me.bytebeats.asp.okhttpanalyzor.util.*

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 20:12
 * @Version 1.0
 * @Description TO-DO
 */

class JavaClassModelPrinter(private val classModels: List<ObjClazzModel>) : BaseClassModelPrinter() {

    override fun addImports() {
        builder.append(IMPORT_NULLABLE)
        builder.append(IMPORT_LIST)
        super.addImports()
    }

    override fun obtainListType(field: FieldModel): String {
        return if (field.genericType != null) field.genericType.javaWrapperType
        else field.type.javaWrapperType
    }

    override fun build(): StringBuilder {
        addImports()
        classModels.forEachIndexed { index, objClazzModel ->
            builder.append(CLASS_NAME)
            builder.append(objClazzModel.name)
            builder.append(START_OF_CLASS)
            builder.append(LINE_BREAK)
            if (objClazzModel.fieldModels.isEmpty()) {
                builder.append(TODO_NULLABLE)
            } else {
                objClazzModel.fieldModels.forEach { fieldModel -> addField(fieldModel) }
            }
            if (classModels.isNotEmpty() && index == 0) {

            } else if (classModels.isNotEmpty() && index == classModels.lastIndex) {
                builder.append(END_OF_CLASS)
                builder.append(LINE_BREAK)
                builder.append(END_OF_CLASS)
            } else {
                builder.append(END_OF_CLASS)
            }
            builder.append(LINE_BREAK)
            builder.append(LINE_BREAK)
        }
        return builder
    }

    override fun addField(field: FieldModel) {
        if (!field.type.isJavaPrimitive) {
            addNullableAnnotation()
        }
        addSerializationAnnotation(field.originalName)
        builder.append(TABULATION, CONST_VISIBILITY)
        when {
            field.type == FieldType.LIST -> addListDeclaration(field)
            field.typeClassName != null -> builder.append(field.typeClassName)
            else -> builder.append(field.type.javaType)
        }
        builder.append(SPACE)
        builder.append(field.name)
        builder.append(LINE_END)
        builder.append(LINE_BREAK)
    }

    private fun addNullableAnnotation(): JavaClassModelPrinter {
        builder.append(TABULATION, NULLABLE_ANNOTATION, LINE_BREAK)
        return this
    }
}