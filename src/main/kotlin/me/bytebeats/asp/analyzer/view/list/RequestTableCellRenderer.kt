package me.bytebeats.asp.analyzer.view.list

import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.UIManager
import javax.swing.table.DefaultTableCellRenderer

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 20:39
 * @Version 1.0
 * @Description TO-DO
 */

class RequestTableCellRenderer : DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        val model = table?.model
        if (model is RequestTableModel) {
            when {
                model.isFallenDown(row) -> component.foreground = Color.RED
                isSelected -> component.foreground = Color.WHITE
                else -> component.foreground = UIManager.getLookAndFeelDefaults().getColor("EditorPane.foreground")
            }
        }
        return component
    }
}