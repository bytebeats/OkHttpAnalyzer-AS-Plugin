package me.bytebeats.asp.okhttpanalyzor.util

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/2 11:13
 * @Version 1.0
 * @Description constant values
 */

const val BUNDLE_STRINGS = "strings"
const val SELECTED_DEVICE = "selected_device"
const val SELECTED_PID = "selected_pid"
const val SELECTED_TAB = "selected_tab"
const val NEW_LINE = "\r\n"
const val SPACE = " "

const val CURL = "curl"
const val HEADER_PARAM = "-H"
const val METHOD = "-X"
const val DATA_BINARY = "--data-binary"
const val COMPRESSED = "--compressed"
const val STRING_WRAPPER = "'"

const val IMPORT_GSON = "import com.google.gson.annotations.SerializedName;\r\n"
const val IMPORT_LIST = "import java.util.List;\r\n"
const val TODO_NULLABLE = "\t//TODO: CHECK THIS CLASS. IT WAS NULL\r\n"
const val TODO = "\t//TODO: "
const val CLASS_NAME = "class "
const val START_OF_CLASS = " {"
const val END_OF_CLASS = "}"
const val LINE_BREAK = "\r\n"
const val TABULATION = "\t"
const val LINE_END = ";"
const val OBJECT_NAME_DEFAULT = "object"
const val SERIALIZED_TAG_START = "@SerializedName(\""
const val SERIALIZED_TAG_END = "\")"
const val UNDERLINE_CHAR = '_'
const val GENERIC_START = '<'
const val GENERIC_END = '>'

const val NULL_STRING = "null"

const val IMPORT_NULLABLE = "import android.support.annotation.Nullable;\r\n"
const val CONST_VISIBILITY = "private "
const val NULLABLE_ANNOTATION = "@Nullable"

const val DATA_CLASS = "data "
const val VAL_CONST = "val "
const val VAL_DELIMITER = ": "
const val ARG_START = '('
const val ARG_END = ')'
const val ARG_DELIMITER = ','

const val MAX_BODY_LENGTH = 300_000

val RESERVED_WORDS = setOf(
    "package",
    "as",
    "typealias",
    "class",
    "this",
    "super",
    "val",
    "var",
    "fun",
    "for",
    "null",
    "true",
    "false",
    "is",
    "in",
    "throw",
    "return",
    "break",
    "continue",
    "object",
    "if",
    "try",
    "else",
    "while",
    "do",
    "when",
    "interface",
    "yield",
    "typeof",
    "abstract",
    "continue",
    "for",
    "new",
    "switch",
    "assert",
    "default",
    "goto",
    "package",
    "synchronized",
    "boolean",
    "do",
    "if",
    "private",
    "this",
    "break",
    "double",
    "implements",
    "protected",
    "throw",
    "byte",
    "else",
    "import",
    "public",
    "throws",
    "case",
    "enum",
    "instanceof",
    "return",
    "transient",
    "catch",
    "extends",
    "int",
    "short",
    "try",
    "char",
    "final",
    "interface",
    "static",
    "void",
    "class",
    "finally",
    "long",
    "strictfp",
    "volatile",
    "const",
    "float",
    "native",
    "super",
    "while"
)