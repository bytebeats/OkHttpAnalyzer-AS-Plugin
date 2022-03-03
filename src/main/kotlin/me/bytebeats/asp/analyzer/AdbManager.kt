package me.bytebeats.asp.analyzer

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.intellij.openapi.project.Project
import me.bytebeats.asp.analyzer.data.DebugDevice
import me.bytebeats.asp.analyzer.data.DebugProcess
import me.bytebeats.asp.analyzer.data.RequestDataSource
import me.bytebeats.asp.analyzer.enums.MessageType
import me.bytebeats.asp.analyzer.util.TAG_DELIMITER
import me.bytebeats.asp.analyzer.util.TAG_KEY
import me.bytebeats.asp.analyzer.view.FormViewManager
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.util.concurrent.Executors
import javax.swing.AbstractAction
import javax.swing.DefaultComboBoxModel
import javax.swing.SwingUtilities

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/5 11:55
 * @Version 1.0
 * @Description TO-DO
 */

class AdbManager(
    private val mainForm: MainForm,
    private val project: Project,
    private val preferences: Preferences
) {
    private val logcatService = AndroidLogcatService.getInstance()

    private var selectedDevice: IDevice? = null
    private var selectedProcess: DebugProcess? = null
    private var selectedMethod: String? = null
    private val requestTableManager = FormViewManager(mainForm, project)

    private val executor = Executors.newFixedThreadPool(1)

    private val logcatListener = object : AndroidLogcatService.LogcatListener {
        override fun onLogLineReceived(line: LogCatMessage) {
            executor.execute {
                val tag = line.header.tag
                val selected = selectedProcess
                if (selected != null && selected.pid == line.header.pid && tag.startsWith(TAG_KEY)) {
                    val sequences = tag.split(TAG_DELIMITER)
                    if (sequences.size == 3) {
                        val id = sequences[1]
                        val messageType = MessageType.from(sequences[2])
                        val debugRequest = RequestDataSource.getRequestFromMessage(id, messageType, line.message)
                        if (debugRequest != null) {
                            try {
                                mainForm.updateMethodList(debugRequest.method)
                                SwingUtilities.invokeLater {
                                    requestTableManager.insertOrUpdate(debugRequest)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        initDeviceList(project)
        mainForm.setMethodItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                requestTableManager.methodFilter = mainForm.methodList.selectedItem as String
            }
        }
        mainForm.setOnEnter(object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                requestTableManager.keywordFilter = mainForm.keyword
            }
        })
    }

    private fun initDeviceList(project: Project) {
        AndroidDebugBridge.addDeviceChangeListener(object : AndroidDebugBridge.IDeviceChangeListener {
            override fun deviceChanged(device: IDevice?, p1: Int) {
                log("deviceChanged $device")
                device?.let {
                    attachToDevice(it)
                }
            }

            override fun deviceConnected(device: IDevice?) {
                log("deviceConnected $device")
                updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected $device")
                updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }
        })
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it?.devices
            if (devices?.isNotEmpty() == true) {
                log("addDebugBridgeChangeListener $it")
                updateDeviceList(devices)
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${it?.isConnected}")
            }
        }
        AndroidDebugBridge.addClientChangeListener { client: Client?, _: Int ->
            updateClient(client)
        }
        val bridge0: AndroidDebugBridge? = AndroidSdkUtils.getDebugBridge(project)
        log("initDeviceList bridge0 ${bridge0?.isConnected}")
    }

    private fun updateClient(client: Client?) {
        val prefSelectedPackage = preferences.selectedProcess
        val clientData = client?.clientData
        val clientModel = mainForm.appList.model
        if (clientData != null && clientModel != null) {
            for (i in 0 until clientModel.size) {
                val model = clientModel.getElementAt(i)
                if (model.pid == clientData.pid) {
                    log("updateClient ${clientData.pid}")
                    model.process = clientData.packageName
                    model.clientDescription = clientData.clientDescription
                    if (model.getClientKey() == prefSelectedPackage) {
                        mainForm.appList.selectedItem = model
                        selectedProcess = model
                    }
                    break
                }
            }
            mainForm.appList.revalidate()
            mainForm.appList.repaint()
        }
    }

    private fun updateDeviceList(devices: Array<IDevice>?) {
        log("updateDeviceList ${devices?.size}")
        val selectedDeviceName = preferences.selectedDevice
        var selectedDevice: IDevice? = null
        if (devices != null) {
            mainForm.mainContainer.isVisible = true
            val debugDevices = ArrayList<DebugDevice>()
            for (device in devices) {
                if (device.name == selectedDeviceName) {
                    selectedDevice = device
                }
                debugDevices.add(DebugDevice(device))
            }
            val model = DefaultComboBoxModel(debugDevices.toTypedArray())
            val list = mainForm.deviceList
            list.model = model
            list.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    log("Selected ${list.selectedItem}")
                    val device = list.selectedItem as DebugDevice
                    attachToDevice(device.device)
                    preferences.selectedDevice = device.device.name
                    requestTableManager.clear()
                }
            }
            if (selectedDevice != null) {
                attachToDevice(selectedDevice)
            } else {
                devices.firstOrNull()?.let {
                    attachToDevice(it)
                }
            }
        } else {
            mainForm.mainContainer.isVisible = false
        }
    }

    private fun attachToDevice(device: IDevice) {
        createProcessList(device)
        setListener(device)
    }

    private fun createProcessList(device: IDevice) {
        val prefSelectedPackage = preferences.selectedProcess
        var defaultSelection: DebugProcess? = null
        val debugProcessList = ArrayList<DebugProcess>()
        log("createProcessList ${device.clients.size}")
        for (client in device.clients) {
            val clientData = client.clientData
            val process = DebugProcess(
                clientData.pid,
                clientData.packageName,
                clientData.clientDescription
            )
            if (prefSelectedPackage == process.getClientKey()) {
                defaultSelection = process
            }
            log("addClient $process")
            debugProcessList.add(process)
        }
        val model = DefaultComboBoxModel(debugProcessList.toTypedArray())
        mainForm.appList.model = model
        mainForm.appList.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                val client = mainForm.appList.selectedItem as DebugProcess
                preferences.selectedProcess = client.getClientKey()
                defaultSelection = client
                selectedProcess = client
                log("selectedProcess $defaultSelection")
                requestTableManager.clear()
//                requestTableManager.addAll(RequestDataSource.r(client.getClientKey()))
                mainForm.resetMethodList()
            }
        }
        if (defaultSelection != null) {
            mainForm.appList.selectedItem = defaultSelection
            selectedProcess = defaultSelection
        } else {
            selectedProcess = debugProcessList.firstOrNull()
        }
    }


    private fun log(text: String) {
        println(text)
    }

    private fun setListener(device: IDevice) {
        log(device.toString())
        val prevDevice = selectedDevice
        if (prevDevice != null) {
            logcatService.removeListener(prevDevice, logcatListener)
        }
        logcatService.addListener(device, logcatListener)
        selectedDevice = device
        val clients = device.clients
        if (clients != null) {
            for (client in clients) {
                updateClient(client)
            }
        }
    }
}