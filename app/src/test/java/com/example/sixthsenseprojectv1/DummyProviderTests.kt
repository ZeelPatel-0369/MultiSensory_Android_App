package com.example.sixthsenseprojectv1

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.sensors.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)

//These tests are meant to see if the dummy providers provide data within the expected range
// of each sensor
class DummyProviderTests {

    @Test
    //Test for the longitude provider
    fun dummyProviderLongitude() {

        val myTemp = Longitude(1, "Longitude", -62, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in -63..-62)
    }

    @Test
    //Test for the latitude provider
    fun dummyProviderLatitude() {

        val myTemp = Latitude(1, "Latitude", 46, 0, null)

        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 46..47)
    }

    @Test
    //Test for the time provider
    fun dummyProviderTime() {

        val current = LocalDateTime.now()
        val myTemp = Time(1, "Time", current, 0, null)

        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result == current)
    }

    @Test
    //Test for the temperature provider
    fun dummyProviderTemperature() {

        val myTemp = Temperature(1, "Temperature", 28, 0, null)

        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in -40..80)
    }

    @Test
    //Test for the humidity provider
    fun dummyProviderHumidity() {

        val myTemp = Humidity(1, "Humidity", 90, 0, null)

        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 0..100)
    }

    @Test
    //Test for the pressure provider
    fun dummyProviderPressure() {

        val myTemp = Pressure(1, "Pressure", 90, 0, null)

        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 50..110)
    }

    @Test
    //Test for the altitude provider
    fun dummyProviderAltitude() {

        val myTemp = Altitude(1, "Altitude", 9, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 0..10)
    }

    @Test
    //Test for the TVOC provider
    fun dummyProviderTVOC() {

        val myTemp = TVOC(1, "TVOC", 1001, 999, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 1000..1187000)
    }

    @Test
    //Test for the ECO2 provider
    fun dummyProviderEC02() {

        val myTemp = ECO2(1, "eC02", 900, 399, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 400..8192)

    }

    @Test
    //Test for the Body Temperature provider
    fun dummyProviderBodyTemperature() {

        val myTemp = BodyTemperature(1, "Body Temperature", 36, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 20..50)
    }

    @Test
    //Test for the Heart Rate provider
    fun dummyProviderHeartRate() {

        val myTemp = HeartRate(1, "Heart Rate", 100, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 0..200)
    }

    @Test
    //Test for the Oxygen saturation provider
    fun dummyProviderOxygenSaturation() {

        val myTemp = OxygenSaturation(1, "Oxygen Saturation", 90, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 0..100)
    }

    @Test
    //Test for the Light intensity provider
    fun dummyProviderLightIntensity() {

        val myTemp = Luminosity(1, "Luminosity", 90, 0, null)
        myTemp.dummyProvider(1, InstrumentationRegistry.getInstrumentation().targetContext)
        val result = myTemp.getData()
        assertTrue(result in 1..65535)
    }

}