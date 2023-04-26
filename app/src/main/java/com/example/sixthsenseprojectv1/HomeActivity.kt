package com.example.sixthsenseprojectv1

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.teams.Teammates
import com.example.sixthsenseprojectv1.ui.theme.SixthSenseProjectV1Theme
import com.example.sixthsenseprojectv1.ui.theme.customfont
import com.example.sixthsenseprojectv1.uiHelpers.*
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime

var path: String = ""

fun selectMemberOnMap(memberIndex: Int, viewModel: ViewModelRealTime, context: Context) {
    selectedMember = teammateName[memberIndex]
    if (selectedMember == "Me") {
        viewModel.onUiViewChange(
            0,
            0,
            0,
            0,
            "Me",
            null,
            null,
            null
        )

        try {
            val i = teammateName.indexOf("Me")
            val wholeLongitude = myLongitude.getFromDatabase(context, teammateID[i])
            val wholeLatitude = myLatitude.getFromDatabase(context, teammateID[i])
            val delDate = "°"
            val longitudeArray = wholeLongitude.split(delDate)
            val latitudeArray = wholeLatitude.split(delDate)
            val longitude = longitudeArray[0].toDouble()
            val latitude = latitudeArray[0].toDouble()

            MapActivity.zoom = 6.0
            MapActivity.point = Point.fromLngLat(longitude, latitude)
        } catch (e: Exception) {
            //handle exception
        }
    } else {
        viewModel.onUiViewChange(
            null,
            1,
            null,
            0,
            selectedMember,
            null,
            null,
            null
        )

        try {
            val i = teammateName.indexOf(selectedMember)
            val wholeLongitude = myLongitude.getFromDatabase(context, teammateID[i])
            val wholeLatitude = myLatitude.getFromDatabase(context, teammateID[i])
            val delDate = "°"
            val longitudeArray = wholeLongitude.split(delDate)
            val latitudeArray = wholeLatitude.split(delDate)
            val longitude = longitudeArray[0].toDouble()
            val latitude = latitudeArray[0].toDouble()

            MapActivity.zoom = 6.0
            MapActivity.point = Point.fromLngLat(longitude, latitude)
        } catch (e: Exception) {
            //handle exception
        }
    }
}

class HomeActivity : ComponentActivity() {

    //vendor id for testing, use product id 67
    val VENDORID: Int = 9025

    //vendor id they have, use product id 32779
    //val VENDORID: Int = 9114
    val BAUDRATE: Int = 115200
    var mDevice: UsbDevice? = null
    var mSerial: UsbSerialDevice? = null
    var mConnection: UsbDeviceConnection? = null
    val actionUsbPermission = "com.android.example.USB_PERMISSION"
    lateinit var mUsbManager: UsbManager
    var message = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@HomeActivity,
                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent: Intent =
                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(String.format("package:%s", applicationContext.packageName))
                    startActivityIfNeeded(intent, 101)
                } catch (exception: Exception) {
                    val intent: Intent =
                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    startActivityIfNeeded(intent, 101)
                }
            }
        }

        val handler2: Handler = Handler()
        val task2: Runnable = object : Runnable {
            override fun run() {
                //val wholeData = "A54.19!P1006.92!T25.63!U1.83!E400.00!V0.00!"
                val wholeData = message
                val delData = "!"

                if (wholeData.isNotEmpty()) {
                    if (wholeData.last() == '!') {
                        message = ""
                        val splitData = wholeData.split(delData)
                        for (element in splitData) {
                            processSomeData(this@HomeActivity, element)
                        }
                    } else {
                        val splitData = wholeData.split(delData)
                        for (i in 0..(splitData.size - 2)) {
                            val date = splitData[i]
                            processSomeData(this@HomeActivity, date)
                        }
                        message = splitData[splitData.size - 1]
                    }
                }
                handler2.postDelayed(this, 5000)
            }
        }
        task2.run()

        mUsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        initiateFilters()

        setContent {
            SixthSenseProjectV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    if (connectionChange == 1) {
                        ShowConnectionWarning(value = connectionChange)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        RealTimeScreen()
                    }
                }
            }
        }
    }

    ///**********************************UI*******************************************************
    @Composable
    fun ShowConnectionWarning(value: Int) {
        connectionChange = 0
        if (value == -1) {
            DialogAlert().AlertDialogMessage(title = "Alert!", message = "Failed to Connect Device")

        } else if (value == 0) {
            DialogAlert().AlertDialogMessage(title = "Alert!", message = "No Device Found")

        } else if (value == 1) {
            DialogAlert().AlertDialogMessage(
                title = "Alert!",
                message = "Device Connection Established"
            )

        }
    }


    ///**********************************Backend Communication Protocol *******************************************************
    fun startUsbConnection() {
        val usbDevices: HashMap<String, UsbDevice>? = mUsbManager.deviceList

        if (!usbDevices?.isEmpty()!!) {
            var keep = true
            usbDevices.forEach { entry ->
                mDevice = entry.value
                mConnection = mUsbManager.openDevice(mDevice)
                val deviceVendorId: Int? = mDevice?.vendorId
                val deviceProductId: Int? = mDevice?.productId

                //for testing enable when errors
                //Toast.makeText(this@HomeActivity, "Vendor ID $deviceVendorId, Product ID $deviceProductId", Toast.LENGTH_SHORT).show()

                if (deviceVendorId == VENDORID) {
                    val intent: PendingIntent =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            PendingIntent.getBroadcast(
                                this, 0, Intent(actionUsbPermission),
                                PendingIntent.FLAG_MUTABLE
                            )
                        } else {
                            PendingIntent.getBroadcast(
                                this, 0, Intent(actionUsbPermission),
                                0
                            )
                        }
                    mUsbManager.requestPermission(mDevice, intent)

                    connectionChange = 1
                    keep = false

                } else {
                    mConnection = null
                    mDevice = null
                    Log.i("Serial", "Connection to Device was Failed")

                    connectionChange = -1
                }


            }

            if (!keep) {
                return
            }
        } else {
            connectionChange = 0
        }


    }

    fun initiateFilters() {
        val filter = IntentFilter()
        filter.addAction(actionUsbPermission)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        registerReceiver(broadcastReceiver, filter)
    }

    val broadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action!! == actionUsbPermission) {
                val granted: Boolean =
                    intent.extras!!.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)
                if (granted) {

                    mSerial = UsbSerialDevice.createUsbSerialDevice(mDevice, mConnection)
                    writeToTxt()

                    if (mSerial != null) {
                        mSerial!!.open()
                        if (mSerial!!.open()) {
                            mSerial!!.setBaudRate(115200)
                            mSerial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                            mSerial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                            mSerial!!.setParity(UsbSerialInterface.PARITY_NONE)
                            mSerial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
                            //mSerial!!.read(callback123)

                            val outputStream = FileOutputStream(path)

                            mSerial!!.read(object : UsbSerialInterface.UsbReadCallback {
                                override fun onReceivedData(bytes: ByteArray) {
                                    try {
                                        outputStream.write(bytes)

                                        val buffer = String(bytes, charset("UTF-8")).trim()
                                        message = message + buffer

                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                            })
                        }
                    } else {
                        Log.i("Serial", "Port is Null")
                    }
                }
            } else if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
                Toast.makeText(this@HomeActivity, "Connected", Toast.LENGTH_SHORT).show()
                startUsbConnection()
            } else if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                Toast.makeText(this@HomeActivity, "Disconnected", Toast.LENGTH_SHORT).show()
                disconnect()
            }
        }
    }

    fun writeToTxt() {
        val folderName = "SixthSenseImports"
        val fileName = folderName + System.currentTimeMillis() + "_data.txt"
        path =
            Environment.getExternalStorageDirectory()
                .toString() + File.separator.toString() + folderName + File.separator.toString() + fileName
        val path2: String =
            File.separator.toString() + folderName + File.separator.toString() + fileName
        val file =
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator.toString() + folderName
            )
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun disconnect() {
        connectionChange = 0
        mSerial?.close()
    }

    fun processSomeData(context: Context, newData: String) {
        var data = newData
        val dbHandler = DBHandler(context)
        val id = 1

        try {
            if (data.isEmpty()) {
                return
            }

            when (data[0]) {
                'G' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myLongitude.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myLongitude.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'D' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myLatitude.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myLatitude.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'M' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myTime.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myTime.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'T' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myTemperature.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myTemperature.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'H' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myHumidity.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myHumidity.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'P' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myPressure.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myPressure.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'A' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myAltitude.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myAltitude.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'V' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myTVOC.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myTVOC.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'E' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myECO2.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myECO2.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'B' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myBodyTemperature.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myBodyTemperature.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'R' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myHeartRate.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myHeartRate.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'S' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myOxygenSaturation.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myOxygenSaturation.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
                'U' -> {
                    val trimString = data.substring(1).trim()
                    val doubleString = trimString.toDouble()
                    val intString = doubleString.toInt()
                    val warning = myLuminosity.checkWarnings(intString)
                    dbHandler.addNewSensorData(
                        id.toString(), myLuminosity.getName(), data.substring(1),
                        warning.toString(), LocalDateTime.now().toString()
                    )
                }
            }
        } catch (e: Exception) {
            //catch anything
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //call import method
        } else {
            Toast.makeText(this@HomeActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun RealTimeScreen() {

        val viewModel: ViewModelRealTime = viewModel()
        val tempSwitchPanelsMe: Int by viewModel.tempSwitchPanelsMe.observeAsState(tspm)
        val tempSwitchPanelsTeammate: Int by viewModel.tempSwitchPanelsTeammate.observeAsState(tspt)
        val tempSwitchTabsMe: Int by viewModel.tempSwitchTabsMe.observeAsState(tstm)
        val tempSwitchTabsTeammate: Int by viewModel.tempSwitchTabsTeammate.observeAsState(tstt)
        val tempSwitchTeammate: String by viewModel.tempSwitchTeammate.observeAsState(tst)
        val tempSwitchView: Int by viewModel.tempSwitchView.observeAsState(tsv)
        val tempSplitHomeMe: Int by viewModel.tempSplitHomeMe.observeAsState(0)
        val tempSplitHomeTeammate: Int by viewModel.tempSplitHomeTeammate.observeAsState(0)
        viewModel.onUiViewChange(
            tempSwitchPanelsMe,
            tempSwitchPanelsTeammate,
            tempSwitchTabsMe,
            tempSwitchTabsTeammate,
            tempSwitchTeammate,
            tempSwitchView,
            tempSplitHomeMe,
            tempSplitHomeTeammate
        )

        Scaffold(modifier = Modifier
            .background(Color.Transparent)
            .clip(RoundedCornerShape(10.dp)),
            topBar = {
                TopAppBar(modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(.10f)
                    .graphicsLayer {
                        shape = RoundedCornerShape(12.dp)
                        clip = true
                    }, elevation = 13.dp, title = {
                    Text(
                        text = "Live Data",
                        color = Color.White,
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(3.dp),
                        fontSize = 18.sp,
                        overflow = TextOverflow.Visible,
                        letterSpacing = 1.sp

                    )
                }, actions = {

                    //create the row layout to arrange the action button and dropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.85f)
                            .fillMaxHeight()
                            .padding(1.dp)
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(.80f)
                                .fillMaxHeight()
                                .padding(1.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyHorizontalGrid(
                                GridCells.Fixed(1),
                                Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                items(teammateName.size) { index ->
                                    IconButton(
                                        modifier = Modifier
                                            .size(95.dp, 45.dp)
                                            .padding(1.dp)
                                            .background(Color.Transparent),
                                        onClick = {
                                            selectMemberOnMap(
                                                index,
                                                viewModel,
                                                this@HomeActivity
                                            )
                                        }
                                    ) {
                                        Row {
                                            val pin = Teammates().returnPins(
                                                teammateName[index], this@HomeActivity
                                            )
                                            val color =
                                                Teammates().returnPinsColor(teammateName[index])
                                            Icon(
                                                painter = painterResource(pin),
                                                "Pins",
                                                modifier = Modifier
                                                    .padding(1.dp)
                                                    .size(20.dp),
                                                tint = color
                                            )
                                            //create the space for size of 3.Dp
                                            Spacer(modifier = Modifier.size(3.dp))

                                            Text(
                                                teammateName[index], color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(3.dp))

                        IconButton(modifier = Modifier
                            .size(55.dp, 45.dp)
                            .padding(1.dp)
                            .background(Color.Transparent),
                            onClick = {
                                if (tempSwitchView == 0) {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        1,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                } else {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        0,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                }
                            }) {
                            Icon(

                                painter = painterResource(R.drawable.eyeicon),
                                "Change View",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(3.dp))
                        val showConsentDialog = remember { mutableStateOf(false) }
                        if (showConsentDialog.value) {
                            DialogAlert().ConsentDialog(onYesClick = {
                                val dbHandler = DBHandler(this@HomeActivity)
                                dbHandler.exportToCSV(this@HomeActivity)
                                showConsentDialog.value = false
                            }, onNoClick = { showConsentDialog.value = false })
                        }

                        IconButton(modifier = Modifier
                            .size(55.dp, 45.dp)
                            .padding(1.dp)
                            .background(Color.Transparent), onClick = {
                            showConsentDialog.value = true
                        })

                        {
                            Icon(

                                painter = painterResource(R.drawable.export),
                                "Export",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(3.dp))
                    }
                }


                )
            }) {
            MapboxScreen()
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)

                    .padding(4.dp)
                    .clip(RoundedCornerShape(15.dp))
            ) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.5f)
                        .background(Color.Transparent)
                        //.weight(0.5F)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    if (tempSwitchPanelsMe == 0) {
                        RealTimeMainUser()
                    }
                }

                //create the space for size of 3.Dp
                Spacer(modifier = Modifier.size(4.dp))

                if (tempSwitchPanelsTeammate == 2) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f)
                            .background(Color.Transparent)
                            //.weight(0.5F)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(15.dp))
                    ) {
                        RealTimeTeammate()
                    }
                } else if (tempSwitchPanelsTeammate == 1) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f)
                            .background(Color.Transparent)
                            //.weight(0.5F)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            Modifier
                                .fillMaxHeight(.45f)
                                .fillMaxWidth(.98f)
                                .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.End
                        ) {
                            RealTimeTeammateSmall()
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun ExpandablePanelHeader(title: String, arrowPointingUp: Boolean, onClick: () -> Unit) {
        val icon =
            if (arrowPointingUp)
                R.drawable.arrow_up
            else
                R.drawable.arrow_down

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color.Transparent)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )

            Icon(
                painter = painterResource(icon),
                "temp_desc",
                modifier = Modifier
                    .padding(vertical = 4.dp),
                tint = Color.White
            )
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun RealTimeMainUser() {

        val viewModel: ViewModelRealTime = viewModel()
        val tempSwitchPanelsMe: Int by viewModel.tempSwitchPanelsMe.observeAsState(tspm)
        val tempSwitchPanelsTeammate: Int by viewModel.tempSwitchPanelsTeammate.observeAsState(tspt)
        val tempSwitchTabsMe: Int by viewModel.tempSwitchTabsMe.observeAsState(tstm)
        val tempSwitchTabsTeammate: Int by viewModel.tempSwitchTabsTeammate.observeAsState(tstt)
        val tempSwitchTeammate: String by viewModel.tempSwitchTeammate.observeAsState(tst)
        val tempSwitchView: Int by viewModel.tempSwitchView.observeAsState(tsv)
        val tempSplitHomeMe: Int by viewModel.tempSplitHomeMe.observeAsState(0)
        val tempSplitHomeTeammate: Int by viewModel.tempSplitHomeTeammate.observeAsState(0)
        viewModel.onUiViewChange(
            tempSwitchPanelsMe,
            tempSwitchPanelsTeammate,
            tempSwitchTabsMe,
            tempSwitchTabsTeammate,
            tempSwitchTeammate,
            tempSwitchView,
            tempSplitHomeMe,
            tempSplitHomeTeammate
        )

        Row {
            Scaffold(topBar = {
                TopAppBar(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.13f)
                    .padding(top = 3.dp, start = 3.dp, end = 3.dp, bottom = 3.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(12.dp)
                        clip = true
                    }, elevation = 10.dp, title = {
                    Text(
                        text = "My Data",
                        color = Color.White,
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterVertically),
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }, actions = {

                    //create the row layout to arrange the action button and dropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.80f)
                            .fillMaxHeight()
                            .padding(2.dp)
//                                .border(BorderStroke(Dp.Hairline,Color.Red))
                        ,
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Health Action button
                        IconButton(modifier = Modifier
                            .padding(4.dp)
                            .size(60.dp, 60.dp)
                            .weight(0.5F),
                            onClick = {
                                viewModel.onUiViewChange(
                                    tempSwitchPanelsMe,
                                    tempSwitchPanelsTeammate,
                                    0,
                                    tempSwitchTabsTeammate,
                                    tempSwitchTeammate,
                                    tempSwitchView,
                                    0,
                                    tempSplitHomeTeammate
                                )
                            }) {
                            if (tempSwitchTabsMe == 0) {

                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = Color.Green
                                )

//
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = Color.White
                                )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // Health Action button
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(140.dp, 60.dp)
                            .weight(1F), onClick = {
                            viewModel.onUiViewChange(
                                tempSwitchPanelsMe,
                                tempSwitchPanelsTeammate,
                                1,
                                tempSwitchTabsTeammate,
                                tempSwitchTeammate,
                                tempSwitchView,
                                tempSplitHomeMe,
                                tempSplitHomeTeammate
                            )
                        }) {
                            if (tempSwitchTabsMe == 1) {
                                Text(
                                    text = "Health",
                                    color = Color.Green,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Visible,

                                    )
                            } else {
                                Text(
                                    text = "Health",
                                    color = Color.White,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Visible,

                                    )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // Environmental button
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(140.dp, 60.dp)
                            .weight(1.5F), onClick = {
                            viewModel.onUiViewChange(
                                tempSwitchPanelsMe,
                                tempSwitchPanelsTeammate,
                                2,
                                tempSwitchTabsTeammate,
                                tempSwitchTeammate,
                                tempSwitchView,
                                tempSplitHomeMe,
                                tempSplitHomeTeammate
                            )
                        }) {
                            if (tempSwitchTabsMe == 2) {
                                Text(
                                    text = "Environment",
                                    color = Color.Green,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(
                                    text = "Environment",
                                    color = Color.White,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // Environmental button
                        IconButton(modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterVertically)
                            .size(60.dp, 60.dp)
                            .weight(0.5F),
                            onClick = {
                                if (tempSwitchPanelsTeammate == 0) {
                                    //closeMyData = 1
                                    //changeMapMe = 0
                                    viewModel.onUiViewChange(
                                        1,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                    val navigate =
                                        Intent(this@HomeActivity, MapActivity::class.java)
                                    startActivity(navigate)
                                } else {
                                    //closeMyData = 1
                                    //changeMapMe = 0
                                    //val navigate = Intent(this@HomeActivity, MapActivity::class.java)
                                    //startActivity(navigate)
                                    viewModel.onUiViewChange(
                                        1,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                }
                            }

                        ) {
                            Icon(

                                painter = painterResource(R.drawable.close_button),
                                "Close Button",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))
                    }
                })
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                ) {
                    if (tempSwitchTabsMe == 0) {


                        if (tempSplitHomeMe == 0) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {

                                ExpandablePanelHeader(
                                    title = "Warnings",
                                    arrowPointingUp = false
                                ) {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        1,
                                        tempSplitHomeTeammate
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxSize()
                                        .weight(3F),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    val warningSign = SensorsUI().GetWarningLightInt(
                                        this@HomeActivity, "Me"
                                    )
                                    if (warningSign == 0) {
                                        Text(
                                            text = "  No Warnings for the User",
                                            modifier = Modifier
                                                .padding(bottom = 2.dp)
                                                .align(Alignment.CenterVertically),
                                            color = Color.White,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.SemiBold,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 18.sp,

                                            )
                                    } else if (warningSign == 1) {
                                        SensorsUI().RealTimeSingleWarningMe(this@HomeActivity)
                                    }

                                }


                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {

                                ExpandablePanelHeader(
                                    title = "Vitals",
                                    arrowPointingUp = true
                                ) {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        2,
                                        tempSplitHomeTeammate
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .weight(3F),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    SensorsUI().RealTimeVitalSubGraphMe(this@HomeActivity)

                                }

                            }
                        } else if (tempSplitHomeMe == 1) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {

                                ExpandablePanelHeader(
                                    title = "Warnings",
                                    arrowPointingUp = true
                                ) {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        0,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        0,
                                        tempSplitHomeTeammate
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxSize()
                                        .weight(3.3F),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    val warningSign = SensorsUI().GetWarningLightInt(
                                        this@HomeActivity, "Me"
                                    )
                                    if (warningSign == 0) {
                                        Text(
                                            text = "  No Warnings for the User",
                                            modifier = Modifier
                                                .padding(bottom = 2.dp)
                                                .align(Alignment.CenterVertically),
                                            color = Color.White,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.SemiBold,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 18.sp,


                                            )
                                    } else if (warningSign == 1) {
                                        SensorsUI().RealTimeSingleWarningMe(this@HomeActivity)
                                    }
                                }
                            }
                        } else if (tempSplitHomeMe == 2) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {

                                ExpandablePanelHeader(
                                    title = "Vitals",
                                    arrowPointingUp = false
                                ) {
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        tempSwitchPanelsTeammate,
                                        0,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        0,
                                        tempSplitHomeTeammate
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .weight(3.3F),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    SensorsUI().RealTimeVitalSubGraphMe(this@HomeActivity)

                                }
                            }
                        }
                    } else if (tempSwitchTabsMe == 1) {

                        if (tempSwitchView == 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {
                                SensorsUI().RealTimeHealthTwoPointOMe(this@HomeActivity)
                            }
                        } else if (tempSwitchView == 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {
                                SensorsUI().RealTimeHealthSubGraphMe(this@HomeActivity)
                            }
                        }
                    } else if (tempSwitchTabsMe == 2) {
                        if (tempSwitchView == 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {
                                SensorsUI().RealTimeEnvTwoPointOMe(this@HomeActivity)
                            }
                        } else if (tempSwitchView == 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .weight(1F)
                            ) {
                                SensorsUI().RealTimeEnvSubGraphMe(this@HomeActivity)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun RealTimeTeammateSmall() {
        val viewModel: ViewModelRealTime = viewModel()
        val tempSwitchPanelsMe: Int by viewModel.tempSwitchPanelsMe.observeAsState(tspm)
        val tempSwitchPanelsTeammate: Int by viewModel.tempSwitchPanelsTeammate.observeAsState(tspt)
        val tempSwitchTabsMe: Int by viewModel.tempSwitchTabsMe.observeAsState(tstm)
        val tempSwitchTabsTeammate: Int by viewModel.tempSwitchTabsTeammate.observeAsState(tstt)
        val tempSwitchTeammate: String by viewModel.tempSwitchTeammate.observeAsState(tst)
        val tempSwitchView: Int by viewModel.tempSwitchView.observeAsState(tsv)
        val tempSplitHomeMe: Int by viewModel.tempSplitHomeMe.observeAsState(0)
        val tempSplitHomeTeammate: Int by viewModel.tempSplitHomeTeammate.observeAsState(0)
        viewModel.onUiViewChange(
            tempSwitchPanelsMe,
            tempSwitchPanelsTeammate,
            tempSwitchTabsMe,
            tempSwitchTabsTeammate,
            tempSwitchTeammate,
            tempSwitchView,
            tempSplitHomeMe,
            tempSplitHomeTeammate
        )

        Row {
            Scaffold(
                topBar = {
                    TopAppBar(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.35f)
                        .padding(top = 3.dp, start = 3.dp, end = 3.dp, bottom = 3.dp)
                        .graphicsLayer {
                            shape = RoundedCornerShape(12.dp)
                            clip = true
                        },
                        elevation = 10.dp,
                        title = {
                            Text(
                                text = tempSwitchTeammate,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = customfont,
                                modifier = Modifier.padding(3.dp),
                                fontSize = 18.sp,
                                overflow = TextOverflow.Visible,

                                )
                        },
                        actions = {

                            //create the row layout to arrange the action button and dropdownMenu
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(.20f)
                                    .fillMaxHeight()
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.End
                            )
                            {

                                IconButton(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(140.dp, 60.dp)
                                        .weight(1F),
                                    onClick = {
                                        //changeView = 0
                                        //changeMap = 0
                                        //warningsScreen = 0
                                        //vitalsScreen = 0
                                        //closeTeammate = 0
                                        //closeSmallTeammate = 1
                                        //val navigate = Intent(this@HomeActivity, HomeActivity::class.java)
                                        //startActivity(navigate)
                                        viewModel.onUiViewChange(
                                            tempSwitchPanelsMe,
                                            2,
                                            tempSwitchTabsMe,
                                            tempSwitchTabsTeammate,
                                            tempSwitchTeammate,
                                            tempSwitchView,
                                            tempSplitHomeMe,
                                            tempSplitHomeTeammate
                                        )
                                    }

                                ) {
                                    Icon(

                                        painter = painterResource(R.drawable.arrow_up),
                                        "Expand Button", modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        tint = Color.White
                                    )
                                }
                                //create the space for size of 3.Dp
                                Spacer(modifier = Modifier.size(1.dp))

                                // Environmental button
                                IconButton(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(140.dp, 60.dp)
                                        .weight(1F),
                                    onClick = {
                                        if (tempSwitchPanelsMe == 1) {
                                            //closeTeammate = 1
                                            //closeSmallTeammate = 1
                                            //changeMapMe = 0
                                            viewModel.onUiViewChange(
                                                tempSwitchPanelsMe,
                                                0,
                                                tempSwitchTabsMe,
                                                tempSwitchTabsTeammate,
                                                tempSwitchTeammate,
                                                tempSwitchView,
                                                tempSplitHomeMe,
                                                tempSplitHomeTeammate
                                            )
                                            val navigate =
                                                Intent(this@HomeActivity, MapActivity::class.java)
                                            startActivity(navigate)
                                        } else {
                                            //closeTeammate = 1
                                            //closeSmallTeammate = 1
                                            //selectedMember = "Me"
                                            //changeMapMe = 0
                                            //val navigate = Intent(this@HomeActivity, HomeActivity::class.java)
                                            //startActivity(navigate)
                                            viewModel.onUiViewChange(
                                                tempSwitchPanelsMe,
                                                0,
                                                tempSwitchTabsMe,
                                                tempSwitchTabsTeammate,
                                                tempSwitchTeammate,
                                                tempSwitchView,
                                                tempSplitHomeMe,
                                                tempSplitHomeTeammate
                                            )
                                        }
                                    }

                                ) {
                                    Icon(

                                        painter = painterResource(R.drawable.close_button),
                                        "Close Button", modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp),
                                        tint = Color.White
                                    )
                                }
                                //create the space for size of 3.Dp
                                Spacer(modifier = Modifier.size(3.dp))
                            }
                        }
                    )
                }
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val warningSign = SensorsUI().GetWarningLightInt(
                                this@HomeActivity, selectedMember
                            )
                            if (warningSign == 0) {
                                Text(
                                    text = "  No Warnings for the User",
                                    modifier = Modifier
                                        .padding(bottom = 2.dp)
                                        .align(Alignment.CenterVertically),
                                    color = Color.White,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp
                                )
                            } else if (warningSign == 1) {
                                SensorsUI().RealTimeSingleWarning(this@HomeActivity)
                            }
                        }
                    }
                }
            }
        }
    }

    /* The above code is the main code for the RealTimeTeammate.kt file. This file is responsible for
    the Real Time Teammate screen. This screen is the screen that is displayed when the user selects
    a teammate from the dropdown menu. This screen is similar to the Real Time screen, but it
    displays the data of the teammate that the user selected. */
    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun RealTimeTeammate() {

        val viewModel: ViewModelRealTime = viewModel()
        val tempSwitchPanelsMe: Int by viewModel.tempSwitchPanelsMe.observeAsState(tspm)
        val tempSwitchPanelsTeammate: Int by viewModel.tempSwitchPanelsTeammate.observeAsState(tspt)
        val tempSwitchTabsMe: Int by viewModel.tempSwitchTabsMe.observeAsState(tstm)
        val tempSwitchTabsTeammate: Int by viewModel.tempSwitchTabsTeammate.observeAsState(tstt)
        val tempSwitchTeammate: String by viewModel.tempSwitchTeammate.observeAsState(tst)
        val tempSwitchView: Int by viewModel.tempSwitchView.observeAsState(tsv)
        val tempSplitHomeMe: Int by viewModel.tempSplitHomeMe.observeAsState(0)
        val tempSplitHomeTeammate: Int by viewModel.tempSplitHomeTeammate.observeAsState(0)
        viewModel.onUiViewChange(
            tempSwitchPanelsMe,
            tempSwitchPanelsTeammate,
            tempSwitchTabsMe,
            tempSwitchTabsTeammate,
            tempSwitchTeammate,
            tempSwitchView,
            tempSplitHomeMe,
            tempSplitHomeTeammate
        )

        Row {
            Scaffold(topBar = {
                TopAppBar(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.13f)
                    .padding(top = 3.dp, start = 3.dp, end = 3.dp, bottom = 3.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(12.dp)
                        clip = true
                    }, elevation = 10.dp, title = {
                    Text(
                        text = selectedMember,
                        color = Color.White,
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterVertically),
                        fontSize = 15.sp,
                        overflow = TextOverflow.Visible,

                        )
                }, actions = {

                    //create the row layout to arrange the action button and dropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.85f)
                            .fillMaxHeight()
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        // Home Action button
                        IconButton(modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterVertically)
                            .size(60.dp, 60.dp)
                            .weight(0.5F),
                            onClick = {
                                viewModel.onUiViewChange(
                                    tempSwitchPanelsMe,
                                    tempSwitchPanelsTeammate,
                                    tempSwitchTabsMe,
                                    0,
                                    tempSwitchTeammate,
                                    tempSwitchView,
                                    tempSplitHomeMe,
                                    0
                                )
                            }) {
                            if (tempSwitchTabsTeammate == 0) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = Color.Green
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = Color.White
                                )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // Health Action button
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(140.dp, 60.dp)
                            .weight(1F), onClick = {
                            viewModel.onUiViewChange(
                                tempSwitchPanelsMe,
                                tempSwitchPanelsTeammate,
                                tempSwitchTabsMe,
                                1,
                                tempSwitchTeammate,
                                tempSwitchView,
                                tempSplitHomeMe,
                                tempSplitHomeTeammate
                            )
                        }) {
                            if (tempSwitchTabsTeammate == 1) {
                                Text(
                                    text = "Health",
                                    color = Color.Green,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Visible,

                                    )
                            } else {
                                Text(
                                    text = "Health",
                                    color = Color.White,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Visible,

                                    )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // Environmental button
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(140.dp, 60.dp)
                            .weight(1.5F), onClick = {
                            viewModel.onUiViewChange(
                                tempSwitchPanelsMe,
                                tempSwitchPanelsTeammate,
                                tempSwitchTabsMe,
                                2,
                                tempSwitchTeammate,
                                tempSwitchView,
                                tempSplitHomeMe,
                                tempSplitHomeTeammate
                            )
                        }

                        ) {
                            if (tempSwitchTabsTeammate == 2) {
                                Text(
                                    text = "Environment",
                                    color = Color.Green,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(
                                    text = "Environment",
                                    color = Color.White,
                                    fontFamily = customfont,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))

                        // retract button
                        IconButton(
                            modifier = Modifier
                                .padding(0.dp)
                                .size(140.dp, 60.dp)
                                .weight(0.5F),
                            onClick = {
                                viewModel.onUiViewChange(
                                    tempSwitchPanelsMe,
                                    1,
                                    tempSwitchTabsMe,
                                    0,
                                    selectedMember,
                                    tempSwitchView,
                                    tempSplitHomeMe,
                                    tempSplitHomeTeammate
                                )
                            }

                        ) {
                            Icon(

                                painter = painterResource(R.drawable.arrow_down),
                                "Expand Button", modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }

                        // close button
                        IconButton(modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterVertically)
                            .size(60.dp, 60.dp)
                            .weight(0.5F),
                            onClick = {
                                if (tempSwitchPanelsMe == 1) {
                                    //closeTeammate = 1
                                    //changeMapMe = 0
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        0,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                    val navigate =
                                        Intent(this@HomeActivity, MapActivity::class.java)
                                    startActivity(navigate)
                                } else {
                                    //closeTeammate = 1
                                    //selectedMember = "Me"
                                    //changeMapMe = 0
                                    //val navigate = Intent(this@HomeActivity, HomeActivity::class.java)
                                    //startActivity(navigate)
                                    viewModel.onUiViewChange(
                                        tempSwitchPanelsMe,
                                        0,
                                        tempSwitchTabsMe,
                                        tempSwitchTabsTeammate,
                                        tempSwitchTeammate,
                                        tempSwitchView,
                                        tempSplitHomeMe,
                                        tempSplitHomeTeammate
                                    )
                                }
                            }

                        ) {
                            Icon(

                                painter = painterResource(R.drawable.close_button),
                                "Close Button",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(1.dp))
                    }
                })
            }) {
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp)
                    ) {
                        if (tempSwitchTabsTeammate == 0) {

                            if (tempSplitHomeTeammate == 0) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {

                                    // Teammate warnings panel (retracted)
                                    ExpandablePanelHeader(
                                        title = "Warnings",
                                        arrowPointingUp = false
                                    ) {
                                        viewModel.onUiViewChange(
                                            tempSwitchPanelsMe,
                                            tempSwitchPanelsTeammate,
                                            tempSwitchTabsMe,
                                            tempSwitchTabsTeammate,
                                            tempSwitchTeammate,
                                            tempSwitchView,
                                            tempSplitHomeMe,
                                            1
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .fillMaxSize()
                                            .weight(3F),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        val warningSign = SensorsUI().GetWarningLightInt(
                                            this@HomeActivity, selectedMember
                                        )
                                        if (warningSign == 0) {
                                            Text(
                                                text = "  No Warnings for the User",
                                                modifier = Modifier
                                                    .padding(bottom = 2.dp)
                                                    .align(Alignment.CenterVertically),
                                                color = Color.White,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 20.sp
                                            )
                                        } else if (warningSign == 1) {
                                            SensorsUI().RealTimeSingleWarning(this@HomeActivity)
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {

                                    // Teammate vitals panel (retracted)
                                    ExpandablePanelHeader(
                                        title = "Vitals",
                                        arrowPointingUp = true
                                    ) {
                                        viewModel.onUiViewChange(
                                            tempSwitchPanelsMe,
                                            tempSwitchPanelsTeammate,
                                            tempSwitchTabsMe,
                                            tempSwitchTabsTeammate,
                                            tempSwitchTeammate,
                                            tempSwitchView,
                                            tempSplitHomeMe,
                                            2
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .fillMaxSize()
                                            .weight(3F),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        SensorsUI().RealTimeVitalSubGraph(this@HomeActivity)

                                    }


                                }
                            } else if (tempSplitHomeTeammate == 1) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {

                                    // Teammate warnings panel (expanded)
                                    ExpandablePanelHeader(
                                        title = "Warnings",
                                        arrowPointingUp = true
                                    ) {
                                        viewModel.onUiViewChange(
                                            tempSwitchPanelsMe,
                                            tempSwitchPanelsTeammate,
                                            tempSwitchTabsMe,
                                            0,
                                            tempSwitchTeammate,
                                            tempSwitchView,
                                            tempSplitHomeMe,
                                            0
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .fillMaxSize()
                                            .weight(3.3F),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        val warningSign = SensorsUI().GetWarningLightInt(
                                            this@HomeActivity, selectedMember
                                        )
                                        if (warningSign == 0) {
                                            Text(
                                                text = "  No Warnings for the User",
                                                modifier = Modifier
                                                    .padding(bottom = 2.dp)
                                                    .align(Alignment.CenterVertically),
                                                color = Color.White,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 20.sp
                                            )
                                        } else if (warningSign == 1) {
                                            SensorsUI().RealTimeSingleWarning(this@HomeActivity)
                                        }

                                    }


                                }
                            } else if (tempSplitHomeTeammate == 2) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {

                                    // Teammate vitals panel (expanded)
                                    ExpandablePanelHeader(
                                        title = "Vitals",
                                        arrowPointingUp = false
                                    ) {
                                        viewModel.onUiViewChange(
                                            tempSwitchPanelsMe,
                                            tempSwitchPanelsTeammate,
                                            tempSwitchTabsMe,
                                            0,
                                            tempSwitchTeammate,
                                            tempSwitchView,
                                            tempSplitHomeMe,
                                            0
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .fillMaxSize()
                                            .weight(3.3F),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        SensorsUI().RealTimeVitalSubGraph(this@HomeActivity)

                                    }


                                }
                            }


                        } else if (tempSwitchTabsTeammate == 1) {

                            if (tempSwitchView == 0) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {
                                    SensorsUI().RealTimeHealthTwoPointO(this@HomeActivity)
                                }
                            } else if (tempSwitchView == 1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {
                                    SensorsUI().RealTimeHealthSubGraphTeammate(this@HomeActivity)
                                }
                            }


                        } else if (tempSwitchTabsTeammate == 2) {

                            if (tempSwitchView == 0) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {
                                    SensorsUI().RealTimeEnvTwoPointO(this@HomeActivity)
                                }
                            } else if (tempSwitchView == 1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(1.dp)
                                        .weight(1F)
                                ) {
                                    SensorsUI().RealTimeEnvSubGraphTeammate(this@HomeActivity)
                                }
                            }


                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MapboxScreen() {
        val viewModel: ViewModelRealTime = viewModel()  // :PinSelectMember:
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            AndroidView(modifier = Modifier, factory = { context ->
                ResourceOptionsManager.getDefault(
                    context, context.getString(R.string.MAPBOX_DOWNLOADS_TOKEN)
                )

                //create map
                val initialCameraOptions =
                    CameraOptions.Builder().center(MapActivity.point).zoom(MapActivity.zoom).build()
                val mapInitOptions = MapInitOptions(
                    context,
                    MapInitOptions.getDefaultResourceOptions(context),
                    MapInitOptions.getDefaultMapOptions(context),
                    MapInitOptions.defaultPluginList,
                    initialCameraOptions,
                    true
                )
                val mapView = MapView(context, mapInitOptions)
                mapView.apply {
                    getMapboxMap().loadStyleUri(Style.DARK)
                    for (i in teammateName.indices) {
                        addAnnotationToMap(mapView, i, onPinClick = {
                            selectMemberOnMap(i, viewModel, this@HomeActivity)
                        })
                    }
                }
            })
        }
    }

    fun addAnnotationToMap(mapView: MapView, i: Int, onPinClick: () -> Unit): View {
        // Create an instance of the Annotation API and get the PointAnnotationManager.

        try {
            val pin = Teammates().returnPins(teammateName[i], this@HomeActivity)

            val wholeLongitude = myLongitude.getFromDatabase(this@HomeActivity, teammateID[i])
            val wholeLatitude = myLatitude.getFromDatabase(this@HomeActivity, teammateID[i])
            val delDate = "°"
            val longitudeArray = wholeLongitude.split(delDate)
            val latitudeArray = wholeLatitude.split(delDate)
            val longitude = longitudeArray[0].toDouble()
            val latitude = latitudeArray[0].toDouble()

            val location: Point = Point.fromLngLat(longitude, latitude)

            bitmapFromDrawableRes(this@HomeActivity, pin)?.let {
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                // Set options for the resulting symbol layer.
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    // Define a geographic coordinate.
                    .withPoint(location)
                    // Specify the bitmap you assigned to the point annotation
                    // The bitmap will be added to map style automatically.
                    .withIconImage(it)
                // Add a click listener to the annotation
                pointAnnotationManager.addClickListener {
                    // Handle click event here
//                    Toast.makeText(this@HomeActivity, "${teammateName[i]}", Toast.LENGTH_SHORT).show()
                    onPinClick()  // :PinSelectMember:
                    true
                }
                // Add the resulting pointAnnotation to the map.
                pointAnnotationManager.create(pointAnnotationOptions)
            }
        } catch (e: Exception) {
            //handle exception
        }

        return mapView
    }

    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}