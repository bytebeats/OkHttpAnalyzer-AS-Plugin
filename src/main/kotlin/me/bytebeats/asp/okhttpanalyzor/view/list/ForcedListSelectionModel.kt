package me.bytebeats.asp.okhttpanalyzor.view.list

import javax.swing.DefaultListSelectionModel
import javax.swing.ListSelectionModel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 20:38
 * @Version 1.0
 * @Description TO-DO
 */

class ForcedListSelectionModel : DefaultListSelectionModel() {
    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }
}