package com.android.navegacion.views


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.navegacion.components.MainButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Login(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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

        Text("Login", style = MaterialTheme.typography.titleMedium)

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

        MainButton(name = "Login", backColor = Color.Magenta, color = Color.White) {
            var id = username.value
            var pass = password.value
            if (!id.isEmpty() && !pass.isEmpty()) {
                with(navController) { navigate(route = "Splash/${id}/${pass}") }
            } else {
                username.value = "No deje vacios los campos"
                password.value = "No deje vacios los campos"
                textColor.value = Color.Red
                borderColor.value = Color.Red
                //coroutine para dejar el color y textos como antes
                coroutineScope.launch {
                    delay(2000)
                    textColor.value = Color.Black
                    borderColor.value = Color.Gray
                    username.value = ""
                    password.value = ""
                }

            }
        }
    }
}



