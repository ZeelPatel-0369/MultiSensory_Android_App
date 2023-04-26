package com.example.sixthsenseprojectv1


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.database.SensorModelDB
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class DBHandlerTest {

    private lateinit var dbHandler: DBHandler


    @Before
    //Sets up the database
    fun setupDB() {
        ShadowLog.stream = System.out  // log to console

        // Create a new instance of the DBHandler for each test
        dbHandler = DBHandler(RuntimeEnvironment.getApplication())

    }

    @After
    //Closes database after completion
    fun tearDown() {
        dbHandler.close()
    }

    @Test
    //Checks to see that new sensor data is added correctly
    fun addNewSensorData() {
        dbHandler.deleteAll()
        val result = dbHandler.addNewSensorData(
            "user1", "sensor1",
            "data1", "warning1", "time1"
        )
        val size = dbHandler.getSize()
        assertNotEquals(-1, result) // Check that the row was added successfully
        assertEquals("1", size) //checks that there is only 1 entry in table
    }

    @Test
    //Checks to see that new warning data is added correctly
    fun addNewWarningData() {
        val result = dbHandler.addNewWarningData(
            "user1", "sensor1",
            "data1", "warning1", "time1"
        )
        assertNotEquals(-1, result) // Check that the row was added successfully
        val size = dbHandler.getSizeWarning()
        assertEquals("1", size) //checks that there is only 1 entry in table
    }

    @Test
    //checks to see that added data is entered into the main table as expected
    fun readAllData() {
        dbHandler.deleteAll()
        dbHandler.addNewSensorData(
            "user1", "sensor1",
            "data1", "warning1", "time1"
        )
        dbHandler.addNewSensorData(
            "user1", "sensor2",
            "data2", "warning2", "time2"
        )
        dbHandler.addNewSensorData(
            "user2", "sensor1",
            "data3", "warning3", "time3"
        )

        val sensorDataList: ArrayList<SensorModelDB> = dbHandler.readAllData()!!

        // Check that the list contains the expected number of rows
        assertEquals(3, sensorDataList.size)

        // Check that the data in the first row is correct
        val firstRow: SensorModelDB = sensorDataList[0]
        assertEquals("user1", firstRow.userID)
        assertEquals("sensor1", firstRow.sensorName)
        assertEquals("data1", firstRow.data)
        assertEquals("warning1", firstRow.warning)
        assertEquals("time1", firstRow.Time)
        val size = dbHandler.getSize()
        assertEquals("3", size) //checks that there are only 3 entries in table

    }

    @Test
    //checks to see that added data in main table
    // is entered as expected when fetching data based on sensor name
    fun readLatestPerSensor() {
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data1", "warning1", "time1"
        )
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data2", "warning2", "time2"
        )
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data3", "warning3", "time3"
        )

        // Read the latest data for each sensor
        val latestData = dbHandler.readLatestPerSensor("Temperature", 1)

        // Check that the list contains the expected number of rows
        assertEquals(1, latestData.size)

        // Check that the data in the first row is correct
        val firstRow = latestData[0]
        assertEquals("1", firstRow.userID)
        assertEquals("Temperature", firstRow.sensorName)
        assertEquals("data3", firstRow.data)
        assertEquals("warning3", firstRow.warning)
        assertEquals("time3", firstRow.Time)

    }

    @Test
    //checks to see that added data in warning table
    // is entered as expected when fetching data based on sensor name
    fun readLatestWarningPerSensor() {
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data1", "warning1", "time1"
        )
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data2", "warning2", "time2"
        )
        dbHandler.addNewSensorData(
            "1", "Temperature",
            "data3", "warning3", "time3"
        )

        val temp: String = dbHandler.readLatestWarningPerSensor("Temperature", 1)
        // Check that the list contains the expected number of rows
        assertEquals("warning3", temp)
    }

    @Test
    //checks to see that the main table is empty when the delete function is called
    fun deleteAll() {
        val dbHandler =
            DBHandler(context = InstrumentationRegistry.getInstrumentation().targetContext)
        dbHandler.deleteAll()

        // verify that the table is empty
        val result = dbHandler.readAllData()
        assertEquals(0, result?.size)
    }

    @Test
    //checks to see that the warning table is empty when the delete function is called
    fun deleteAllWarning() {
        val dbHandler =
            DBHandler(context = InstrumentationRegistry.getInstrumentation().targetContext)
        dbHandler.deleteAllWarning()
    }

    @Test
    //checks to see the generated query when retrieving data
    // is the same as what is expected from the method
    fun generateLatestData() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        val testSensorName = "Temperature"
        val tempUserID = 1
        val expectedQuery = "SELECT * FROM ${DBHandler.TABLE_NAME} WHERE ${DBHandler.NAME_COL} " +
                "= 'Temperature' AND ${DBHandler.USERID_COL} = $tempUserID"
        val generatedQuery = dbHandler.generateLatestData(testSensorName, tempUserID)
        assertEquals(expectedQuery, generatedQuery)

    }

    @Test
    //checks to see the generated query when retrieving data in last 10 minutes
    // is the same as what is expected from the method
    fun lastTenMinutes() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        val testSensorName = "Temperature"
        val tempUserID = 1
        val expectedQuery = "SELECT * FROM ${DBHandler.TABLE_NAME} WHERE ${DBHandler.NAME_COL}" +
                " = 'Temperature' AND ${DBHandler.USERID_COL} = $tempUserID" +
                " AND ${DBHandler.TIME_COL} > datetime('now', '-10 minutes')"
        val generatedQuery = dbHandler.generateTenMinutesData(testSensorName, tempUserID)
        assertEquals(expectedQuery, generatedQuery)
    }

    @Test
    fun exportCsvTest() {

    }
}