package com.example.sixthsenseprojectv1

//import the classes needed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sixthsenseprojectv1.sensors.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

//tests to check the warning boundaries
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class WarningCheckTests {

    //checks to see that the warning value is correctly calculated
    @Test
    fun warningTemperature() {

        val myTemp = Temperature(1, "Temperature", 28, 0, null)

        myTemp.checkWarnings(28)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(50)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(51)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(-28)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(-40)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(-41)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningHeartRate() {

        val myTemp = HeartRate(1, "Heart Rate", 100, 0, null)

        myTemp.checkWarnings(165)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(166)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(40)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(39)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(41)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(164)
        result = myTemp.getWarning()
        assertEquals(0, result)
    }

    @Test
    fun warningOxygenSaturation() {

        val myTemp = OxygenSaturation(1, "Oxygen Saturation", 90, 0, null)

        myTemp.checkWarnings(90)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(91)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(89)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(100)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(99)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(101)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningHumidity() {

        val myTemp = Humidity(1, "Humidity", 90, 0, null)

        myTemp.checkWarnings(0)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(100)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(50)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(101)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(-1)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningPressure() {

        val myTemp = Pressure(1, "Pressure", 90, 0, null)

        myTemp.checkWarnings(50)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(51)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(102)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(101)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(103)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(49)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningTVOC() {

        val myTemp = TVOC(1, "TVOC", 90, 0, null)

        myTemp.checkWarnings(1000)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(1187000)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(1001)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(999)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningEC02() {

        val myTemp = ECO2(1, "EC02", 90, 0, null)

        myTemp.checkWarnings(2000)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(1999)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(400)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(401)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(399)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(2001)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningBodyTemperature() {

        val myTemp = BodyTemperature(1, "Body Temperature", 28, 0, null)

        myTemp.checkWarnings(38)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(36)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(37)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(35)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(48)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }

    @Test
    fun warningLuminosity() {

        val myTemp = Luminosity(1, "Luminosity", 28, 0, null)

        myTemp.checkWarnings(12000)
        var result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(11999)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(12001)
        result = myTemp.getWarning()
        assertEquals(1, result)

        myTemp.checkWarnings(1)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(2)
        result = myTemp.getWarning()
        assertEquals(0, result)

        myTemp.checkWarnings(0)
        result = myTemp.getWarning()
        assertEquals(1, result)
    }
}