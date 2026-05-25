package com.example.echoes.ui.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Czcionka Serif
val ElegantTypography = Typography(
    bodyLarge = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, color = TextDark),
    bodyMedium = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, color = TextDark),
    titleLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = PurplePrimary),
    labelLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
)

private val AestheticScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    background = BackgroundPastel,
    surface = CardWhite,
    onPrimary = CardWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun EchoesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AestheticScheme,
        typography = ElegantTypography,
        content = content
    )
}