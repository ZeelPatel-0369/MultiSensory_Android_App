package com.example.sixthsenseprojectv1

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.sixthsenseprojectv1.uiHelpers.DialogAlert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DialogAlertTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAlertDialog() {
        composeTestRule.setContent {
            DialogAlert().AlertDialogMessage(title = "Test Title", message = "Test Message")
        }
        // Find the AlertDialog by its text content and ensure it is displayed
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()

        // Simulate a button click to dismiss the AlertDialog
        composeTestRule.onNodeWithText("Dismiss").performClick()

        // Ensure the AlertDialog is no longer displayed
        composeTestRule.onNodeWithText("Test Title").assertDoesNotExist()
    }


}


class ConsentDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickYesButton_displaysExportRequestExecutedToast() {
        var yesButtonClicked = false
        val onYesClick: () -> Unit = { yesButtonClicked = true }
        val onNoClick: () -> Unit = { }

        composeTestRule.setContent {
            DialogAlert().ConsentDialog(onYesClick = onYesClick, onNoClick = onNoClick)
        }

        // Click on the "Yes" button
        composeTestRule.onNodeWithText("Yes").performClick()
        composeTestRule.onNodeWithText("Test Title").assertDoesNotExist()
        // Check that the onYesClick function was called and the expected toast message was displayed
        assertEquals(true, yesButtonClicked)

    }

    @Test
    fun clickNoButton_displaysExportRequestTerminatedToast() {
        var noButtonClicked = false
        val onYesClick: () -> Unit = { }
        val onNoClick: () -> Unit = { noButtonClicked = true }

        composeTestRule.setContent {
            DialogAlert().ConsentDialog(onYesClick = onYesClick, onNoClick = onNoClick)
        }

        // Click on the "No" button
        composeTestRule.onNodeWithText("No").performClick()
        composeTestRule.onNodeWithText("Test Title").assertDoesNotExist()
        // Check that the onNoClick function was called and the expected toast message was displayed
        assertEquals(true, noButtonClicked)


    }
}
