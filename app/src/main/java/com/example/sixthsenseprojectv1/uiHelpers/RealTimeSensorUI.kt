package com.example.sixthsenseprojectv1.uiHelpers

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sixthsenseprojectv1.*
import com.example.sixthsenseprojectv1.R
import com.example.sixthsenseprojectv1.database.*
import com.example.sixthsenseprojectv1.sensors.*
import com.example.sixthsenseprojectv1.teams.Teammates
import com.example.sixthsenseprojectv1.ui.theme.customfont
import com.example.sixthsenseprojectv1.utils.round
import com.example.sixthsenseprojectv1.utils.truncateLargeNumber
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import java.time.LocalDateTime

class SensorsUI {


    // This function displays real-time environmental data in a column layout using Compose UI.
    @Composable
    fun RealTimeEnvTwoPointO(context: Context) {
        // Create a column layout with various modifications
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .testTag(stringResource(id = R.string.RTEnv))
                .background(Color.Transparent)
        ) {
            // Retrieve data to be displayed in cards in the column
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(3))
            {
                items(environmentSensorsArray.size) { index ->

                    viewModel.onDataChange(
                        environmentSensorsArray[index].getName(),
                        environmentSensorsArray[index].getFromDatabase(context, id),
                        environmentSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerTwoPointO(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context
                    )
                }
            }
        }

    }

    // this method creates the Health tab screen for the teammates
    @Composable
    fun RealTimeHealthTwoPointO(context: Context) {

        Column(
            //modified column
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTHealth))
                .padding(7.dp)
//
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)


        ) {
            //get the respective sensor data to be displayed
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(3))
            {
                items(healthSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        healthSensorsArray[index].getName(),
                        healthSensorsArray[index].getFromDatabase(context, id),
                        healthSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerTwoPointO(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context
                    )
                }
            }
        }
    }

    // this method creates the environmental tab screen for the User
    @Composable
    fun RealTimeEnvTwoPointOMe(context: Context) {
        Column(
            //modified column
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTEnvMe))
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            //get the data to be displayed in column
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(3))
            {
                items(environmentSensorsArray.size) { index ->

                    viewModel.onDataChange(
                        environmentSensorsArray[index].getName(),
                        environmentSensorsArray[index].getFromDatabase(context, id),
                        environmentSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerTwoPointOMe(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context
                    )
                }
            }
        }
    }

    // this method creates the Health tab screen for the User
    // Composable function named RealTimeHealthTwoPointOMe which takes in a context parameter
    @Composable
    fun RealTimeHealthTwoPointOMe(context: Context) {
        // A Column composable with the following modifiers
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTHealthMe))
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(3))
            {
                items(healthSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        healthSensorsArray[index].getName(),
                        healthSensorsArray[index].getFromDatabase(context, id),
                        healthSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerTwoPointOMe(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context
                    )
                }
            }
        }
    }
    //**********************************************************************************************************************************************************************************


    @Composable
    fun RealTimeEnvSubGraphTeammate(context: Context) {
        // Create a column layout with various modifications
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .testTag(stringResource(id = R.string.RTEnv))
                .background(Color.Transparent)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)
            LazyColumn()
            {
                items(environmentSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        environmentSensorsArray[index].getName(),
                        environmentSensorsArray[index].getFromDatabase(context, id),
                        environmentSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointO(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "env"
                    )
                }
            }
        }
    }

    // this method creates the Health tab screen for the teammates
    @Composable
    fun RealTimeHealthSubGraphTeammate(context: Context) {

        Column(
            //modified column
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTHealth))
                .padding(7.dp)
//
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)


        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)
            LazyColumn()
            {
                items(healthSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        healthSensorsArray[index].getName(),
                        healthSensorsArray[index].getFromDatabase(context, id),
                        healthSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointO(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "health"
                    )
                }
            }
        }
    }

    // this method creates the environmental tab screen for the User
    @Composable
    fun RealTimeEnvSubGraphMe(context: Context) {
        Column(
            //modified column
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTEnvMe))
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyColumn()
            {
                items(environmentSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        environmentSensorsArray[index].getName(),
                        environmentSensorsArray[index].getFromDatabase(context, id),
                        environmentSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointOMe(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "env"
                    )
                }
            }
        }
    }

    // this method creates the Health tab screen for the User
    // Composable function named RealTimeHealthTwoPointOMe which takes in a context parameter
    @Composable
    fun RealTimeHealthSubGraphMe(context: Context) {
        // A Column composable with the following modifiers
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTHealthMe))
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyColumn()
            {
                items(healthSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        healthSensorsArray[index].getName(),
                        healthSensorsArray[index].getFromDatabase(context, id),
                        healthSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointOMe(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "health"
                    )
                }
            }
        }
    }


    //**********************************************************************************************************************************************************************************
    //Method create the Card Layout to be displayed on respective Sub-Screen for teamMates Health/Environmental Screen
    //This method creates a Card layout to be displayed on the respective sub-screen for teammates' Health/Environmental screen
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CardMakerTwoPointO(
        sensorName: String, sensorData: String, warningSign: Int, context: Context
    ) {
        Card(modifier = Modifier
            .width(120.dp)
            .height(55.dp)
            .testTag(stringResource(id = R.string.card1))
            .padding(3.dp),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 2.dp,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                selectedSensor = sensorName
                selectedSensorTwoPoint0 = selectedSensor
                selectedMemberTwoPoint0 = selectedMember
                //Navigate to the HistoricalUI activity on click of card
                val navigate = Intent(context, HistoricalUI::class.java)
                context.startActivity(navigate)
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                verticalArrangement = Arrangement.Center

            ) {
                //Row to display the sensor name
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    //Shorten the sensor name and display it
                    val tempSensorName = shortSensorNames[sensorNames.indexOf(sensorName)]
                    Text(
                        text = tempSensorName,
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                //Get the sensor data and split it into data and unit
                var wholeDate = sensorData
                var delDate = " "
                var justData = " "
                var dataUnit = " "

                try {
                    wholeDate = sensorData
                    delDate = " "
                    val splitDate = wholeDate.split(delDate)
                    justData = splitDate[0]
                    dataUnit = splitDate[1]
                } catch (e: Exception) {

                }
                //Row to display the sensor data
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1.5F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top

                ) {
                    //Display the sensor data in red color if the warning sign is 1, else in white color
                    if (warningSign == 0) {
                        Text(
                            text = "$justData",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontFamily = customfont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis

                        )
                    } else if (warningSign == 1) {
                        Text(
                            text = "$justData",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontFamily = customfont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.Red,
                            overflow = TextOverflow.Ellipsis

                        )

                    }
                }
                //Row to display the sensor data unit
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top

                ) {
                    Text(
                        text = "$dataUnit",
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis

                    )
                }


            }

        }
    }

    //Method create the Card Layout to be displayed on respective Sub-Screen for User Health/Environmental Screen
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CardMakerTwoPointOMe(
        sensorName: String, sensorData: String, warningSign: Int, context: Context
    ) {

        Card(modifier = Modifier
            .width(120.dp)
            .testTag(stringResource(id = R.string.card2))
            .height(55.dp)
            .padding(3.dp),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 5.dp,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                selectedSensor = sensorName
                selectedMemberTwoPoint0 = "Me"
                selectedSensorTwoPoint0 = selectedSensor
                val navigate = Intent(context, HistoricalUI::class.java)
                context.startActivity(navigate)
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                verticalArrangement = Arrangement.Center

            ) {


                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    val tempSensorName = shortSensorNames[sensorNames.indexOf(sensorName)]
                    Text(
                        text = tempSensorName,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                var wholeDate = sensorData
                var delDate = " "
                var justData = " "
                var dataUnit = " "

                try {
                    wholeDate = sensorData
                    delDate = " "
                    val splitDate = wholeDate.split(delDate)
                    justData = splitDate[0]
                    dataUnit = splitDate[1]
                } catch (e: Exception) {

                }
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1.5F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {

                    if (warningSign == 0) {
                        Text(
                            text = "$justData",
                            modifier = Modifier,
                            fontFamily = customfont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis

                        )
                    } else if (warningSign == 1) {
                        Text(
                            text = "$justData",
                            modifier = Modifier,
                            fontFamily = customfont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.Red,
                            overflow = TextOverflow.Ellipsis

                        )

                    }
                }
                Spacer(modifier = Modifier.size(1.dp))
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1F)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "$dataUnit",
                        modifier = Modifier,
                        fontFamily = customfont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis

                    )
                }
            }

        }
    }

    /**

    An experimental composable function that displays a card UI for real-time sensor data.
    @param sensorName The name of the sensor.
    @param sensorData The current sensor data.
    @param warningSign The warning sign for the sensor data.
    @param context The context of the current state of the application.
     */
    //Method create the Card Layout to be displayed on respective Sub-Screen for teamMates on Home Screen Under Vitals
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CardMakerRealTimeScreen2TwoPointO(
        sensorName: String,
        sensorData: String,
        warningSign: Int,
        context: Context,
        graphType: String
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(2.dp)
                .border(BorderStroke(0.8.dp, Color.White), shape = RoundedCornerShape(10.dp)),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 1.dp,
            shape = RoundedCornerShape(10.dp),
            onClick = {
                selectedSensor = sensorName
                selectedSensorTwoPoint0 = selectedSensor
                selectedMemberTwoPoint0 = selectedMember
                val navigate = Intent(context, HistoricalUI::class.java)
                context.startActivity(navigate)
            },

            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),

                    )
                {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1F)
                            .background(MaterialTheme.colors.background),
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(3.dp)
                        ) {
                            val tempSensorName = shortSensorNames[sensorNames.indexOf(sensorName)]
                            Text(
                                text = tempSensorName,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontFamily = customfont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (warningSign == 0) {
                                Text(
                                    text = sensorData,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    fontFamily = customfont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.White,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else if (warningSign == 1) {
                                Text(
                                    text = sensorData,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    fontFamily = customfont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(2.5F),
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        // call 10 min graph
                        tenMinGraphSensorArray(sensorName, selectedMember, context, graphType)
                    }
                }

            }
        )

    }

    /**

    An experimental composable function that displays a card UI for real-time sensor data.
    @param sensorName The name of the sensor.
    @param sensorData The current sensor data.
    @param warningSign The warning sign for the sensor data.
    @param context The context of the current state of the application.
     */
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CardMakerRealTimeScreen2TwoPointOMe(
        sensorName: String,
        sensorData: String,
        warningSign: Int,
        context: Context,
        graphType: String
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(2.dp)
                .border(BorderStroke(0.8.dp, Color.White), shape = RoundedCornerShape(10.dp)),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 1.dp,
            onClick = {
                selectedSensor = sensorName
                selectedMemberTwoPoint0 = "Me"
                selectedSensorTwoPoint0 = selectedSensor
                val navigate = Intent(context, HistoricalUI::class.java)
                context.startActivity(navigate)
            },

            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),

                    )
                {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1F)
                            .background(MaterialTheme.colors.background),
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(3.dp)
                        ) {
                            val tempSensorName = shortSensorNames[sensorNames.indexOf(sensorName)]
                            Text(
                                text = tempSensorName,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontFamily = customfont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (warningSign == 0) {
                                Text(
                                    text = sensorData,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    fontFamily = customfont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.White,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else if (warningSign == 1) {
                                Text(
                                    text = sensorData,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    fontFamily = customfont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(2.5F),
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        tenMinGraphSensorArray(sensorName, "Me", context, graphType)
                    }
                }

            }
        )

    }

    //***********************************************************************************************************************************************************************************
    // This method creates the Home tab screen for the teammates using LazyColumn to display all the cards under vitals.
    @Composable
    fun RealTimeVitalSubGraph(context: Context) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .testTag(stringResource(id = R.string.RTVitalSub))
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyColumn()
            {
                items(vitalSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        vitalSensorsArray[index].getName(),
                        vitalSensorsArray[index].getFromDatabase(context, id),
                        vitalSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointO(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "vitals"
                    )
                }
            }
        }
    }

    // This method creates the Home tab screen for the teammates using LazyColumn to display all the cards under vitals.
    @Composable
    fun RealTimeVitalSubGraphMe(context: Context) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTVitalSubMe))
                .padding(3.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyColumn()
            {
                items(vitalSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        vitalSensorsArray[index].getName(),
                        vitalSensorsArray[index].getFromDatabase(context, id),
                        vitalSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    CardMakerRealTimeScreen2TwoPointOMe(
                        sensorName = tempSensorName,
                        sensorData = tempSensorData,
                        warningSign = tempWarningSign,
                        context = context,
                        graphType = "vitals"
                    )
                }
            }
        }
    }

    //this method return the Warning sign in 0/1 to display in UI with color
    fun GetWarningLightInt(context: Context, member: String): Int {

        val id = Teammates().getIDFromName(member)
        var warningCheck = 0

        for (k in healthSensorsArray) {
            val tempWarning = k.getWarningFromDatabase(context, id)
            if (tempWarning == 1) {
                warningCheck = 1
                break
            }
        }
        for (k in environmentSensorsArray) {
            val tempWarning = k.getWarningFromDatabase(context, id)
            if (tempWarning == 1) {
                warningCheck = 1
                break
            }
        }
        return warningCheck
    }

    // This composable function creates a screen to display warnings for non-vital sensors using LazyHorizontalGrid to display cards in a horizontal scroll view.
    @Composable
    fun RealTimeSingleWarning(context: Context) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(id = R.string.RTWarning))
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName(selectedMember)
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(1))
            {
                items(allSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        allSensorsArray[index].getName(),
                        allSensorsArray[index].getFromDatabase(context, id),
                        allSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    if (tempWarningSign == 1) {
                        CardMakerTwoPointO(
                            sensorName = tempSensorName,
                            sensorData = tempSensorData,
                            warningSign = tempWarningSign,
                            context = context
                        )
                    }
                }
            }
        }
    }

    // this method creates the Home tab screen for the user using lazyRow to display all the cards under Warnings
    // This function is annotated with @Composable, which means it is a composable function
    // used to build a UI element in Jetpack Compose.
    @Composable
    fun RealTimeSingleWarningMe(context: Context) {
        // This composable function builds a Column layout that takes up the full available size,
        // with a padding of 7dp, a rounded corner shape of 12dp, and a background color
        // defined by the MaterialTheme colors.

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .testTag(stringResource(id = R.string.RTWarningMe))
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            val viewModel: ViewModelRealTime = viewModel()
            val id = Teammates().getIDFromName("Me")
            val tempSensorName: String by viewModel.tempSensorName.observeAsState("")
            val tempSensorData: String by viewModel.tempSensorData.observeAsState("")
            val tempWarningSign: Int by viewModel.tempWarningSign.observeAsState(0)

            LazyHorizontalGrid(GridCells.Fixed(1))
            {
                val tempSensor: ArrayList<Sensors> = arrayListOf<Sensors>()

                items(allSensorsArray.size) { index ->
                    viewModel.onDataChange(
                        allSensorsArray[index].getName(),
                        allSensorsArray[index].getFromDatabase(context, id),
                        allSensorsArray[index].getWarningFromDatabase(context, id)
                    )
                    if (tempWarningSign == 1) {
                        CardMakerTwoPointOMe(
                            sensorName = tempSensorName,
                            sensorData = tempSensorData,
                            warningSign = tempWarningSign,
                            context = context
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun graphSensorArray(context: Context) {
        val viewModel: ViewModelRealTime = viewModel()
        val realTimeDataPoint: MutableList<TimedDataPoint> by viewModel.tempValues.observeAsState(
            mutableListOf()
        )

        var tempSensor: Sensors = allSensorsArray[0]
        for (k in allSensorsArray) {
            if (k.getName() == selectedSensor) {
                tempSensor = k
                break
            }
        }

        val id = Teammates().getIDFromName(selectedMember)
//        val tempDataIntArray: MutableList<Float> = mutableListOf()
        val values: MutableList<TimedDataPoint> = mutableListOf()

//        if(tempSensor.getName() == "Time"){
//            //cannot check
//            tempDataIntArray.clear()
//            tempDataIntArray.add(10.0f)
//            tempDataIntArray.add(8.0f)
//            tempDataIntArray.add(6.0f)
//            tempDataIntArray.add(4.0f)
//            tempDataIntArray.add(2.0f)
//            tempDataIntArray.add(1.0f)
//            tempDataIntArray.add(3.0f)
//            tempDataIntArray.add(5.0f)
//            tempDataIntArray.add(7.0f)
//            tempDataIntArray.add(9.0f)
//        }
//        else{
//            tempDataIntArray.clear()
//            for (k in tempSensor.getHistoricalWithTime(context, id)){
//                tempDataIntArray.add(k.toFloat())
//            }
//        }

//        if(tempSensor.getName() != "Time") {
        for ((time, data) in tempSensor.getHistoricalWithTime(context, id)) {
            values.add(TimedDataPoint(LocalDateTime.parse(time), data.toFloat()))
            viewModel.onGraphChange(values)
        }
//        }

        HistoricalGraph(realTimeDataPoint)
    }

    @Composable
    fun tenMinGraphSensorArray(
        sensorName: String,
        memberName: String,
        context: Context,
        graphType: String
    ) {

        val viewModel: ViewModelRealTime = viewModel()
        val realTimeDataPoint: MutableList<LineData> by viewModel.tempValuesTen.observeAsState(
            mutableListOf()
        )

        var tempSensor: Sensors = vitalSensorsArray[0]

        if (graphType == "vitals") {
            for (k in vitalSensorsArray) {
                if (k.getName() == sensorName) {
                    tempSensor = k
                    break
                }
            }
        } else if (graphType == "health") {
            for (k in healthSensorsArray) {
                if (k.getName() == sensorName) {
                    tempSensor = k
                    break
                }
            }
        } else if (graphType == "env") {
            for (k in environmentSensorsArray) {
                if (k.getName() == sensorName) {
                    tempSensor = k
                    break
                }
            }
        }

        val id = Teammates().getIDFromName(memberName)
        val sentToViewModel: MutableList<Float> = mutableListOf()
        val values: MutableList<LineData> = mutableListOf()

        if (tempSensor.getName() == "Time") {
            //cannot check
            sentToViewModel.clear()
            sentToViewModel.add(10.0f)
            sentToViewModel.add(8.0f)
            sentToViewModel.add(6.0f)
            sentToViewModel.add(4.0f)
            sentToViewModel.add(2.0f)
            sentToViewModel.add(1.0f)
            sentToViewModel.add(3.0f)
            sentToViewModel.add(5.0f)
            sentToViewModel.add(7.0f)
            sentToViewModel.add(9.0f)
        } else {
            sentToViewModel.clear()
            for (k in tempSensor.getLastTenMinutes(context, id)) {
                sentToViewModel.add(k.toFloat())
            }
        }

        for (i in sentToViewModel.indices) {
            val size = sentToViewModel.size - 1
            values.add(LineData(i.toFloat(), sentToViewModel[size - i]))
            viewModel.onGraphChangeTen(values)
        }

        TenMinuteGraph(realTimeDataPoint)
    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun HistoricalGraph(historicalData: MutableList<TimedDataPoint>) {
        val dataPoints: MutableList<DataPoint> = mutableListOf()
        for (i in 0 until historicalData.size) {
            dataPoints.add(DataPoint(i.toFloat(), historicalData[i].data))
        }
        val screenWidth = LocalConfiguration.current.screenWidthDp
        if (historicalData.isEmpty()) Text(
            text = "No Data",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                fontFamily = customfont,
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        )
        else {
            val textMeasurer = rememberTextMeasurer()
            var selectedValue: Float by remember {
                mutableStateOf(0f)
            }
            LineGraph(plot = LinePlot(
                listOf(
                    LinePlot.Line(
                        dataPoints,
                        LinePlot.Connection(color = Color(0x77FFFFFF)),
                        LinePlot.Intersection { center, point ->

                            val warning = SensorsUI().checkWarningsGraph(point.y.toInt())
                            val color = if (warning == 1) Color(0xFFEB0000)
                            else Color(0xFF5FEB00)
                            drawCircle(
                                color,
                                6.dp.toPx(),
                                center,
                            )
                        },
                        LinePlot.Highlight(color = Color(0xFFFFFFFF), alpha = 0.5f),
                        LinePlot.AreaUnderLine(Color(0xFFFFFFFF), alpha = 0.2f)
                    )
                ),
                grid = LinePlot.Grid(Color(0x44FFFFFF), steps = 5),
                yAxis = LinePlot.YAxis(content = { min, offset, _ ->
                    for (it in 0 until 5) {
                        val value = it * offset + min
                        Text(
                            text = truncateLargeNumber(value.toDouble(), 1),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontFamily = customfont,
                                color = Color.White,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }),
                xAxis = LinePlot.XAxis(content = { _, _, _ ->
                    val numLabels = 10
                    for (it in 0 until numLabels) {
                        Text(
                            text = historicalData[it * (historicalData.size / numLabels)].time.toString()
                            // Using .format caused the app to crash upon opening the historical screen
//                            text = historicalData[it].time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL))
                            ,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }),
                selection = LinePlot.Selection(
                    detectionTime = 200L,
                    highlight = LinePlot.Connection(
                        draw = { offsetBottom, offsetTop ->

                            val measuredText = textMeasurer.measure(
                                AnnotatedString(
                                    selectedValue.toDouble().round(0).toLong().toString()
                                ),
                                style = TextStyle(
                                    fontFamily = customfont,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            var topLeft = offsetTop
                            if (offsetTop.x + measuredText.size.width > screenWidth)
                                topLeft = Offset(topLeft.x - measuredText.size.width, topLeft.y)

                            drawLine(
                                Color.White,
                                offsetBottom,
                                offsetTop,
                                strokeWidth = 7f,
                                alpha = 0.7f
                            )
                            drawRect(Color.DarkGray, topLeft, measuredText.size.toSize())
                            drawText(measuredText, topLeft = topLeft)
                        }
                    )
                )
            ),
                modifier = Modifier
                    .fillMaxSize(),

                onSelection = { xLine, points ->
                    selectedValue = points[0].y
                }
            )
        }
    }

    @Composable
    fun TenMinuteGraph(realTimeDataPoint: MutableList<LineData>) {

        if (realTimeDataPoint.isEmpty()) {
            Text(
                text = "No Data",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(
                    fontFamily = customfont,
                    color = Color.White,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            )
        } else {
            LineChart(
                lineData = realTimeDataPoint,
                color = Color(0xFF6BBEFF),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 7.dp, end = 5.dp, top = 8.dp, bottom = 5.dp),
                chartDimens = ChartDimens(8.dp),
                axisConfig = AxisConfig(
                    false, false,
                    false, false,
                    xAxisColor = Color(0xFFFFFFFF),
                    yAxisColor = Color(0xFFFFFFFF),
                    textColor = Color(0xFFFFFFFF)
                ),
                lineConfig = LineConfig(true, false)
            )
        }
    }

    // This function checks for warnings in a sensor's data and returns the number of warnings found.
    // It requires API version Build.VERSION_CODES.O or higher.
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkWarningsGraph(data: Int): Int {
        var warning = 0
        val sensorName = selectedSensor

        // Determine which sensor's data is being checked and call its checkWarnings() method
        if (sensorName == "Temperature") {
            warning = myTemperature.checkWarnings(data)
        } else if (sensorName == "Humidity") {
            warning = myHumidity.checkWarnings(data)
        } else if (sensorName == "Pressure") {
            warning = myPressure.checkWarnings(data)
        } else if (sensorName == "Altitude") {
            warning = myAltitude.checkWarnings(data)
        } else if (sensorName == "TVOC") {
            warning = myTVOC.checkWarnings(data)
        } else if (sensorName == "ECO2") {
            warning = myECO2.checkWarnings(data)
        } else if (sensorName == "Luminosity") {
            warning = myLuminosity.checkWarnings(data)
        } else if (sensorName == "Longitude") {
            warning = myLongitude.checkWarnings(data)
        } else if (sensorName == "Latitude") {
            warning = myLatitude.checkWarnings(data)
        } else if (sensorName == "Time") {
            warning = myTime.checkWarnings(data)
        } else if (sensorName == "Heart Rate") {
            warning = myHeartRate.checkWarnings(data)
        } else if (sensorName == "Oxygen Saturation") {
            warning = myOxygenSaturation.checkWarnings(data)
        } else if (sensorName == "Body Temperature") {
            warning = myBodyTemperature.checkWarnings(data)
        }

        // Return the number of warnings found for the given sensor
        return warning

    }

    //generates the cards
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RealTimeSingle(context: Context): ArrayList<Float> {
        var db = DBHandler(context)
        val id = Teammates().getIDFromName(selectedMember)
        var tempSensorName: Sensors = healthSensorsArray[0]
        tempSensorName.getHistorical(context, id)
        var tempArray: ArrayList<String> = tempSensorName.getHistorical(context, id)
        var returnArray: ArrayList<Float> = arrayListOf<Float>()
        var i = 0
        for (each in tempArray) {
            returnArray.add(tempArray[i].toFloat())
        }

        return returnArray
    }

}