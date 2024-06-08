package com.universae.navegacion.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.navegacion.R


// TODO: terminar de agregar tipografias del branding UNIVERSAE
val ralewayFamily = FontFamily(
    Font(R.font.raleway_black, FontWeight.Normal),
    Font(R.font.raleway_italic, FontWeight.Normal),
    Font(R.font.raleway_medium, FontWeight.Normal)
)

// TODO: agregar estilos de tipografía del branding UNIVERSAE
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)