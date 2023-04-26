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


class RTEnvTest {

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
            SensorsUI().RealTimeEnvTwoPointO(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTEnv))
            .assertIsDisplayed()
    }

}

class RTHealthTest {

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
            SensorsUI().RealTimeHealthTwoPointO(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTHealth))
            .assertIsDisplayed()
    }

}

class RTEnvMe {

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
            SensorsUI().RealTimeEnvTwoPointOMe(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTEnvMe))
            .assertIsDisplayed()
    }

}

class RTHealthMe {

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
            SensorsUI().RealTimeHealthTwoPointOMe(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTHealthMe))
            .assertIsDisplayed()
    }

}


class RTVitalSub {

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
            SensorsUI().RealTimeVitalSubGraph(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTVitalSub))
            .assertIsDisplayed()
    }

}

class RTVitalSubMe {

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
            SensorsUI().RealTimeVitalSubGraphMe(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTVitalSubMe))
            .assertIsDisplayed()
    }

}

class RTWarning {

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
            SensorsUI().RealTimeSingleWarning(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTWarning))
            .assertIsDisplayed()
    }

}

class RTWarningMe {

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
            SensorsUI().RealTimeSingleWarningMe(context = context)
        }

        composeRule.onNodeWithTag(context.getString(R.string.RTWarningMe))
            .assertIsDisplayed()
    }

}