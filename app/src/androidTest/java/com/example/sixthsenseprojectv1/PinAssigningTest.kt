package com.example.sixthsenseprojectv1

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.teams.Teammates
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PinAssigningTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: android.content.Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun pinAssigning() {
        for (i in teammateName.indices) {
            var pin = Teammates().returnPins(teammateName[i], context)
            if (teammateName[i] == "Me") {
                Assert.assertTrue(pin == R.drawable.location_on_red)
            } else if (teammateName[i] == "Baker") {
                Assert.assertTrue(pin == R.drawable.location_on_green)
            } else if (teammateName[i] == "Carter") {
                Assert.assertTrue(pin == R.drawable.location_on_purple)
            } else if (teammateName[i] == "Dean") {
                Assert.assertTrue(pin == R.drawable.location_on_blue)
            } else if (teammateName[i] == "John") {
                Assert.assertTrue(pin == R.drawable.location_on_white)
            } else if (teammateName[i] == "Tim") {
                Assert.assertTrue(pin == R.drawable.location_on_orange)
            }
        }
    }

}