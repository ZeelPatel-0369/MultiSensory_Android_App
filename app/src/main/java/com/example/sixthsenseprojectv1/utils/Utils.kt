package com.example.sixthsenseprojectv1.utils

import kotlin.math.round

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun truncateLargeNumber(num: Double, decimals: Int): String {
    if (num / 1000000000000 >= 1) {
        return "" + (num / 1000000000000).round(decimals) + "T"
    } else if (num / 1000000000 >= 1) {
        return "" + (num / 1000000000).round(decimals) + "B"
    } else if (num / 1000000 >= 1) {
        return "" + (num / 1000000).round(decimals) + "M"
    } else if (num / 1000 >= 1) {
        return "" + (num / 1000).round(decimals) + "K"
    } else {
        return "" + num.round(decimals)
    }
}