package com.example.sixthsenseprojectv1.teams

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.sixthsenseprojectv1.R
import com.example.sixthsenseprojectv1.allSensorsArray
import com.example.sixthsenseprojectv1.fakeSensorsArray
import com.example.sixthsenseprojectv1.squad

class Teammates {
    //print the squad members' id and name
    fun printSquad() {
        val size1 = squad.size - 1
        val size2 = squad[0].size - 1

        for (i in 0 until size1) {
            for (j in 0..size2) {
                val id = squad[i][j]
                val name = squad[i + 1][j]
                println("$id = $name")
            }
        }
    }

    //assigns a pin to a team member and returns the pin from the drawables for the map
    fun returnPins(member: String, context: Context): Int {


        val id = Teammates().getIDFromName(member)
        var warning = 0
        for (i in allSensorsArray) {
            val tempWarning = i.getWarningFromDatabase(context, id)
            if (tempWarning == 1) {
                warning += 1
            }
        }


        var pin = R.drawable.location_on_red
        when (member) {
            "Me" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_red
                } else {
                    R.drawable.location_on_red
                }
            }
            "Baker" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_green
                } else {
                    R.drawable.location_on_green
                }
            }
            "Carter" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_purple
                } else {
                    R.drawable.location_on_purple
                }
            }
            "Dean" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_blue
                } else {
                    R.drawable.location_on_blue
                }
            }
            "John" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_white
                } else {
                    R.drawable.location_on_white
                }
            }
            "Tim" -> {
                pin = if (warning > 0) {
                    R.drawable.wlocation_on_orange
                } else {
                    R.drawable.location_on_orange
                }
            }
        }

        return pin
    }

    //return the color of pin for floating bar because it resets the color
    fun returnPinsColor(member: String): Color {
        var color: Color = Color.Red
        when (member) {
            "Me" -> {
                color = Color.Red
            }
            "Baker" -> {
                color = Color.Green
            }
            "Carter" -> {
                color = Color.Magenta
            }
            "Dean" -> {
                color = Color.Blue
            }
            "John" -> {
                color = Color.White
            }
            "Tim" -> {
                color = Color.Yellow
            }
        }

        return color
    }

    //generate data for each of the team member
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateTeamData(context: Context) {
        val size2 = squad[0].size - 2

        for (i in 1..1) {
            for (j in 0..size2) {
                if (j == 0) {
                    val id: Int = squad[0][j] as Int
                    /**for (tempSensor in allSensorsArray){
                    var data = 0.0
                    if (tempSensor.getName() == "Temperature"){
                    val list = listOf(12.0, 21.0, 13.0, 14.0, -54.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Humidity"){
                    val list = listOf(91.0, 95.0, 98.0, 96.0, 92.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Pressure"){
                    val list = listOf(190.0, 89.0, 99.0, 88.0, 91.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Altitude"){
                    val list = listOf(1.0, 5.0, 8.0, 6.0, 2.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "TVOC"){
                    val list = listOf(9100.0, 3000.0, 9800.0, 9600.0, 9200.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "ECO2"){
                    val list = listOf(910.0, 950.0, 980.0, 960.0, 920.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Luminosity"){
                    val list = listOf(9100.0, 9500.0, 9800.0, 9600.0, 9200.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Longitude"){
                    val list = listOf(-63.2311, -63.1231, -63.3185, -63.1231, -63.3185)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Latitude"){
                    val list = listOf(46.2311, 46.1231, 46.3185, 46.1231, 46.3185)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Time"){
                    data = 0.0
                    //cannot check
                    }
                    else if(tempSensor.getName() == "Heart Rate"){
                    val list = listOf(91.0, 95.0, 98.0, 96.0, 92.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Oxygen Saturation"){
                    val list = listOf(40.0, 35.0, 89.0, 10.0, 5.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    else if(tempSensor.getName() == "Body Temperature"){
                    val list = listOf(49.0, 41.0, 40.0, 42.0, 45.0)
                    val randomIndex = Random.nextInt(list.size)
                    data = list[randomIndex]
                    }
                    tempSensor.addToDatabase(id,tempSensor.getName(),data,0,LocalDateTime.now(),context)
                    }*/
                    for (tempSensor in fakeSensorsArray) {
                        tempSensor.dummyProvider(id, context)
                    }
                } else {
                    val id: Int = squad[0][j] as Int
                    for (tempSensor in allSensorsArray) {
                        tempSensor.dummyProvider(id, context)
                    }
                }
            }
        }
    }

    //get the ID of the team member based on the name
    fun getIDFromName(teammate: String): Int {
        var id = 1
        val size2 = squad[0].size - 1

        for (j in 0..size2) {
            if (squad[1][j] == teammate) {
                val tempId: Int = squad[0][j] as Int
                id = tempId
                return tempId
            }
        }
        return id
    }

}