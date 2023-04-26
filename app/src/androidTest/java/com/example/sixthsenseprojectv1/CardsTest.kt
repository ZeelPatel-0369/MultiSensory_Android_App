package com.example.sixthsenseprojectv1

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sixthsenseprojectv1.uiHelpers.SensorsUI
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CardMakerTwoPointOTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testCardMakerTwoPointO() {
        composeRule.setContent {
            SensorsUI().CardMakerTwoPointO(
                sensorName = "Temperature",
                sensorData = "25 C",
                warningSign = 0,
                context = context
            )
        }

        composeRule.onNodeWithTag(context.getString(R.string.card1))
            .assertIsDisplayed()
    }

}

class CardMakerTwoPointOMeTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testCardMakerTwoPointO() {
        composeRule.setContent {
            SensorsUI().CardMakerTwoPointOMe(
                sensorName = "Temperature",
                sensorData = "25 C",
                warningSign = 0,
                context = context
            )
        }

        composeRule.onNodeWithTag(context.getString(R.string.card2))
            .assertIsDisplayed()
    }

}

class CardMakerTwoPointOMeRealTimeTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testCardMakerTwoPointO() {
        composeRule.setContent {
            SensorsUI().CardMakerRealTimeScreen2TwoPointOMe(
                sensorName = "Temperature",
                sensorData = "25 C",
                warningSign = 0,
                context = context,
                //graphType = "env"
            )
        }

        composeRule.onNodeWithTag(context.getString(R.string.cardsub1))
            .assertIsDisplayed()
    }
}

class CardMakerTwoPointORealTimeTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testCardMakerTwoPointO() {
        composeRule.setContent {
            SensorsUI().CardMakerRealTimeScreen2TwoPointOMe(
                sensorName = "Temperature",
                sensorData = "25 C",
                warningSign = 0,
                context = context,
                //  graphType = "env"
            )
        }

        composeRule.onNodeWithTag(context.getString(R.string.cardsub2))
            .assertIsDisplayed()
    }

}