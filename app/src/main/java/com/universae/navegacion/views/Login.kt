package com.android.navegacion.views


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
import com.android.navegacion.components.MainButton
import com.universae.navegacion.theme.gradientBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
                    color = Color.White
                )
            },
            placeholder = {
                Text(
                    text = avisoUser,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            },
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                /*
                TODO("cambiar colores por MaterialTheme")
                focusedTextColor = OutlinedTextFieldTokens.FocusInputColor.value,
                unfocusedTextColor = OutlinedTextFieldTokens.InputColor.value,
                disabledTextColor = OutlinedTextFieldTokens.DisabledInputColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorTextColor = OutlinedTextFieldTokens.ErrorInputColor.value,
                 */


                /*
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = Color.White,

                 */
                /*
                TODO("cambiar colores por MaterialTheme")
                errorCursorColor = OutlinedTextFieldTokens.ErrorFocusCaretColor.value,
                selectionColors = LocalTextSelectionColors.current,
                 */


                //focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                disabledBorderColor = Color.White,
                errorBorderColor = MaterialTheme.colorScheme.error,


                /*
                TODO("cambiar colores por MaterialTheme")
                focusedLeadingIconColor = OutlinedTextFieldTokens.FocusLeadingIconColor.value,
                unfocusedLeadingIconColor = OutlinedTextFieldTokens.LeadingIconColor.value,
                disabledLeadingIconColor = OutlinedTextFieldTokens.DisabledLeadingIconColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledLeadingIconOpacity),
                errorLeadingIconColor = OutlinedTextFieldTokens.ErrorLeadingIconColor.value,
                focusedTrailingIconColor = OutlinedTextFieldTokens.FocusTrailingIconColor.value,
                unfocusedTrailingIconColor = OutlinedTextFieldTokens.TrailingIconColor.value,
                disabledTrailingIconColor = OutlinedTextFieldTokens.DisabledTrailingIconColor
                    .value.copy(alpha = OutlinedTextFieldTokens.DisabledTrailingIconOpacity),
                errorTrailingIconColor = OutlinedTextFieldTokens.ErrorTrailingIconColor.value,
                focusedLabelColor = OutlinedTextFieldTokens.FocusLabelColor.value,
                unfocusedLabelColor = OutlinedTextFieldTokens.LabelColor.value,
                disabledLabelColor = OutlinedTextFieldTokens.DisabledLabelColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledLabelOpacity),
                errorLabelColor = OutlinedTextFieldTokens.ErrorLabelColor.value,
                focusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                unfocusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                disabledPlaceholderColor = OutlinedTextFieldTokens.DisabledInputColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                focusedSupportingTextColor = OutlinedTextFieldTokens.FocusSupportingColor.value,
                unfocusedSupportingTextColor = OutlinedTextFieldTokens.SupportingColor.value,
                disabledSupportingTextColor = OutlinedTextFieldTokens.DisabledSupportingColor
                    .value.copy(alpha = OutlinedTextFieldTokens.DisabledSupportingOpacity),
                errorSupportingTextColor = OutlinedTextFieldTokens.ErrorSupportingColor.value,
                focusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                unfocusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                disabledPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                focusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                unfocusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                disabledSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,

                 */
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
                    color = Color.White
                )
            },
            placeholder = {
                Text(
                    text = avisoPass,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            },
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                /*
                TODO("cambiar colores por MaterialTheme")
                focusedTextColor = OutlinedTextFieldTokens.FocusInputColor.value,
                unfocusedTextColor = OutlinedTextFieldTokens.InputColor.value,
                disabledTextColor = OutlinedTextFieldTokens.DisabledInputColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorTextColor = OutlinedTextFieldTokens.ErrorInputColor.value,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = Color.White,
                errorCursorColor = OutlinedTextFieldTokens.ErrorFocusCaretColor.value,
                selectionColors = LocalTextSelectionColors.current,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                disabledBorderColor = Color.White,
                errorBorderColor = OutlinedTextFieldTokens.ErrorOutlineColor.value,
                focusedLeadingIconColor = OutlinedTextFieldTokens.FocusLeadingIconColor.value,
                unfocusedLeadingIconColor = OutlinedTextFieldTokens.LeadingIconColor.value,
                disabledLeadingIconColor = OutlinedTextFieldTokens.DisabledLeadingIconColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledLeadingIconOpacity),
                errorLeadingIconColor = OutlinedTextFieldTokens.ErrorLeadingIconColor.value,
                focusedTrailingIconColor = OutlinedTextFieldTokens.FocusTrailingIconColor.value,
                unfocusedTrailingIconColor = OutlinedTextFieldTokens.TrailingIconColor.value,
                disabledTrailingIconColor = OutlinedTextFieldTokens.DisabledTrailingIconColor
                    .value.copy(alpha = OutlinedTextFieldTokens.DisabledTrailingIconOpacity),
                errorTrailingIconColor = OutlinedTextFieldTokens.ErrorTrailingIconColor.value,
                focusedLabelColor = OutlinedTextFieldTokens.FocusLabelColor.value,
                unfocusedLabelColor = OutlinedTextFieldTokens.LabelColor.value,
                disabledLabelColor = OutlinedTextFieldTokens.DisabledLabelColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledLabelOpacity),
                errorLabelColor = OutlinedTextFieldTokens.ErrorLabelColor.value,
                focusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                unfocusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                disabledPlaceholderColor = OutlinedTextFieldTokens.DisabledInputColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                focusedSupportingTextColor = OutlinedTextFieldTokens.FocusSupportingColor.value,
                unfocusedSupportingTextColor = OutlinedTextFieldTokens.SupportingColor.value,
                disabledSupportingTextColor = OutlinedTextFieldTokens.DisabledSupportingColor
                    .value.copy(alpha = OutlinedTextFieldTokens.DisabledSupportingOpacity),
                errorSupportingTextColor = OutlinedTextFieldTokens.ErrorSupportingColor.value,
                focusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                unfocusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                disabledPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                focusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                unfocusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                disabledSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value
                    .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                errorSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,

                 */
                //focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                disabledBorderColor = Color.White,
                errorBorderColor = MaterialTheme.colorScheme.error,
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

        MainButton(
            name = "Login",
            backColor = MaterialTheme.colorScheme.primary,
            color = Color.White
        ) {
            var id = user
            var pass = clave
            if (!id.isEmpty() && !pass.isEmpty()) {
                with(navController) { navigate(route = "Splash/${id}/${pass}") }
            } else {
                avisoUser = "No deje vacios los campos"
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



