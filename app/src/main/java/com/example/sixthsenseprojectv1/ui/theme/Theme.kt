package com.example.sixthsenseprojectv1.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPalette = darkColors(
//    Can override colors from the default palette if we want to, but don't need to
//    background = Color(0xFF122B00)
    primary = Color(0xFF6BBEFF)
)

@Composable
fun SixthSenseProjectV1Theme(
    content: @Composable () -> Unit
) {

    val colors = ColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}