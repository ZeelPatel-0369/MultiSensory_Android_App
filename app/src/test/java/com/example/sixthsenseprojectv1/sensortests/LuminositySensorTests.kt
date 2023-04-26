package com.example.sixthsenseprojectv1.sensortests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.sensors.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)

//This class tests each method inside of the Luminosity sensor object
class LuminositySensorTests {
    @Test
    //tests the getFromDatabase method inside the Luminosity sensor object
    fun getFromDatabase() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAll()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "data1", "warning1", "time1"
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 0, null)
        val temp =
            myTemp.getFromDatabase(InstrumentationRegistry.getInstrumentation().targetContext, 1)
        assertEquals("data1 lx", temp)
    }

    @Test
    //tests the getWarningFromDatabase method inside the Luminosity sensor object
    fun getWarningFromDatabase() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAllWarning()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "23", "24", "time1"
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 23, null)
        val temp = myTemp.getWarningFromDatabase(
            InstrumentationRegistry.getInstrumentation().targetContext,
            1
        )
        assertEquals(24, temp)
    }

    @Test
    //tests the checkWarnings method inside the Luminosity sensor object
    fun checkWarnings() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAllWarning()
        val myTemp = Luminosity(1, "Luminosity", 90, 23, null)
        val temp = myTemp.checkWarnings(300)
        assertEquals(0, temp)
    }

    @Test
    //tests the getHistorical method inside the Luminosity sensor object
    fun getHistorical() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAll()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "data1", "warning1", "time1"
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 0, null)
        val temp =
            myTemp.getHistorical(InstrumentationRegistry.getInstrumentation().targetContext, 1).size
        assertEquals(1, temp)
    }

    @Test
    //tests the getHistoricalWithTime method inside the Luminosity sensor object
    fun getHistoricalWithTime() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAll()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "23", "23", "time1"
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 23, null)
        val temp = myTemp.getHistoricalWithTime(
            InstrumentationRegistry.getInstrumentation().targetContext,
            1
        ).size
        assertEquals(1, temp)
    }

    @Test
    //tests the getHistoricalWarning method inside the Luminosity sensor object
    fun getHistoricalWarning() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAllWarning()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "23", "24", "time1"
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 23, null)
        val temp = myTemp.getHistoricalWarning(
            InstrumentationRegistry.getInstrumentation().targetContext,
            1
        ).size
        assertEquals(1, temp)
    }

    @Test
    //tests the getLastTenMinutes method inside the Luminosity sensor object
    fun getLastTenMinutes() {
        val dbHandler = DBHandler(
            context =
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        dbHandler.deleteAllWarning()
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "23", "24", "time1"
        )
        dbHandler.addNewSensorData(
            "1", "Luminosity",
            "23", "24", null
        )
        val myTemp = Luminosity(1, "Luminosity", 90, 23, null)
        val temp = myTemp.getLastTenMinutes(
            InstrumentationRegistry.getInstrumentation().targetContext,
            1
        ).size
        assertEquals(1, temp)
    }
}