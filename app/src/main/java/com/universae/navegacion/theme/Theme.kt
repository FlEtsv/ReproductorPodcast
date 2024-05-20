package com.universae.reproductor.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// TODO: agregar colores del branding UNIVERSAE
private val DarkColorScheme = darkColorScheme(
    primary = Blanco,
    secondary = AzulClaro,
    tertiary = AzulMedio,

    // Other default colors to override
    background = AzulDark,
    surface = Blanco,
    onPrimary = GrisClaro,
    onSecondary = GrisClaro,
    onTertiary = GrisOscuro,
    onBackground = GrisOscuro,
    onSurface = Negro,
    onError = Rojo
)

private val LightColorScheme = lightColorScheme(
    primary = AzulMedio,
    secondary = Blanco,
    tertiary = AzulClaro,

    // Other default colors to override
    background = AzulDark,
    surface = Blanco,
    onPrimary = GrisClaro,
    onSecondary = GrisClaro,
    onTertiary = AzulOscuro,
    onBackground = GrisOscuro,
    onSurface = Negro,
    onError = Rojo
)

@Composable
fun ReproductorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*
        // esquma de colores dinamico en base al fondo de pantalla del usuario
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
         */
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
fun Modifier.gradientBackground(): Modifier {
    return this.background(
        brush = Brush.linearGradient(
            colors = listOf(
                AzulDark,
                AzulOscuro,
                AzulMedio
            )
        )
    )
}