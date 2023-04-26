package com.example.sixthsenseprojectv1.sensors

//import the classes needed
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.database.SensorModelDB
import java.time.LocalDateTime

//class for Time sensor
class Time(id: Int, name: String, data: LocalDateTime, warning: Int, time: LocalDateTime?) :
    Sensors(id, name, data, warning, time) {

    //method to get the data of a sensor from the database
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFromDatabase(context: Context, personID: Int): String {
        var returnData = ""

        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readLatestPerSensor("Time", personID)
            //returnData = latestData.get(0).Time + " sec"

            val wholeDateTime = latestData[0].Time
            val delDate = "T"
            val splitDateTime = wholeDateTime.split(delDate)
            val date = splitDateTime[0]
            val wholeTime = splitDateTime[1]
            val delTime = "."
            val splitTime = wholeTime.split(delTime)
            val formattedDateTime = splitTime[0] + " " + date

            returnData = formattedDateTime
        } catch (e: Exception) {
            //handle exception
        }

        return returnData
    }

    //method to get the data of a sensor from the database
    override fun getWarningFromDatabase(context: Context, personID: Int): Int {
        var returnData = 0
        try {
            val dbHandler: DBHandler = DBHandler(context)
            returnData = dbHandler.readLatestWarningPerSensor("Time", personID).toInt()
            if (dbHandler.readLatestWarningPerSensor("Time", personID).isEmpty()) {
                return 0
            }
        } catch (e: Exception) {
            //handle exception
        }

        return returnData.toInt()
    }

    //method to check the warning of a sensor based on the data provided
    override fun checkWarnings(checkData: Int): Int {
        tempWarning = 0
        return tempWarning
    }

    //method to get the historical data of the sensor
    override fun getHistorical(context: Context, personID: Int): ArrayList<String> {
        val tempDataArray: ArrayList<String> = arrayListOf()
        val tempWarningArray: ArrayList<Int> = arrayListOf()
        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readAllPerSensor("Time", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "Time") {
                    val wholeDateTime = m.Time
                    val delDate = "T"
                    val splitDateTime = wholeDateTime.split(delDate)
                    val date = splitDateTime[0]
                    val wholeTime = splitDateTime[1]
                    val delTime = "."
                    val splitTime = wholeTime.split(delTime)
                    val formattedDateTime = splitTime[0] + " " + date

                    tempDataArray.add(formattedDateTime)
                    tempWarningArray.add(m.warning.toInt())
                }
            }
        } catch (e: Exception) {
            //handle exception
        }

        return tempDataArray
    }

    //method to get the historical data of the sensor with timestamp
    override fun getHistoricalWithTime(
        context: Context,
        personID: Int
    ): MutableMap<String, String> {
        val tempDataArray: MutableMap<String, String> = mutableMapOf()
//        try{
//            val dbHandler = DBHandler(context)
//            val latestData = dbHandler.readAllPerSensor("Latitude", personID)
//
//            val size = latestData.size - 1
//            for (i in 0..size){
//                val m = latestData[i]
//                if (m.sensorName == "Latitude")
//                {
//                    tempDataArray.put(m.Time, m.data)
//                }
//            }
//        }
//        catch (e: Exception){
//            //handle exception
//        }

        return tempDataArray
    }

    //method to get the historical data of the sensor
    override fun getHistoricalWarning(context: Context, personID: Int): ArrayList<Int> {
        val tempDataArray: ArrayList<String> = arrayListOf()
        val tempWarningArray: ArrayList<Int> = arrayListOf()
        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readAllPerSensor("Time", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "Time") {
                    tempDataArray.add(m.data)
                    tempWarningArray.add(m.warning.toInt())
                }
            }
        } catch (e: Exception) {
            //handle exception
        }

        return tempWarningArray
    }

    override fun getLastTenMinutes(context: Context, personID: Int): ArrayList<String> {
        val tempDataArray: ArrayList<String> = arrayListOf()
        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readTenMinutes("Time", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "Time") {
                    val wholeDateTime = m.Time
                    val delDate = "T"
                    val splitDateTime = wholeDateTime.split(delDate)
                    val date = splitDateTime[0]
                    val wholeTime = splitDateTime[1]
                    val delTime = "."
                    val splitTime = wholeTime.split(delTime)
                    val formattedDateTime = splitTime[0] + " " + date

                    tempDataArray.add(formattedDateTime)
                }
            }
        } catch (e: Exception) {
            //handle exception
        }

        return tempDataArray
    }

    //randomly generate the data for the sensor - use only for fake sensors
    @RequiresApi(Build.VERSION_CODES.O)
    override fun dummyProvider(personID: Int, context: Context) {
        val current = LocalDateTime.now()
        addToDatabase(personID, "Time", 0.toDouble(), 0, current, context)
    }

    override fun generateSQL(context: Context): DBHandler {
        return DBHandler(context)
    }
}
