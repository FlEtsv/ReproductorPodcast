package com.universae.navegacion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.universae.navegacion.navigation.NavManager
import com.universae.navegacion.theme.ReproductorTheme


/**
 * Clase MainActivity que hereda de ComponentActivity.
 * Esta es la actividad principal de la aplicación y se encarga de establecer el contenido de la vista.
 */
class MainActivity : ComponentActivity() {
    // Anotación para suprimir la advertencia de parámetro de relleno no utilizado en Material3Scaffold
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReproductorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavManager()
                }
            }
        }
    }
}