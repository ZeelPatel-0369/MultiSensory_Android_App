package com.example.sixthsenseprojectv1

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sixthsenseprojectv1.sensors.*
import java.time.LocalDateTime

var tspm: Int = 0
var tspt: Int = 0
var tstm: Int = 0
var tstt: Int = 0
var tst: String = "Me"
var tsv: Int = 0

val allSensorsArray: ArrayList<Sensors> = arrayListOf<Sensors>()
val environmentSensorsArray: ArrayList<Sensors> = arrayListOf<Sensors>()
val healthSensorsArray: ArrayList<Sensors> = arrayListOf<Sensors>()
val vitalSensorsArray: ArrayList<Sensors> = arrayListOf<Sensors>()
val fakeSensorsArray: ArrayList<Sensors> = arrayListOf<Sensors>()

val myTemperature = Temperature(1, "Temperature", 0, 0, null)
val myHumidity = Humidity(1, "Humidity", 0, 0, null)
val myPressure = Pressure(1, "Pressure", 0, 0, null)
val myAltitude = Altitude(1, "Altitude", 0, 0, null)
val myTVOC = TVOC(1, "TVOC", 0, 0, null)
val myECO2 = ECO2(1, "ECO2", 0, 0, null)
val myLuminosity = Luminosity(1, "Luminosity", 0, 0, null)
val myLongitude = Longitude(1, "Longitude", 0, 0, null)
val myLatitude = Latitude(1, "Latitude", 0, 0, null)

@RequiresApi(Build.VERSION_CODES.O)
val myTime = Time(1, "Time", LocalDateTime.now(), 0, null)
val myHeartRate = HeartRate(1, "Heart Rate", 0, 0, null)
val myOxygenSaturation = OxygenSaturation(1, "Oxygen Saturation", 0, 0, null)
val myBodyTemperature = BodyTemperature(1, "Body Temperature", 0, 0, null)
val myRespiratory = Respiratory(1, "Respiratory", 0, 0, null)
val myAccelerometer = Accelerometer(1, "Accelerometer", 0, 0, null)
val mySystolic = Systolic(1, "Systolic", 0, 0, null)
val myDiastolic = Diastolic(1, "Diastolic", 0, 0, null)

val teammateName = arrayOf("Me", "Baker", "Carter", "Dean", "John", "Tim")
val teammateID = arrayOf(1, 2, 3, 4, 5, 6)
val squad = arrayOf(teammateID, teammateName)
val sensorNames = arrayOf(
    "Temperature",
    "Humidity",
    "Pressure",
    "Altitude",
    "TVOC",
    "ECO2",
    "Luminosity",
    "Longitude",
    "Latitude",
    "Time",
    "Heart Rate",
    "Oxygen Saturation",
    "Body Temperature",
    "Respiratory",
    "Accelerometer",
    "Systolic",
    "Diastolic"
)
val shortSensorNames = arrayOf(
    "E.T.", "Hum.", "P.S.I.", "Alt.", "TVOC", "ECO2",
    "Lum.", "Long.", "Lat.", "Time", "H.R.", "SPO2", "B.T.", "R.T", "Acc.", "Syst.", "Dias."
)
val vitalSensorNames = arrayOf("Heart Rate", "Oxygen Saturation", "Body Temperature", "Respiratory")
val fakeSensorNames = arrayOf("Respiratory", "Accelerometer", "Systolic", "Diastolic")

var selectedSensor = "Temperature"
var selectedMember = "Me"
var selectedSensorTwoPoint0 = "Temperature"
var selectedMemberTwoPoint0 = "Me"

var connectionChange: Int = 0

val sensorNamesWarningCheck = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

class GlobalVariable {
    @RequiresApi(Build.VERSION_CODES.O)
    fun addSensorsToArray() {
        if (allSensorsArray.isEmpty()) {
            allSensorsArray.add(myTemperature)
            allSensorsArray.add(myHumidity)
            allSensorsArray.add(myPressure)
            allSensorsArray.add(myAltitude)
            allSensorsArray.add(myTVOC)
            allSensorsArray.add(myECO2)
            allSensorsArray.add(myLuminosity)
            allSensorsArray.add(myLongitude)
            allSensorsArray.add(myLatitude)
            allSensorsArray.add(myTime)
            allSensorsArray.add(myHeartRate)
            allSensorsArray.add(myOxygenSaturation)
            allSensorsArray.add(myBodyTemperature)
            allSensorsArray.add(myRespiratory)
            allSensorsArray.add(myAccelerometer)
            allSensorsArray.add(mySystolic)
            allSensorsArray.add(myDiastolic)
        }
        if (vitalSensorsArray.isEmpty()) {
            vitalSensorsArray.add(myHeartRate)
            vitalSensorsArray.add(myOxygenSaturation)
            vitalSensorsArray.add(myBodyTemperature)
            vitalSensorsArray.add(myRespiratory)
        }
        if (fakeSensorsArray.isEmpty()) {
            fakeSensorsArray.add(myRespiratory)
            fakeSensorsArray.add(myAccelerometer)
            fakeSensorsArray.add(mySystolic)
            fakeSensorsArray.add(myDiastolic)
        }
        if (environmentSensorsArray.isEmpty()) {
            environmentSensorsArray.add(myTemperature)
            environmentSensorsArray.add(myHumidity)
            environmentSensorsArray.add(myPressure)
            environmentSensorsArray.add(myAltitude)
            environmentSensorsArray.add(myTVOC)
            environmentSensorsArray.add(myECO2)
            environmentSensorsArray.add(myLuminosity)
            environmentSensorsArray.add(myLongitude)
            environmentSensorsArray.add(myLatitude)
            environmentSensorsArray.add(myTime)
        }
        if (healthSensorsArray.isEmpty()) {
            healthSensorsArray.add(myHeartRate)
            healthSensorsArray.add(myOxygenSaturation)
            healthSensorsArray.add(myBodyTemperature)
            healthSensorsArray.add(myRespiratory)
            healthSensorsArray.add(myAccelerometer)
            healthSensorsArray.add(mySystolic)
            healthSensorsArray.add(myDiastolic)
        }
    }
}