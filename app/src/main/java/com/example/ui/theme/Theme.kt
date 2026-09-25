package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = BackgroundDark,
    primaryContainer = PrimaryGreenLight,
    onPrimaryContainer = MintContainer,
    secondary = SecondaryDark,
    onSecondary = BackgroundDark,
    secondaryContainer = Color(0xFF6E3A24),
    onSecondaryContainer = TerracottaContainer,
    tertiary = SaffronGold,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = MintContainer,
    onPrimaryContainer = OnMintContainer,
    secondary = Terracotta,
    onSecondary = Color.White,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = Color(0xFF5D1D09),
    tertiary = SaffronGold,
    background = WarmBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = CardSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun SobraChefTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep bespoke culinary branding consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
