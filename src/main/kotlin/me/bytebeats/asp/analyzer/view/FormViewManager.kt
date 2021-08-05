package me.bytebeats.asp.analyzer.view

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.psi.codeStyle.CodeStyleManager
import me.bytebeats.asp.analyzer.MainForm
import me.bytebeats.asp.analyzer.Preferences
import me.bytebeats.asp.analyzer.data.DebugRequest
import me.bytebeats.asp.analyzer.data.RequestDataSource
import me.bytebeats.asp.analyzer.data.generic.JsonNodeToClassConverter
import me.bytebeats.asp.analyzer.data.generic.ObjClazzModel
import me.bytebeats.asp.analyzer.data.generic.printer.JavaClassModelPrinter
import me.bytebeats.asp.analyzer.data.generic.printer.KotlinClassModelPrinter
import me.bytebeats.asp.analyzer.enums.TableColumn
import me.bytebeats.asp.analyzer.util.JAVA
import me.bytebeats.asp.analyzer.util.Resources
import me.bytebeats.asp.analyzer.util.SCR
import me.bytebeats.asp.analyzer.util.System.copyToClipBoard
import me.bytebeats.asp.analyzer.view.adapter.TableMouseAdapter
import me.bytebeats.asp.analyzer.view.form.DataForm
import me.bytebeats.asp.analyzer.view.json.JsonMutableTreeNode
import me.bytebeats.asp.analyzer.view.list.ForcedListSelectionModel
import me.bytebeats.asp.analyzer.view.list.RequestTableCellRenderer
import me.bytebeats.asp.analyzer.view.list.RequestTableModel
import me.bytebeats.asp.analyzer.view.listener.OnRequestItemClickListener
import me.bytebeats.asp.analyzer.view.listener.TreeNodeMenuListener
import java.awt.BorderLayout
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JTable

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/5 12:01
 * @Version 1.0
 * @Description TO-DO
 */


class FormViewManager(private val form: MainForm, settings: Preferences, private val project: Project) :
        TreeNodeMenuListener, OnRequestItemClickListener {

    private val dataForm = DataForm()
    private val requestTable = dataForm.table
    private val requestListModel = RequestTableModel()
    private val tabsHelper = TabHelper(dataForm.tabbedPane, this)
    private val firstLaunch = AtomicBoolean(true)

    init {
        requestTable.addMouseListener(TableMouseAdapter(this))
        requestTable.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        requestTable.model = requestListModel
        requestTable.setDefaultRenderer(Any::class.java, RequestTableCellRenderer())
        requestTable.selectionModel = ForcedListSelectionModel()
        resizeTableColumnsWidth()
        form.localizeButton.text = Resources.getString("localize") + " " + project.name;
        form.localizeButton.addActionListener {
            BrowserUtil.open("https://localebro.com/?utm_source=OkHttpProfiler&utm_medium=IDE&utm_campaign=localize-button")
        }

        form.donateButton.addActionListener {
            BrowserUtil.open("https://www.buymeacoffee.com/gektor650")
        }

        form.clearButton.addActionListener {
            requestListModel.clear()
            RequestDataSource.clear()
            tabsHelper.clearTabs()
        }
        form.scrollToBottomButton.addActionListener {
            requestTable.clearSelection()
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount - 1, 0, true))
        }
        requestTable.selectionModel.addListSelectionListener { it ->
            if (!it.valueIsAdjusting) {
                requestListModel.getRequestAt(requestTable.selectedRow)?.let {
                    fillRequestInfo(it)
                }
            }
        }
        tabsHelper.initialize()
    }

    private fun resizeTableColumnsWidth() {
        requestTable.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                val tW = requestTable.width
                val jTableColumnModel = requestTable.columnModel
                val cantCols = jTableColumnModel.columnCount
                for (i in 0 until cantCols) {
                    val column = jTableColumnModel.getColumn(i)
                    val percent = TableColumn.values()[i].widthPercent
                    val colW = (tW / 100) * percent
                    column.preferredWidth = colW
                }
            }
        })
    }

    override fun onClick(request: DebugRequest) {
//        fillRequestInfo(debugRequest)
    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        tabsHelper.fill(debugRequest)
    }

    fun insertOrUpdate(debugRequest: DebugRequest) {
        if (firstLaunch.get()) {
            synchronized(this) {
                if (firstLaunch.get()) {
                    form.mainContainer.removeAll()
                    form.mainContainer.add(dataForm.panel, BorderLayout.CENTER)
                    firstLaunch.set(false)
                }
            }
        }
        if (requestTable.selectedColumn == -1) {
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount - 1, 0, true))
        }
        requestListModel.addOrUpdate(debugRequest)
    }

    fun clear() {
        requestListModel.clear()
    }

    override fun createJavaModel(node: JsonMutableTreeNode) {
        val classes = JsonNodeToClassConverter().buildClasses(node).getClasses()
        chooseFileAndWriteAndOpen(true, classes)
    }

    override fun createKotlinModel(node: JsonMutableTreeNode) {
        val classes = JsonNodeToClassConverter().buildClasses(node).getClasses()
        chooseFileAndWriteAndOpen(false, classes)
    }

    override fun copyToClipboard(node: JsonMutableTreeNode) {
        copyToClipBoard(node.value.toString())
    }

    override fun openInEditor(node: JsonMutableTreeNode) {
        val text = node.value.toString()
        val directory =
            FileChooser.chooseFiles(FileChooserDescriptor(false, true, false, false, false, false), project, null)
        directory.firstOrNull()?.let { selectedVirtualFile ->
            val file = createUniqueFile(selectedVirtualFile.path, "response", ".json")
            writeAndOpenFile(file, text)
        }
    }

    private fun chooseFileAndWriteAndOpen(isJava: Boolean, classes: List<ObjClazzModel>) {
        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
        val selected = FileChooser.chooseFiles(descriptor, project, null)
        selected.firstOrNull()?.let { selectedVirtualFile ->
            val pathToDirectory = if (selectedVirtualFile.isDirectory) {
                selectedVirtualFile.path
            } else {
                val theEndOfPath = selectedVirtualFile.path.length - selectedVirtualFile.name.length - 1
                selectedVirtualFile.path.substring(0, theEndOfPath)
            }
            val file: File? = classes.firstOrNull()?.let {
                val extension = if (isJava) {
                    ".java"
                } else {
                    ".kt"
                }
                val file = createUniqueFile(pathToDirectory, it.name, extension)
                it.name = file.name.split(".")[0]
                file
            }
            if (file != null && classes.isNotEmpty()) {
                val segments = file.path.split(File.separator)
                val parts = ArrayList<String>()
                for (segment in segments.reversed()) {
                    if (segment == file.name) continue
                    if (segment == JAVA || segment == SCR) break
                    parts.add(segment)
                }
                val packageName = parts.reversed().joinToString(".")
                val textBuilder = if (isJava) {
                    JavaClassModelPrinter(classes).build()
                } else {
                    KotlinClassModelPrinter(classes).build()
                }
                textBuilder.insert(0, "package $packageName;\r\n\r\n")
                classes.firstOrNull()?.let {
                    writeAndOpenFile(file, textBuilder.toString())
                }
            }
        }
    }

    private fun createUniqueFile(path: String, name: String, extension: String, amount: Int = 0): File {
        val file = if (amount > 0) {
            File("$path${File.separator}${name}Generated$amount$extension")
        } else {
            File("$path${File.separator}$name$extension")
        }
        if (file.canRead()) {
            return createUniqueFile(path, name, extension, amount + 1)
        }
        file.createNewFile()
        return file
    }

    private fun writeAndOpenFile(file: File, classText: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            file.createNewFile()
            var writer: BufferedWriter? = null
            try {
                writer = BufferedWriter(FileWriter(file))
                writer.write(classText)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    writer?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)?.let { vFile ->
                val psiFile = PsiManager.getInstance(project).findFile(vFile)
                psiFile?.let {
                    CodeStyleManager.getInstance(project).reformatText(psiFile, 0, psiFile.textLength)
                    psiFile.navigate(true)
                }
            }
        }
    }

    fun addAll(requestList: List<DebugRequest>) {
        requestListModel.addAll(requestList)
    }
}