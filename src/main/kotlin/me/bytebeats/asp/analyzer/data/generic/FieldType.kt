package me.bytebeats.asp.analyzer.data.generic

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 17:30
 * @Version 1.0
 * @Description TO-DO
 */

enum class FieldType(
    val javaType: String,
    val kotlinType: String,
    val isJavaPrimitive: Boolean,
    val javaWrapperType: String
) {
    INTEGER("int", "Int?", true, "Integer"),
    LONG("long", "Long?", true, "Long"),
    BOOLEAN("boolean", "Boolean?", true, "Boolean"),
    FLOAT("float", "Float?", true, "Float"),
    DOUBLE("double", "Double?", true, "Double"),
    OBJECT("Object", "Any?", false, "Object"),
    LIST("List", "List", false, "List"),
    STRING("String", "String?", false, "String"),
    UNDEFINED("?", "?", false, "?");
}