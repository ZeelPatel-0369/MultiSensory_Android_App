package com.example.sixthsenseprojectv1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.ui.theme.SixthSenseProjectV1Theme
import com.example.sixthsenseprojectv1.ui.theme.customfont
import com.example.sixthsenseprojectv1.uiHelpers.*

class HistoricalUI : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SixthSenseProjectV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    HistoricalScreen()
                }
            }
        }
    }

    // This is the top-level function for the historical screen.
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun HistoricalScreen() {

        Scaffold(modifier = Modifier
            .background(Color.Transparent)
            .clip(RoundedCornerShape(12.dp)),
            topBar = { NavBar() }
        ) {
            SensorsUI().graphSensorArray(this@HistoricalUI)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun NavBar() {

        val viewModel: ViewModelRealTime = viewModel()
        val tempSwitchPanelsMe: Int by viewModel.tempSwitchPanelsMe.observeAsState(0)
        val tempSwitchPanelsTeammate: Int by viewModel.tempSwitchPanelsTeammate.observeAsState(0)
        val tempSwitchTabsMe: Int by viewModel.tempSwitchTabsMe.observeAsState(0)
        val tempSwitchTabsTeammate: Int by viewModel.tempSwitchTabsTeammate.observeAsState(0)
        val tempSwitchTeammate: String by viewModel.tempSwitchTeammate.observeAsState("Me")
        val tempSwitchView: Int by viewModel.tempSwitchView.observeAsState(0)
        val tempSplitHomeMe: Int by viewModel.tempSplitHomeMe.observeAsState(0)
        val tempSplitHomeTeammate: Int by viewModel.tempSplitHomeTeammate.observeAsState(0)


        // State: selected person
        var selectedPerson by remember {
            mutableStateOf(selectedMemberTwoPoint0)
        }

        // State: selected sensor
        var selectedTempSensor by remember {
            mutableStateOf(selectedSensorTwoPoint0)
        }

        //create the TopAppBar
        TopAppBar(modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(12.dp)
                clip = true
            }
            .fillMaxWidth()
            .fillMaxHeight(.16f)
            .padding(3.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(12.dp)
                clip = true
            },

            elevation = 13.dp, title = {
                Text(
                    "Historical",
                    color = Color.White,
                    fontFamily = customfont,
                    overflow = TextOverflow.Ellipsis
                )
            }, actions = {

                //create the row layout to arrange the action button and dropdownMenu
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.87f)
                        .fillMaxHeight()
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.End,

                    ) {

                    var personDropdownIsExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                        .fillMaxWidth(0.35f),

                        expanded = personDropdownIsExpanded, onExpandedChange = {
                            personDropdownIsExpanded = !personDropdownIsExpanded
                        }) {

                        // text field
                        OutlinedTextField(
                            enabled = false,
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxSize(),

                            shape = RoundedCornerShape(15.dp),
                            singleLine = true,
                            value = selectedPerson,
                            onValueChange = { },

                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = personDropdownIsExpanded,
                                )
                            },
                            textStyle = TextStyle(
                                fontFamily = customfont, color = Color.White, fontSize = 13.sp
                            ),
                        )

                        // menu
                        ExposedDropdownMenu(modifier = Modifier.padding(1.dp),
                            expanded = personDropdownIsExpanded,
                            onDismissRequest = { personDropdownIsExpanded = false }) {
                            teammateName.forEach { selectedOption ->
                                // menu item
                                DropdownMenuItem(contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                                    onClick = {
                                        selectedPerson = selectedOption
                                        selectedMember = selectedOption
                                        selectedMemberTwoPoint0 = selectedOption
                                        personDropdownIsExpanded = false
                                    }) {
                                    Text(
                                        text = selectedOption,
                                        fontFamily = customfont,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                    }
                    // PEOPLE DROPDOWN

                    //create the space for size of 3.Dp
                    Spacer(modifier = Modifier.size(3.dp))

                    // SENSOR DROPDOWN
                    var sensorDropdownIsExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                        .fillMaxWidth(0.35f),
                        expanded = sensorDropdownIsExpanded,
                        onExpandedChange = {
                            sensorDropdownIsExpanded = !sensorDropdownIsExpanded
                        }

                    ) {

                        // text field
                        OutlinedTextField(
                            enabled = false,
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxSize(),

                            shape = RoundedCornerShape(15.dp),
                            singleLine = true,
                            value = selectedTempSensor,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = sensorDropdownIsExpanded,
                                )
                            },
                            textStyle = TextStyle(
                                fontFamily = customfont, color = Color.White, fontSize = 13.sp
                            ),
                        )

                        // menu
                        ExposedDropdownMenu(modifier = Modifier.padding(0.dp),
                            expanded = sensorDropdownIsExpanded,
                            onDismissRequest = { sensorDropdownIsExpanded = false }) {
                            sensorNames.forEach { selectedOption ->
                                // menu item
                                DropdownMenuItem(contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                                    onClick = {
                                        selectedTempSensor = selectedOption
                                        selectedSensor = selectedOption
                                        selectedSensorTwoPoint0 = selectedOption
                                        sensorDropdownIsExpanded = false
                                    }) {
                                    Text(
                                        text = selectedOption,
                                        fontFamily = customfont,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    //create the space for size of 3.Dp
                    Spacer(modifier = Modifier.size(2.dp))

                    // Health Action button
                    IconButton(modifier = Modifier
                        .size(65.dp)
                        .padding(0.dp)
                        .background(Color.Transparent),
                        onClick = {
                            if (selectedMember == "Me") {
                                tspm = 0
                                tspt = 0
                                tstm = 0
                                tstt = 0
                                tst = "Me"
                                tsv = 0
                            } else {
                                tspm = 0
                                tspt = 1
                                tstm = 0
                                tstt = 0
                                tst = selectedMember
                                tsv = 0
                            }

                            val navigate = Intent(this@HistoricalUI, HomeActivity::class.java)
                            startActivity(navigate)
                        }) {
                        Icon(

                            imageVector = Icons.Default.Home, "Home", tint = Color.White
                        )

                    }

                    //create the space for size of 3.Dp
                    Spacer(modifier = Modifier.size(1.dp))

                    //Icons Button-4
                    IconButton(modifier = Modifier
                        .size(45.dp)
                        .padding(12.dp)
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                        .background(Color.Transparent),
                        onClick = {
                            val navigate = Intent(this@HistoricalUI, MapActivity::class.java)
                            startActivity(navigate)
                        }) {
                        Icon(

                            painter = painterResource(R.drawable.map), "Map", tint = Color.White
                        )
                    }
                    //create the space for size of 2.Dp
                    Spacer(modifier = Modifier.size(2.dp))
                    val showConsentDialog = remember { mutableStateOf(false) }
                    if (showConsentDialog.value) {
                        DialogAlert().ConsentDialog(
                            onYesClick = {
                                val dbHandler = DBHandler(this@HistoricalUI)
                                dbHandler.exportToCSV(this@HistoricalUI)
                                showConsentDialog.value = false
                            },
                            onNoClick = { showConsentDialog.value = false }
                        )
                    }
                    //Icons Button-4
                    IconButton(modifier = Modifier
                        .size(45.dp)
                        .padding(12.dp)
                        .align(Alignment.CenterVertically)
                        .background(Color.Transparent),


                        onClick = {
                            showConsentDialog.value = true


                        }) {
                        Icon(

                            painter = painterResource(R.drawable.export),
                            "Export",
                            tint = Color.White
                        )
                    }
                    //create the space for size of 2.Dp
                    Spacer(modifier = Modifier.size(2.dp))

                }
            })
    }
}