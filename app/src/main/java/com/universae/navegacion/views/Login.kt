package com.universae.navegacion.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.universae.navegacion.components.MainButton
import com.universae.navegacion.theme.Blanco
import com.universae.navegacion.theme.GrisOscuro
import com.universae.navegacion.theme.Negro
import com.universae.navegacion.theme.Rojo
import com.universae.navegacion.theme.gradientBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable que muestra la pantalla de inicio de sesión.
 *
 * @param navController Controlador de navegación para manejar la navegación entre composables.
 */
@Composable
fun Login(navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .gradientBackground()
            .pointerInput(Unit) {
                detectTapGestures {
                    // Ocultar el teclado al hacer clic fuera de los campos de texto
                    keyboardController?.hide()
                }
            }
            .fillMaxSize(1f)
            .padding(16.dp)
            .height(500.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val textColor = remember {
            mutableStateOf(Color.Black)
        }
        val borderColor = remember {
            mutableStateOf(Color.Gray)
        }
        val coroutineScope = rememberCoroutineScope()

        Image(
            painter = painterResource(id = R.mipmap.logo_universae),
            contentDescription = "Algo",
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
        )
        var user by remember { mutableStateOf("") }
        var avisoUser by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .focusable(),
            value = user,
            onValueChange = { user = it },
            label = {
                Text(
                    text = "Usuario",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blanco
                )
            },
            placeholder = {
                Text(
                    text = avisoUser,
                    style = MaterialTheme.typography.bodySmall,
                    color = Blanco
                )
            },
            singleLine = true,
            textStyle = TextStyle(color = Blanco),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Blanco,
                disabledBorderColor = Blanco,
                errorBorderColor = Rojo
            )
        )

        var clave by remember { mutableStateOf("") }
        var avisoPass by remember { mutableStateOf("") }
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blanco
                )
            },
            placeholder = {
                Text(
                    text = avisoPass,
                    style = MaterialTheme.typography.bodySmall,
                    color = Blanco
                )
            },
            singleLine = true,
            textStyle = TextStyle(color = Blanco),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Blanco,
                disabledBorderColor = Blanco,
                errorBorderColor = Rojo
            ),
            // VisualTransformation para ocultar la contraseña
            visualTransformation = PasswordVisualTransformation(),
            // Opciones del teclado para establecer el tipo de entrada como contraseña
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        Spacer(modifier = Modifier.height(20.dp))

        MainButton(
            name = "Login",
            backColor = MaterialTheme.colorScheme.primary,
            color = Blanco
        ) {
            var id = user
            var pass = clave
            if (!id.isEmpty() && !pass.isEmpty()) {
                with(navController) { navigate(route = "Splash/${id}/${pass}") }
            } else {
                avisoUser = "No deje vacios los campos"
                avisoPass = "No deje vacios los campos"
                textColor.value = Rojo
                borderColor.value = Rojo
                //coroutine para dejar el color y textos como antes
                coroutineScope.launch {
                    delay(4000)
                    textColor.value = Negro
                    borderColor.value = GrisOscuro
                    username.value = ""
                    password.value = ""
                }

            }
        }
    }
}



