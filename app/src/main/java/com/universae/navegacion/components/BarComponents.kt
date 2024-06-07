package com.universae.navegacion.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.universae.navegacion.theme.Blanco
import com.universae.navegacion.theme.ralewayFamily

/**
 * Barra de título personalizada.
 * @param name El nombre que se mostrará en la barra de título.
 */
@Composable
fun TitleBar(name: String) {
    Text(text = name, fontSize = 25.sp, fontFamily = ralewayFamily, color = Blanco)
}

/**
 * Botón de icono principal personalizado.
 * @param icon El icono que se mostrará en el botón.
 * @param onClick La acción que se ejecutará al hacer clic en el botón.
 */
@Composable
fun MainIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null, tint = Blanco)
    }
}