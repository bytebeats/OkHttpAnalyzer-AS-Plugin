package me.bytebeats.asp.analyzer.view.list

import me.bytebeats.asp.analyzer.data.DebugRequest
import me.bytebeats.asp.analyzer.enums.TableColumn
import me.bytebeats.asp.analyzer.util.getString
import javax.swing.table.DefaultTableModel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/4 20:43
 * @Version 1.0
 * @Description TO-DO
 */

class RequestTableModel : DefaultTableModel() {
    private val reqId2RowIndexMap = mutableMapOf<String, Int>()
    private val rowIdx2ReqMap = mutableMapOf<Int, DebugRequest>()
    private val requests = mutableListOf<DebugRequest>()

    init {
        columnCount = TableColumn.values().size
    }

    override fun getColumnName(column: Int): String {
        return TableColumn.values()[column].text
    }

    fun addOrUpdate(request: DebugRequest) {
        if (!request.isValid()) return
        val rowIndex = reqId2RowIndexMap[request.id]
        if (rowIndex == null) {
            reqId2RowIndexMap[request.id] = rowCount
            rowIdx2ReqMap[rowCount] = request
            requests.add(request)
            super.addRow(convert2RowData(request))
        } else {
            for ((i, value) in convert2RowData(request).withIndex()) {
                setValueAt(value, rowIndex, i)
            }
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    private fun convert2RowData(request: DebugRequest): Array<Any?> {
        val code = when {
            !request.error.isNullOrEmpty() -> getString("request_list_fallen")
            request.responseCode == null -> getString("request_list_loading")
            else -> request.responseCode.toString()
        }
        return arrayOf(code, request.method, request.url, request.duration, request.requestTime)
    }

    fun clear() {
        reqId2RowIndexMap.clear()
        rowIdx2ReqMap.clear()
        requests.clear()
        while (rowCount.compareTo(0) == 1) {
            removeRow(0)
        }
    }

    fun getRequestAt(row: Int): DebugRequest? {
        return rowIdx2ReqMap[row]
    }

    fun isFallenDown(row: Int): Boolean {
        return requests.getOrNull(row)?.isFallenDown() == true
    }

    fun addAll(requests: List<DebugRequest>) {
        for (request in requests) {
            addOrUpdate(request)
        }
    }
}