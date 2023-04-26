package com.example.sixthsenseprojectv1.sensors

//import the classes needed
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.sensorNames
import com.example.sixthsenseprojectv1.sensorNamesWarningCheck
import java.time.LocalDateTime

//abstract sensor object for data processing
abstract class Sensors(id: Int, name: String, data: Any, warning: Int, time: LocalDateTime?) {

    //initialize the variables in the arguments
    private var tempID = id
    private var tempName = name
    private var tempData = data
    var tempWarning = warning
    private var tempTime = time

    //abstract methods that can be edited for each sensor class
    abstract fun getFromDatabase(context: Context, personID: Int): String
    abstract fun getWarningFromDatabase(context: Context, personID: Int): Int
    abstract fun checkWarnings(checkData: Int): Int
    abstract fun getHistorical(context: Context, personID: Int): ArrayList<String>
    abstract fun getHistoricalWithTime(context: Context, personID: Int): Map<String, String>
    abstract fun getHistoricalWarning(context: Context, personID: Int): ArrayList<Int>
    abstract fun getLastTenMinutes(context: Context, personID: Int): ArrayList<String>
    abstract fun dummyProvider(personID: Int, context: Context)
    abstract fun generateSQL(context: Context): DBHandler

    //method to get the id of the person
    fun getID(): Int {
        return tempID
    }

    //method to get the name of sensor
    fun getName(): String {
        return tempName
    }

    //method to get the data
    fun getData(): Any {
        return tempData
    }

    //method to get the warning int of the data
    fun getWarning(): Int {
        return tempWarning
    }

    //method to get the time the data came in
    open fun getTime(): LocalDateTime? {
        return tempTime
    }

    //method to add the data of a sensor to database
    @RequiresApi(Build.VERSION_CODES.O)
    fun addToDatabase(
        id: Int,
        name: String,
        data: Double,
        warning: Int,
        time: LocalDateTime?,
        context: Context
    ) {
        val dbHandler = DBHandler(context)
        //val index = sensorNames.getIndexOf(name)
        dbHandler.addNewSensorData(
            id.toString(),
            name,
            data.toString(),
            warning.toString(),
            time.toString()
        )

        /**val index = sensorNames.indexOf(name)
        if (warning == 1 && id ==1) {
        addWarningData(id,name,data,warning,time,context)
        }
        else if (sensorNamesWarningCheck[index] == 1) {
        addWarningData(id,name,data,warning,time,context)
        }*/

    }

    //method to add warning data to the database
    @RequiresApi(Build.VERSION_CODES.Q)
    fun addWarningData(
        id: Int,
        name: String,
        data: Double,
        warning: Int,
        time: LocalDateTime?,
        context: Context
    ) {
        val dbHandler = DBHandler(context)
        val index = sensorNames.indexOf(name)
        dbHandler.addNewWarningData(
            id.toString(),
            name,
            data.toString(),
            warning.toString(),
            time.toString()
        )
        if (warning == 0) {
            sensorNamesWarningCheck[index] = 0
            dbHandler.exportWarningsCsv(context, name)
        }
        if (warning == 1 && id == 1) {
            addWarningData(id, name, data, warning, time, context)
        } else if (sensorNamesWarningCheck[index] == 1) {
            addWarningData(id, name, data, warning, time, context)
        }
    }

}