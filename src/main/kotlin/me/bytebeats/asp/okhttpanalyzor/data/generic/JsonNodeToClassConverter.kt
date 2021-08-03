package me.bytebeats.asp.okhttpanalyzor.data.generic

import com.fasterxml.jackson.databind.JsonNode
import me.bytebeats.asp.okhttpanalyzor.util.OBJECT_NAME_DEFAULT
import me.bytebeats.asp.okhttpanalyzor.util.RESERVED_WORDS
import me.bytebeats.asp.okhttpanalyzor.util.UNDERLINE_CHAR
import me.bytebeats.asp.okhttpanalyzor.util.isPrimitive
import me.bytebeats.asp.okhttpanalyzor.view.json.JsonMutableTreeNode
import java.util.concurrent.atomic.AtomicInteger

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 17:41
 * @Version 1.0
 * @Description TO-DO
 */

class JsonNodeToClassConverter {
    private val nodes = mutableMapOf<ObjClazzModel, MutableMap<String, Int>>()
    private val classNameMap = mutableMapOf<String, Int>()
    private val classModels = mutableListOf<ObjClazzModel>()

    private fun createUniqueField(clazzModel: ObjClazzModel, key: String, node: JsonNode): FieldModel {
        val fieldType = obtainFieldType(node)
        var listGenericType: FieldType? = null
        var nestingLevel: AtomicInteger? = null
        val ifObjName = when (fieldType) {
            FieldType.OBJECT -> {
                val innerClassName = obtainUniqueClassName(key).capitalize()
                createAndFillClass(innerClassName, node, clazzModel)
                innerClassName
            }
            FieldType.LIST -> {
                nestingLevel = AtomicInteger()
                val pair = obtainNestedFieldType(clazzModel, key, nestingLevel, node)
                listGenericType = pair.second
                pair.first
            }
            else -> null
        }
        return FieldModel(
            obtainUniqueNodeName(clazzModel, key),
            key,
            fieldType,
            ifObjName,
            listGenericType,
            nestingLevel
        )
    }

    private fun obtainNestedFieldType(
        clazzModel: ObjClazzModel,
        name: String,
        nestingLevel: AtomicInteger,
        node: JsonNode
    ): Pair<String?, FieldType> {
        nestingLevel.incrementAndGet()
        var type = FieldType.OBJECT
        var className: String? = null
        node.firstOrNull()?.let {
            when {
                it.isObject -> {
                    val innerClassName = obtainUniqueClassName(name).capitalize()
                    createAndFillClass(innerClassName, it, null)
                    type = FieldType.OBJECT
                    className = innerClassName
                }
                it.isArray -> {
                    return obtainNestedFieldType(clazzModel, name, nestingLevel, it)
                }
                it.isPrimitive() || it.isNull -> {
                    type = obtainFieldType(it)
                    className = null
                }
                else -> {
                    type = obtainFieldType(it)
                    className = null
                }
            }
        }
        return Pair(className, type)
    }

    private fun createAndFillClass(name: String, node: JsonNode?, parent: ObjClazzModel? = null) {
        val clazzModel = ObjClazzModel(obtainUniqueClassName(name).capitalize())
        classModels.add(clazzModel)
        when {
            node?.isObject == true -> {
                clazzModel.parent = parent
                val fields = node.fields()
                fields?.forEach { field ->
                    clazzModel.fieldModels.add(
                        createUniqueField(
                            clazzModel,
                            field.key,
                            field.value
                        )
                    )
                }
            }
            node?.isArray == true -> clazzModel.fieldModels.add(createUniqueField(clazzModel, clazzModel.name, node))
            else -> node?.let {
                val type = obtainFieldType(it)
                clazzModel.fieldModels.add(FieldModel(name, name, type))
            }
        }
    }

    private fun obtainFieldType(node: JsonNode): FieldType {
        return when {
            node.isTextual -> FieldType.STRING
            node.isBoolean -> FieldType.BOOLEAN
            node.isNull -> FieldType.OBJECT
            node.isLong -> FieldType.LONG
            node.isDouble -> FieldType.DOUBLE
            node.isFloat -> FieldType.FLOAT
            node.isArray -> FieldType.LIST
            node.isObject -> FieldType.OBJECT
            else -> FieldType.UNDEFINED
        }
    }

    private fun obtainUniqueNodeName(clazzModel: ObjClazzModel, name: String): String {
        var map = nodes[clazzModel]
        if (map == null) {
            map = mutableMapOf()
            nodes[clazzModel] = map
        }
        return obtainUniqueNameFrom(map, name)
    }

    private fun obtainUniqueClassName(name: String): String {
        return obtainUniqueNameFrom(classNameMap, name)
    }

    private fun obtainUniqueNameFrom(map: MutableMap<String, Int>, name: String): String {
        var tmpName = if (name.startsWith(".")) {
            obtainUniqueClassName("ArrayListOf")
        } else {
            reformatName(name)
        }
        val count = map[tmpName]
        return if (count == null) {
            map[tmpName] = 1
            if (RESERVED_WORDS.contains(name)) {
                "${tmpName}0"
            } else {
                tmpName
            }
        } else {
            map[tmpName] = count.inc()
            "$tmpName$count"
        }
    }

    private fun reformatName(name: String): String {
        return when {
            name.isEmpty() -> OBJECT_NAME_DEFAULT
            name.contains(UNDERLINE_CHAR) -> {
                val subnames = name.split(UNDERLINE_CHAR)
                val builder = StringBuilder()
                subnames.forEachIndexed { index, s ->
                    if (index == 0) builder.append(s)
                    else builder.append(s.capitalize())
                }
                builder.toString()
            }
            else -> name
        }
    }

    fun buildClasses(node: JsonMutableTreeNode): JsonNodeToClassConverter {
        createAndFillClass(node.name, node.value)
        return this
    }

    fun getClasses(): List<ObjClazzModel> {
        return classModels
    }
}