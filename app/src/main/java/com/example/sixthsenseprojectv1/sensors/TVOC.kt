package com.example.sixthsenseprojectv1.sensors

//import the classes needed
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.database.SensorModelDB
import java.time.LocalDateTime

//class for TVOC sensor
class TVOC(id: Int, name: String, data: Int, warning: Int, time: LocalDateTime?) :
    Sensors(id, name, data, warning, time) {

    //method to get the data of a sensor from the database
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFromDatabase(context: Context, personID: Int): String {
        var returnData = ""

        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readLatestPerSensor("TVOC", personID)
            //If condition to return empty string if the table has size 0
            if (dbHandler.readLatestPerSensor("TVOC", personID).size == 0) {
                return returnData
            }

            returnData = latestData.get(0).data + " ppm"
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
            returnData = dbHandler.readLatestWarningPerSensor("TVOC", personID).toInt()
            if (dbHandler.readLatestWarningPerSensor("TVOC", personID).isEmpty()) {
                return 0
            }
        } catch (e: Exception) {
            //handle exception
        }

        return returnData.toInt()
    }

    //method to check the warning of a sensor based on the data provided
    override fun checkWarnings(checkData: Int): Int {
        if (checkData < 1000) {
            tempWarning = 1
        } else {
            tempWarning = 0
        }
        return tempWarning
    }

    //method to get the historical data of the sensor
    override fun getHistorical(context: Context, personID: Int): ArrayList<String> {
        val tempDataArray: ArrayList<String> = arrayListOf()
        val tempWarningArray: ArrayList<Int> = arrayListOf()

        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readAllPerSensor("TVOC", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "TVOC") {
                    tempDataArray.add(m.data)
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
        try {
            val dbHandler = DBHandler(context)
            val latestData = dbHandler.readAllPerSensor("TVOC", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "TVOC") {
                    tempDataArray.put(m.Time, m.data)
                }
            }
        } catch (e: Exception) {
            //handle exception
        }

        return tempDataArray
    }

    //method to get the historical data of the sensor
    override fun getHistoricalWarning(context: Context, personID: Int): ArrayList<Int> {
        val tempDataArray: ArrayList<String> = arrayListOf()
        val tempWarningArray: ArrayList<Int> = arrayListOf()
        try {
            val dbHandler: DBHandler = DBHandler(context)
            var latestData: ArrayList<SensorModelDB> = ArrayList<SensorModelDB>()
            latestData = dbHandler.readAllPerSensor("TVOC", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "TVOC") {
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
            latestData = dbHandler.readTenMinutes("TVOC", personID)

            val size = latestData.size - 1
            for (i in 0..size) {
                val m = latestData[i]
                if (m.sensorName == "TVOC") {
                    tempDataArray.add(m.data)
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
        val tvoc = (1000..1187000).random()
        checkWarnings(tvoc)
        addToDatabase(personID, "TVOC", tvoc.toDouble(), tempWarning, current, context)
    }

    override fun generateSQL(context: Context): DBHandler {
        return DBHandler(context)
    }
}
