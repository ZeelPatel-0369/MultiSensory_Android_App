package com.example.sixthsenseprojectv1.database

//import as needed
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

lateinit var userList: ArrayList<SensorModelDB>

//creating a constructor for our database handler that handles all the database code
class DBHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    //method for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {

        //create an sqlite query and we are setting our column names along with their data types
        //main table to store all data
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USERID_COL + " TEXT,"
                + NAME_COL + " TEXT,"
                + DATA_COL + " TEXT,"
                + WARNING_COL + " TEXT,"
                + TIME_COL + " TEXT)")

        //create an sqlite query and we are setting our column names along with their data types
        //sub table to store only the warning data
        val query1 = ("CREATE TABLE " + TABLE_NAME_1 + " ("
                + ID_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WARNING_USER_COL + " TEXT,"
                + WARNING_NAME_COL + " TEXT,"
                + WARNING_DATA_COL + " TEXT,"
                + WARNING_WARNING_COL + " TEXT,"
                + WARNING_TIME_COL + " TEXT)")

        //call a exec sql method to execute above sql queries
        db.execSQL(query)
        db.execSQL(query1)
    }

    //method to add new sensor data to our sqlite database in the main table
    fun addNewSensorData(
        userID: String?, sensorName: String?, data: String?,
        warning: String?, Time: String?
    ): Long {

        //variable for our sqlite database and calling writable
        // method as we are writing data in our database
        val db = this.writableDatabase

        //variable for content values
        val values = ContentValues()

        //pass all values along with its key and value pair
        values.put(USERID_COL, userID)
        values.put(NAME_COL, sensorName)
        values.put(DATA_COL, data)
        values.put(WARNING_COL, warning)
        values.put(TIME_COL, Time)

        //pass content values to our table
        val returnValue = db.insert(TABLE_NAME, null, values)

        //close our database
        db.close()

        //return value - if (-1) then error; if > 0 then success
        return returnValue
    }

    //method that returns the number of rows in the main table to help with testing
    fun getSize(): String? {
        val db = this.writableDatabase
        //SQL query that returns the number of rows in main table
        val tempQ = "SELECT COUNT(*) FROM $TABLE_NAME"
        //initialize our cursor
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)
        var result = ""
        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our string
                result = sensorCursor.getString(0)
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }
        //close the cursor
        sensorCursor.close()
        //close the database
        db.close()
        return result
    }

    //method that returns the number of rows in the warning table to help with testing
    fun getSizeWarning(): String? {
        val db = this.writableDatabase
        //SQL query that returns the number of rows in the warning table
        val tempQ = "SELECT COUNT(*) FROM $TABLE_NAME_1"
        //initialize our cursor
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)
        var result = ""
        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our string
                result = sensorCursor.getString(0)
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }
        //close the cursor
        sensorCursor.close()
        //close the database
        db.close()
        return result
    }


    //method to add new warning sensor data to our sqlite database in the warning sub table
    fun addNewWarningData(
        userID: String?, sensorName: String?, data: String?,
        warning: String?, Time: String?
    ): Long {

        //variable for our sqlite database and calling writable
        // method as we are writing data in our database
        val db = this.writableDatabase

        //variable for content values
        val values = ContentValues()

        //pass all values along with its key and value pair
        values.put(WARNING_USER_COL, userID)
        values.put(WARNING_NAME_COL, sensorName)
        values.put(WARNING_DATA_COL, data)
        values.put(WARNING_WARNING_COL, warning)
        values.put(WARNING_TIME_COL, Time)

        //pass content values to our table
        val returnValue = db.insert(TABLE_NAME_1, null, values)

        //close our database
        db.close()

        //return value - if (-1) then error; if > 0 then success
        return returnValue
    }

    //method checks to see if table exists
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        //checks if the table exists already
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1)

        //create table
        onCreate(db)
    }

    //defines the name for DB, Version, Tables, and Columns
    companion object {

        //variable for our database name
        private const val DB_NAME = "msdb"

        //variable for database version
        private const val DB_VERSION = 1

        //variables for main table with sensor data
        const val TABLE_NAME = "mysensors"
        const val ID_COL = "id"
        const val USERID_COL = "user"
        const val NAME_COL = "name"
        private const val DATA_COL = "data"
        private const val WARNING_COL = "warning"
        const val TIME_COL = "time"

        //variables for sub table with warning sensor data
        const val TABLE_NAME_1 = "mywarnings"
        const val ID_COL_1 = "id1"
        private const val WARNING_USER_COL = "user1"
        const val WARNING_NAME_COL = "name1"
        private const val WARNING_DATA_COL = "data1"
        private const val WARNING_WARNING_COL = "warning1"
        private const val WARNING_TIME_COL = "time1"
    }

    //method for reading all the sensor data regardless of sensor name
    fun readAllData(): ArrayList<SensorModelDB>? {

        //database variable for reading our database
        val db = this.readableDatabase

        //cursor with query to read data from database
        val sensorCursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        //array list for storing the data
        val sensorModelArrayList: ArrayList<SensorModelDB> = ArrayList()

        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our array list
                sensorModelArrayList.add(
                    SensorModelDB(
                        sensorCursor.getString(1),
                        sensorCursor.getString(2),
                        sensorCursor.getString(3),
                        sensorCursor.getString(4),
                        sensorCursor.getString(5)
                    )
                )
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }

        //close our cursor
        sensorCursor.close()
        //close the database
        db.close()
        //returning our array list with all the data
        return sensorModelArrayList
    }

    //method for reading latest data of one sensor each
    fun readLatestPerSensor(sensorName: String, usersID: Int): ArrayList<SensorModelDB> {

        //database variable for reading our database
        val db = this.readableDatabase

        //call on generateLatestData to generate a query because
        // doing it here gave errors on the WHERE clause
        val tempQ = generateLatestData(sensorName, usersID) +
                " ORDER BY $ID_COL DESC LIMIT 1"

        //cursor with query to read data from database based on query generated above
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)

        //array list for storing the data
        val sensorModelArrayList: ArrayList<SensorModelDB> = ArrayList()

        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our array list
                sensorModelArrayList.add(
                    SensorModelDB(
                        sensorCursor.getString(1),
                        sensorCursor.getString(2),
                        sensorCursor.getString(3),
                        sensorCursor.getString(4),
                        sensorCursor.getString(5)
                    )
                )
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }

        //close our cursor
        sensorCursor.close()
        //close the database
        db.close()
        //returning our array list with all the data
        return sensorModelArrayList
    }

    //method for reading latest warning of one sensor each
    fun readLatestWarningPerSensor(sensorName: String, usersID: Int): String {

        //database variable for reading our database
        val db = this.readableDatabase

        //call on generateLatestData to generate a query
        // because doing it here gave errors on the WHERE clause
        val tempQ = generateLatestData(sensorName, usersID) + " ORDER BY $ID_COL DESC LIMIT 1"

        //cursor with query to read data from database based on query generated above
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)

        //string for storing the data
        var returnValue = ""

        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our string
                returnValue = sensorCursor.getString(4)
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }

        //close our cursor
        sensorCursor.close()
        //close the database
        db.close()
        //returning our string with the data
        return returnValue
    }

    //method for reading all data of one sensor each
    fun readAllPerSensor(sensorName: String, usersID: Int): ArrayList<SensorModelDB> {

        //database variable for reading our database
        val db = this.readableDatabase

        //call on generateLatestData to generate a query because doing it here gave errors on the WHERE clause
        val tempQ = generateLatestData(sensorName, usersID)

        //cursor with query to read data from database based on query generated above
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)

        //array list for storing the data
        val sensorModelArrayList: ArrayList<SensorModelDB> = ArrayList()

        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our array list
                sensorModelArrayList.add(
                    SensorModelDB(
                        sensorCursor.getString(1),
                        sensorCursor.getString(2),
                        sensorCursor.getString(3),
                        sensorCursor.getString(4),
                        sensorCursor.getString(5)
                    )
                )
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }

        //close our cursor
        sensorCursor.close()
        //close the database
        db.close()
        //returning our array list with all the data
        return sensorModelArrayList
    }

    //method to delete all data from the main table
    fun deleteAll() {

        //database variable for writing our database
        val db = this.writableDatabase

        //delete all data in the table
        db.execSQL("delete from " + TABLE_NAME)

        //close database
        db.close()
    }

    //method to delete all data from the sub table
    fun deleteAllWarning() {

        //database variable for writing our database
        val db = this.writableDatabase

        //delete all data in the table
        db.execSQL("delete from " + TABLE_NAME_1)

        //close database
        db.close()
    }

    //method to retrieve data in the last 10 minutes from sensor table
    fun readTenMinutes(sensorName: String, usersID: Int): ArrayList<SensorModelDB> {
        //database variable for writing our database
        val db = this.writableDatabase

        val tempQ = generateTenMinutesData(sensorName, usersID) + " ORDER BY $ID_COL DESC LIMIT 100"
        //cursor with query to read data from database based on query generated above
        val sensorCursor: Cursor = db.rawQuery(tempQ, null)

        //array list for storing the data
        val sensorArrayList: ArrayList<SensorModelDB> = ArrayList()
        //moving our cursor to first position
        if (sensorCursor.moveToFirst()) {
            do {
                //add the data from cursor to our array list
                sensorArrayList.add(
                    SensorModelDB(
                        sensorCursor.getString(1),
                        sensorCursor.getString(2),
                        sensorCursor.getString(3),
                        sensorCursor.getString(4),
                        sensorCursor.getString(5)
                    )
                )
            } while (sensorCursor.moveToNext())
            //move our cursor to next
        }

        //close our cursor
        sensorCursor.close()
        //close the database
        db.close()
        //returning our array list with all the data
        return sensorArrayList
    }

    //method to generate a query based on the sensor name given from the sensor object
    fun generateLatestData(sensorName: String, usersID: Int): String {

        //variable for storing the query
        var tempQ = ""
        val tempUserID = usersID.toString()

        //change the query based on the sensor name input
        if (sensorName == "Temperature") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Temperature' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Humidity") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Humidity' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Pressure") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Pressure' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Altitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Altitude' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "TVOC") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'TVOC' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "ECO2") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'ECO2' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Luminosity") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Luminosity' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Longitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Longitude' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Latitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Latitude' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Time") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Time' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Heart Rate") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Heart Rate' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Oxygen Saturation") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Oxygen Saturation' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Body Temperature") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Body Temperature' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Respiratory") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Respiratory' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Systolic") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Systolic' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Accelerometer") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Accelerometer' AND $USERID_COL = $tempUserID"
        } else if (sensorName == "Diastolic") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Diastolic' AND $USERID_COL = $tempUserID"
        }

        //return the updated query
        return tempQ
    }
    //method to generate a query based on the sensor name given from the sensor object, similar to
    //generateLatestData except it has a clause in WHERE that returns data saved in the last 10 minutes only

    fun generateTenMinutesData(sensorName: String, usersID: Int): String {

        //variable for storing the query
        var tempQ = ""
        val tempUserID = usersID.toString()

        //change the query based on the sensor name input
        if (sensorName == "Temperature") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Temperature' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Humidity") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Humidity' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Pressure") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Pressure' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Altitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Altitude' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "TVOC") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'TVOC' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "ECO2") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'ECO2' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Luminosity") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Luminosity' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Longitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Longitude' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Latitude") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Latitude' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Time") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Time' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Heart Rate") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Heart Rate' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Oxygen Saturation") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Oxygen Saturation' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Body Temperature") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Body Temperature' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Respiratory") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Respiratory' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Systolic") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Systolic' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Accelerometer") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Accelerometer' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        } else if (sensorName == "Diastolic") {
            tempQ =
                "SELECT * FROM $TABLE_NAME WHERE $NAME_COL = 'Diastolic' AND $USERID_COL = $tempUserID" +
                        " AND $TIME_COL > datetime('now', '-10 minutes')"
        }

        //return the updated query
        return tempQ
    }

    fun getAllSensorData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportToCSV2(context: Context) {
        val cursor = getAllSensorData()
        val csvHeader = "ID,USERID,NAME,DATA,WARNING,TIME\n"
        val csvBody = StringBuilder()
        csvBody.append(csvHeader)
        while (cursor.moveToNext()) {
            val row = StringBuilder()
            row.append(cursor.getString(0)).append(",")
            row.append(cursor.getString(1)).append(",")
            row.append(cursor.getString(2)).append(",")
            row.append(cursor.getString(3)).append(",")
            row.append(cursor.getString(4)).append(",")
            row.append(cursor.getString(5)).append("\n")
            csvBody.append(row)
        }
        cursor.close()

        val fileName = "SensorData_" + LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMMdd_HH-mm-ss")) + ".csv"
        val relativeLocation =
            Environment.DIRECTORY_DOCUMENTS + File.separator + "SixthSenseImports" // Directory path where you want to save the file
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
        }

        val uri = resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { uri ->
            try {
                val outputStream = resolver.openOutputStream(uri)
                outputStream?.write(csvBody.toString().toByteArray())
                outputStream?.close()

                Toast.makeText(context, "File Exported Successfully in $uri", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Error Occurred while exporting File: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun getWarningsToExport(sensorName: String?): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME_1 ORDER BY $NAME_COL ASC", null)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportWarningsCsv(context: Context, sensorName: String?) {
        val cursor = getWarningsToExport(sensorName)
        val csvHeader = "ID,NAME,WARNING,TIME\n"
        val csvBody = StringBuilder()
        csvBody.append(csvHeader)
        while (cursor.moveToNext()) {
            val row = StringBuilder()
            row.append(cursor.getString(cursor.getColumnIndexOrThrow(ID_COL))).append(",")
            row.append(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COL))).append(",")
            row.append(cursor.getString(cursor.getColumnIndexOrThrow(WARNING_COL))).append(",")
            row.append(cursor.getString(cursor.getColumnIndexOrThrow(TIME_COL))).append("\n")
            csvBody.append(row)
        }
        cursor.close()

        val fileName = "warnings.csv"
        val relativeLocation =
            Environment.DIRECTORY_DOCUMENTS + File.separator + "MyApp" // Directory path where you want to save the file
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { uri ->
            try {
                val outputStream = resolver.openOutputStream(uri)
                outputStream?.write(csvBody.toString().toByteArray())
                outputStream?.close()

                Toast.makeText(
                    context,
                    "Warnings File Exported Successfully in $uri",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Error Occurred while exporting Warnings File: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun exportToCSV(context: Context) {
        try {
            val dbHandler: DBHandler = DBHandler(context)
            userList = dbHandler.readAllData()!!
            if (userList.size > 0) {
                Toast.makeText(context, "starting export", Toast.LENGTH_SHORT).show()
                createXlFile(context)
            } else {
                Toast.makeText(context, "list are empty", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            //handle exception
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun createXlFile(context: Context) {

        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        val wb: Workbook = HSSFWorkbook()
        var cell: Cell? = null
        var sheet: Sheet? = null
        sheet = wb.createSheet("Export")
        //Now column and row
        val row: Row = sheet.createRow(0)
        cell = row.createCell(0)
        cell.setCellValue("Index")
        cell = row.createCell(1)
        cell.setCellValue("User ID")
        cell = row.createCell(2)
        cell.setCellValue("Sensor Name")
        cell = row.createCell(3)
        cell.setCellValue("Data")
        cell = row.createCell(4)
        cell.setCellValue("Warning Indicator")
        cell = row.createCell(5)
        cell.setCellValue("Time")

        //column width
        sheet.setColumnWidth(0, 20 * 200)
        sheet.setColumnWidth(1, 30 * 200)
        sheet.setColumnWidth(2, 30 * 200)
        sheet.setColumnWidth(3, 30 * 200)
        sheet.setColumnWidth(4, 30 * 200)
        sheet.setColumnWidth(5, 30 * 200)
        for (i in 0 until userList.size) {
            val row1: Row = sheet.createRow(i + 1)
            cell = row1.createCell(0)
            cell.setCellValue(i.toString())
            cell = row1.createCell(1)
            cell.setCellValue(userList.get(i).userID)
            cell = row1.createCell(2)
            cell.setCellValue(userList.get(i).sensorName)
            cell = row1.createCell(3)
            cell.setCellValue(userList.get(i).data)
            cell = row1.createCell(4)
            cell.setCellValue(userList.get(i).warning)
            cell = row1.createCell(5)
            cell.setCellValue(userList.get(i).Time)
            sheet.setColumnWidth(0, 20 * 200)
            sheet.setColumnWidth(1, 30 * 200)
            sheet.setColumnWidth(2, 30 * 200)
            sheet.setColumnWidth(3, 30 * 200)
            sheet.setColumnWidth(4, 30 * 200)
            sheet.setColumnWidth(5, 30 * 200)
        }
        val folderName = "SixthSenseImports"
        val fileName = folderName + LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMMdd_HH-mm-ss")) + ".xls"
        val path: String =
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
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(path)
            wb.write(outputStream)
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(context, "Excel Created in $path2", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Not OK", Toast.LENGTH_LONG).show()
            try {
                if (outputStream != null) {
                    outputStream.close()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}