package com.example.ticketway.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ticketway.ui.ui.theme.*
import androidx.compose.ui.graphics.Color

@RequiresApi(Build.VERSION_CODES.O)
private val DarkColorScheme = darkColorScheme(
    // Primary/Accent
    primary = PrimaryGreen,
    onPrimary = DarkOnPrimary, // NEW: Use DarkOnPrimary for better contrast on the bright green

    // Backgrounds
    background = DarkBackground, // Deep standard dark gray
    surface = DarkSurface,      // Very Dark Grey - Cards, Nav Bar
    surfaceVariant = DarkSurfaceVariant, // NEW: Distinct medium-dark gray for League Section

    // Text/Content on surfaces
    onBackground = LightOnDarkText,
    onSurface = LightOnDarkText,
    onSurfaceVariant = SecondaryLightOnDarkText,

    // Secondary Text/Content
    secondary = SecondaryLightOnDarkText,
    onSecondary = LightOnDarkText
)

@RequiresApi(Build.VERSION_CODES.O)
private val LightColorScheme = lightColorScheme(
    // Primary/Accent
    primary = PrimaryGreen,
    onPrimary = Color.White,

    // Backgrounds
    background = Color.White,
    surface = Color.White,
    surfaceVariant = LightGray, // Off-white gray

    // Text/Content on surfaces
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = LightText,

    // Secondary Text/Content
    secondary = LightText,
    onSecondary = DarkText
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketWayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}