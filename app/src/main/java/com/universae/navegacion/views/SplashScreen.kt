package com.android.navegacion.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.universae.domain.usecases.SesionUseCase
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.usecases.AlumnoUseCaseImpl
import com.universae.navegacion.theme.gradientBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

@Composable
fun SplashScreen(navController: NavController, usuario: String, pass: String) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            // TODO("Mirar si quitar error por un cambio en navegacion si tarda demasiado en conectar, ej: volver a pantalla login")
            var usuarioEncontrado: Boolean = false
            try {
                withTimeout(10000) { // Timeout of 10 seconds
                    withContext(Dispatchers.IO) {
                        //COMPARAR CON BBDD DESDE PREVIEWDATA
                        while (!DataFech(usuario = usuario, password = pass)) {
                            delay(1000)
                        }
                    }
                }
                usuarioEncontrado = true
            } catch (e: TimeoutCancellationException) {
                // Manejar la excepción de tiempo de espera aquí
                println("La operación de la base de datos excedió el tiempo límite después de 10 segundos.")
            }
            var alumno: Alumno? = null

            if (usuarioEncontrado) {
                usuarioEncontrado = false
                alumno = AlumnoUseCaseImpl.getAlumno(nombreUsuario = usuario, clave = pass)
            }

            if (alumno != null) {
                val id: Int = alumno.alumnoId.id
                navController.navigate("Home/$id") {
                    popUpTo("Login") { inclusive = true }
                }
            } else {
                navController.navigate("Login") {
                    popUpTo("SplashScreen") { inclusive = true }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo_universae),
            contentDescription = "Algo",
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        /*
        Text(

            text = "Cargando...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        */
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.primary,
        )
    }
}

fun DataFech(usuario: String, password: String): Boolean {
    return SesionUseCase.iniciarSesion(usuario, password)
}