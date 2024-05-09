package com.android.navegacion.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.android.navegacion.components.MainButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Login(navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
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

        /*
        BasicTextField(
            value = username.value,
            onValueChange = { username.value = it },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor.value, fontSize = 18.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (username.value.isEmpty()) {
                    Text("Username", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
                }
                innerTextField()
            }
        )
        */
        var user by remember {mutableStateOf("")}
        var avisoUser by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .focusable(),
            value = user,
            onValueChange = {user = it},
            label = { Text(text = "Usuario", style = MaterialTheme.typography.bodyMedium, color = Color.White)},
            placeholder = { Text(text=avisoUser, style = MaterialTheme.typography.bodySmall, color = Color.White)},
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                disabledBorderColor = Color.White,
                cursorColor = Color.White
            )
        )

        var clave by remember {mutableStateOf("")}
        var avisoPass by remember { mutableStateOf("") }
        OutlinedTextField(
            value = clave,
            onValueChange = {clave = it},
            label = { Text(text = "Contraseña", style = MaterialTheme.typography.bodyMedium, color = Color.White)},
            placeholder = { Text(text=avisoPass, style = MaterialTheme.typography.bodySmall, color = Color.White)},
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                disabledBorderColor = Color.White,
                cursorColor = Color.White
            ),
            // VisualTransformation para ocultar la contraseña
            visualTransformation = PasswordVisualTransformation(),
            // Opciones del teclado para establecer el tipo de entrada como contraseña
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        /*
        BasicTextField(
            value = password.value,
            onValueChange = { password.value = it },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor.value, fontSize = 18.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (password.value.isEmpty()) {
                    Text("Password", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
                }
                innerTextField()
            }
        )
        */
        Spacer(modifier = Modifier.height(20.dp))

        MainButton(name = "Login", backColor = MaterialTheme.colorScheme.primary, color = Color.White) {
            var id = user
            var pass = clave
            if (!id.isEmpty() && !pass.isEmpty()) {
                with(navController) { navigate(route = "Splash/${id}/${pass}") }
            } else {
                avisoUser= "No deje vacios los campos"
                avisoPass = "No deje vacios los campos"
                textColor.value = Color.Red
                borderColor.value = Color.Red
                //coroutine para dejar el color y textos como antes
                coroutineScope.launch {
                    delay(4000)
                    textColor.value = Color.Black
                    borderColor.value = Color.Gray
                    username.value = ""
                    password.value = ""
                }

            }
        }
    }
}



