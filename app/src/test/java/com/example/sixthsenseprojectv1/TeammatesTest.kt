package com.example.sixthsenseprojectv1

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.teams.Teammates
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TeammateTests {

    @Test
    fun generateTeam() {
        val teammateName = arrayOf("Me", "Baker", "Carter", "Dean", "John", "Tim")
        val teammateID = arrayOf(1, 2, 3, 4, 5, 6)
        val size1 = squad.size - 1
        val size2 = squad[0].size - 1

        for (i in 0 until size1) {
            for (j in 0..size2) {
                val id = squad[i][j]
                val name = squad[i + 1][j]
                assertEquals(teammateID[j], id)
                assertEquals(teammateName[j], name)
            }
        }
    }

    @Test
    fun generateTeamData() {
        Teammates().generateTeamData(InstrumentationRegistry.getInstrumentation().targetContext)

        for (i in allSensorsArray.indices) {
            val m = allSensorsArray[i]
            val id = m.getID()
            val data = m.getData()
            val sensorName = m.getName()

            var k = 2
            if (i > 12) {
                k = 3
            }
            if (i > 12 + 13) {
                k = 4
            }
            if (i > 12 + 13 + 13) {
                k = 5
            }
            if (i > 12 + 13 + 13 + 13) {
                k = 6
            }

            if (i < 12 + 13 + 13 + 13 + 13) {
                assertEquals(k, id)
            }

            if (sensorName == "Temperature") {
                assertTrue(data in -40..80)
            } else if (sensorName == "Humidity") {
                assertTrue(data in 0..100)
            } else if (sensorName == "Pressure") {
                assertTrue(data in 50..110)
            } else if (sensorName == "Altitude") {
                assertTrue(data in 0..10)
            } else if (sensorName == "TVOC") {
                assertTrue(data in 1000..1187000)
            } else if (sensorName == "ECO2") {
                assertTrue(data in 400..8192)
            } else if (sensorName == "Luminosity") {
                assertTrue(data in 1..65535)
            } else if (sensorName == "Longitude") {
                assertTrue(data in -63..-62)
            } else if (sensorName == "Latitude") {
                assertTrue(data in 46..47)
            } else if (sensorName == "Time") {
                //cannot check
            } else if (sensorName == "Heart Rate") {
                assertTrue(data in 0..200)
            } else if (sensorName == "Oxygen Saturation") {
                assertTrue(data in 0..100)
            } else if (sensorName == "Body Temperature") {
                assertTrue(data in 20..50)
            }
        }
    }

    @Test
    fun getIDFromName() {
        val teammate = Teammates()

        assertEquals(teammate.getIDFromName("Me"), 1)
        assertEquals(teammate.getIDFromName("Baker"), 2)
        assertEquals(teammate.getIDFromName("Carter"), 3)
        assertEquals(teammate.getIDFromName("Dean"), 4)
        assertEquals(teammate.getIDFromName("John"), 5)
        assertEquals(teammate.getIDFromName("Tim"), 6)
    }
}