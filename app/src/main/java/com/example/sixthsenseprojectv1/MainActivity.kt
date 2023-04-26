package com.example.sixthsenseprojectv1

//Communication protocol imports
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.sixthsenseprojectv1.teams.Teammates
import com.example.sixthsenseprojectv1.ui.theme.SixthSenseProjectV1Theme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalVariable().addSensorsToArray()

        val handler: Handler = Handler()
        val task: Runnable = object : Runnable {
            override fun run() {
                Teammates().generateTeamData(this@MainActivity)
                handler.postDelayed(this, 5000)
            }
        }
        task.run()
        /**for (i in 0..5){
        Teammates().generateTeamData(this@MainActivity)
        }*/

        setContent {
            SixthSenseProjectV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navigate = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(navigate)
                }
            }
        }
    }
}