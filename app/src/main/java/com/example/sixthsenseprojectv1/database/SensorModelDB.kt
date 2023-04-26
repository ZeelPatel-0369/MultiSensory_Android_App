package com.example.sixthsenseprojectv1.database

import java.time.LocalDateTime

//model declared for storing the data in the database
data class SensorModelDB(
    var userID: String,
    var sensorName: String,
    var data: String,
    var warning: String,
    var Time: String
)

data class TimedDataPoint(var time: LocalDateTime, var data: Float)