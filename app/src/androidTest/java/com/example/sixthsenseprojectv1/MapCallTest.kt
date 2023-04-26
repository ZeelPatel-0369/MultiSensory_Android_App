package com.example.sixthsenseprojectv1

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapCallTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun mapCall() {
        composeRule.setContent {
            MapActivity().getMap()
        }
        composeRule.onNodeWithTag(context.getString(R.string.MBScreen))
            .assertIsDisplayed()
    }

}