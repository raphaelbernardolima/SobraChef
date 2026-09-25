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

// =========================================================================
// Material 3 Color Schemes - Fully WCAG AA Compliant
// =========================================================================

val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = MintContainer,
    onPrimaryContainer = OnMintContainer,

    secondary = Terracotta,
    onSecondary = Color.White,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = OnTerracottaContainer,

    tertiary = SaffronGold,
    onTertiary = Color.White,
    tertiaryContainer = SaffronContainer,
    onTertiaryContainer = OnSaffronGold,

    error = UrgentRed,
    onError = Color.White,
    errorContainer = UrgentBg,
    onErrorContainer = OnUrgentBg,

    background = WarmBackgroundLight,
    onBackground = TextPrimaryLight,

    surface = CardSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,

    outline = BorderMedium,
    outlineVariant = BorderSubtle,

    inverseSurface = TextPrimaryLight,
    inverseOnSurface = WarmBackgroundLight,
    inversePrimary = PrimaryDark,

    surfaceTint = PrimaryGreen
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,

    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,

    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,

    error = UrgentRedDark,
    onError = OnUrgentDark,
    errorContainer = UrgentBgDark,
    onErrorContainer = OnUrgentBgDark,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,

    outline = BorderMediumDark,
    outlineVariant = BorderSubtleDark,

    inverseSurface = TextPrimaryDark,
    inverseOnSurface = BackgroundDark,
    inversePrimary = PrimaryGreen,

    surfaceTint = PrimaryDark
)

@Composable
fun SobraChefTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep bespoke culinary identity consistent & high contrast
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
