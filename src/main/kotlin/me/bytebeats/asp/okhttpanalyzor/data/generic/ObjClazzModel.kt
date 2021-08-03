package me.bytebeats.asp.okhttpanalyzor.data.generic

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/3 17:36
 * @Version 1.0
 * @Description TO-DO
 */

class ObjClazzModel(var name: String) {
    var parent: ObjClazzModel? = null
    var fieldModels: MutableList<FieldModel> = mutableListOf()
    var innerClasses: MutableList<ObjClazzModel> = mutableListOf()
}