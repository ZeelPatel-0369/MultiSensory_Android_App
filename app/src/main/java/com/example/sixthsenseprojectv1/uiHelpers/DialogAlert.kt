package com.example.sixthsenseprojectv1.uiHelpers

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sixthsenseprojectv1.R


class DialogAlert {
    /**

    Composable function to display an alert dialog message with a title and a message.

    @param title the title of the alert dialog message.

    @param message the message of the alert dialog message.
     */
    @Composable
    fun AlertDialogMessage(title: String, message: String) {
        // Set the initial status of the dialog to be open
        val openDialogStatus = remember { mutableStateOf(true) }

        // Display the alert dialog if the status is true
        if (openDialogStatus.value) {
            AlertDialog(
                modifier = Modifier
                    .height(180.dp)
                    .testTag(stringResource(id = R.string.Alert_Dialog_1))
                    .fillMaxWidth()
                    .shadow(elevation = 20.dp)
                    .background(
                        color = MaterialTheme.colors.onPrimary,
                        shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                    ),
                onDismissRequest = { openDialogStatus.value = false },
                title = {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                text = {
                    Text(
                        text = message,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        color = Color.White,
                        fontFamily = FontFamily.Default,


                        )
                },
                confirmButton = {

                    // Add a dismiss button to close the alert dialog
                    Button(
                        onClick = { openDialogStatus.value = false },
                        elevation = ButtonDefaults.elevation(15.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .shadow(
                                elevation = 4.dp, // adjust the elevation value to achieve desired shadow effect
                                shape = RoundedCornerShape(40.dp),
                                spotColor = Color.White
                            ),
                        shape = RoundedCornerShape(40.dp),

                        ) {
                        Text(text = "Dismiss", color = Color.White, fontWeight = FontWeight.Bold)
                    }


                },
            )
        }

    }

    /**

    A composable function that displays an AlertDialog asking for user's consent to export their data.

    @param onYesClick A function that will be called when the user clicks the "Yes" button.

    @param onNoClick A function that will be called when the user clicks the "No" button or dismisses the dialog.
     */
    @Composable
    fun ConsentDialog(onYesClick: () -> Unit, onNoClick: () -> Unit) {
        // Get the current context
        val context = LocalContext.current

        // Display an AlertDialog with a custom background color and shape
        AlertDialog(
            modifier = Modifier
                .height(210.dp)
                .fillMaxWidth()
                .shadow(elevation = 20.dp)
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                ),
            // Call the onNoClick function when the user dismisses the dialog
            onDismissRequest = { onNoClick() },
            // Set the title and text of the dialog
            title = { Text("Alert!", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Would you Like to Export your Data",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            // Add two buttons to the dialog
            buttons = {
                Row {
                    // "Yes" button
                    TextButton(
                        elevation = ButtonDefaults.elevation(15.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .weight(1F)
                            .shadow(
                                elevation = 4.dp, // adjust the elevation value to achieve desired shadow effect
                                shape = RoundedCornerShape(40.dp),
                                spotColor = Color.White // set the desired shadow color
                            ),
                        shape = RoundedCornerShape(40.dp),
                        // Display a toast message and call the onYesClick function when the user clicks "Yes"
                        onClick = {
                            Toast.makeText(
                                context,
                                "Your Export request has been Executed!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onYesClick()
                        }
                    ) {
                        Text("Yes", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    // "No" button
                    TextButton(
                        elevation = ButtonDefaults.elevation(15.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .weight(1F)
                            .shadow(
                                elevation = 4.dp, // adjust the elevation value to achieve desired shadow effect
                                shape = RoundedCornerShape(40.dp),
                                spotColor = Color.White // set the desired shadow color
                            ),
                        shape = RoundedCornerShape(40.dp),
                        // Display a toast message and call the onNoClick function when the user clicks "No"
                        onClick = {
                            Toast.makeText(
                                context,
                                "Your Export request has been Terminated!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNoClick()
                        }
                    ) {
                        Text("No", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }


    /**
     * This is a Jetpack Compose function named `AutoExportToaster` that displays a toast message to indicate
     * that a file has been successfully exported.
     *
     * @param filePath String as The path of the exported file.
     */
    @Composable
    fun AutoExportToaster(Path: Uri) {
        // Get the context from LocalContext
        val context = LocalContext.current

        // Display a toast message to indicate that the file has been exported successfully
        Toast.makeText(context, "File Exported Successfully in {$Path}", Toast.LENGTH_SHORT)
            .show()
    }

}