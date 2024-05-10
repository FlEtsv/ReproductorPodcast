package com.android.navegacion.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.universae.reproductor.ui.theme.AzulClaro

@Composable
fun TitleBar (name: String){
    Text(text =  name, fontSize = 25.sp, color = Color.White)
}
@Composable
fun ActionButton() {
    FloatingActionButton(
        onClick = { /*TODO*/ },
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription ="Agregar" )
    }
}
@Composable
fun MainIconButton(icon : ImageVector, onClick:() -> Unit){
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}