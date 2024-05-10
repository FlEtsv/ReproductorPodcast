import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.android.navegacion.components.MainIconButton
import com.android.navegacion.components.iconArroyBack
import com.android.navegacion.components.iconArroyForward
import com.android.navegacion.components.iconCast
import com.android.navegacion.components.iconFastForward
import com.android.navegacion.components.iconFastReward
import com.android.navegacion.components.iconPause
import com.android.navegacion.components.iconPlay
import com.universae.reproductor.ui.theme.AzulClaro
import com.universae.reproductor.ui.theme.AzulOscuro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ReproductorPodcast(navController: NavController, tituloTema : String) {
    var reproduciendo by remember { mutableStateOf(false) }
    val progress = remember { mutableStateOf(0.0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f), // Ocupa la mitad del ancho disponible
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp) // Ajusta el espaciado según sea necesario
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) // Ajusta el contenido de descripción según sea necesario
            }
        }

        Column(
            modifier = Modifier.weight(1f), // Ocupa la otra mitad del ancho disponible
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            BotonCompartir()
        }
    }
        Spacer(modifier = Modifier.height(100.dp))
        PortadaPodcast()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tituloTema,
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                for (i in 1..100) {
                    delay(100) // Simula un trabajo que toma tiempo 1 segundo
                    progress.value = i / 100f
                }
            }
        }
        ProgressBarRow(progress = progress)
        Spacer(modifier = Modifier.height(16.dp))
        ControlesReproduccion(
            reproduciendo = reproduciendo,
            onReproduccionPausaToggle = { reproduciendo = !reproduciendo },
            onRetroceder = { /* TODO: Retroceder */ },
            onAvanzar = { /* TODO: Avanzar */ },
            onAvanzarRapido = { },
            onBajarVelocidad  = { }
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun PortadaPodcast() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(AzulOscuro),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Portada del Podcast",
            modifier = Modifier.size(150.dp),
            tint = AzulClaro
        )

    }
}

@Composable
fun ControlesReproduccion(
    reproduciendo: Boolean,
    onReproduccionPausaToggle: () -> Unit,
    onRetroceder: () -> Unit,
    onAvanzar: () -> Unit,
    onAvanzarRapido: () -> Unit,
    onBajarVelocidad : () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBajarVelocidad}) {
            Icon(
                imageVector = iconFastReward(),
                contentDescription = "Avanzar",
                tint = Color.White
            )
        }
        IconButton(onClick = onRetroceder) {
            Icon(
                imageVector = iconArroyBack(),
                contentDescription = "Retroceder",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onReproduccionPausaToggle) {
            Icon(
                imageVector = if (reproduciendo) iconPause() else iconPlay(),
                contentDescription = if (reproduciendo) "Pausa" else "Reproducir",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onAvanzar) {
            Icon(
                imageVector = iconArroyForward(),
                contentDescription = "Avanzar",
                tint = Color.White
            )
        }
        IconButton(onClick = onAvanzarRapido) {
            Icon(
                imageVector = iconFastForward(),
                contentDescription = "Avanzar",
                tint = Color.White
            )
        }
    }
}

@Composable
fun BotonCompartir() {
    IconButton(
        onClick = { /* TODO: Lógica para compartir con Android Auto */ }
    ) {
        Icon(
            imageVector = iconCast(),
            contentDescription = "Compartir",
            tint = Color.White
        )
    }
}
@Composable
fun ProgressBarRow(progress: MutableState<Float>) {  // 'progress' debería ser un valor entre 0f y 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), content = {
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxWidth(),
                color = AzulOscuro,
                trackColor = AzulClaro
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPodcast() {

    var idTitulo : String= "titulo del Podcast"
    ReproductorPodcast(navController = rememberNavController(), idTitulo)
}
