package com.lastwagon.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object WagonColors {
    val Graphite950 = Color(0xFF0A0D10); val Graphite900 = Color(0xFF111418)
    val Graphite800 = Color(0xFF191E24); val Steel700 = Color(0xFF29313A)
    val Steel500 = Color(0xFF677482); val Steel300 = Color(0xFFAAB4BE)
    val Steel100 = Color(0xFFE8EDF2); val MarkerAmber = Color(0xFFFFB547)
    val MarkerAmberDim = Color(0xFF6C4B19); val DashboardBlue = Color(0xFF69C4FF)
    val BrakeRed = Color(0xFFFF5C5C); val SignalGreen = Color(0xFF55D68B)
}
object WagonSpacing {
    val xxs = 4.dp; val xs = 8.dp; val sm = 12.dp; val md = 16.dp
    val lg = 24.dp; val xl = 32.dp; val xxl = 48.dp
}
val WagonShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
)
private val WagonTypography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Black, fontSize = 44.sp, lineHeight = 48.sp, letterSpacing = (-1).sp),
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Black, fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, lineHeight = 28.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = 0.2.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 0.5.sp),
    labelMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.8.sp),
)
private val DarkScheme = darkColorScheme(
    primary = WagonColors.MarkerAmber, onPrimary = WagonColors.Graphite950,
    primaryContainer = WagonColors.MarkerAmberDim, onPrimaryContainer = Color(0xFFFFD9A0),
    secondary = WagonColors.DashboardBlue, onSecondary = WagonColors.Graphite950,
    tertiary = WagonColors.SignalGreen, error = WagonColors.BrakeRed,
    background = WagonColors.Graphite950, onBackground = WagonColors.Steel100,
    surface = WagonColors.Graphite900, onSurface = WagonColors.Steel100,
    surfaceVariant = WagonColors.Graphite800, onSurfaceVariant = WagonColors.Steel300,
    outline = WagonColors.Steel500,
)
private val LightScheme = lightColorScheme(
    primary = Color(0xFF855400), onPrimary = Color.White, secondary = Color(0xFF00658A),
    tertiary = Color(0xFF006C48), background = Color(0xFFF3F5F7),
    onBackground = WagonColors.Graphite900, surface = Color.White,
    onSurface = WagonColors.Graphite900, surfaceVariant = Color(0xFFE3E8ED),
    onSurfaceVariant = WagonColors.Steel700, error = Color(0xFFBA1A1A),
)
val LocalReduceMotion = staticCompositionLocalOf { false }

@Composable fun LastWagonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), largeText: Boolean = false, reduceMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    val type = if (largeText) WagonTypography.copy(
        bodyLarge = WagonTypography.bodyLarge.copy(fontSize = 19.sp, lineHeight = 27.sp),
        bodyMedium = WagonTypography.bodyMedium.copy(fontSize = 17.sp, lineHeight = 24.sp),
    ) else WagonTypography
    CompositionLocalProvider(LocalReduceMotion provides reduceMotion) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            shapes = WagonShapes,
            typography = type,
            content = content,
        )
    }
}
