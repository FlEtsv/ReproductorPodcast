package com.universae.navegacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.navegacion.views.DetailView
import com.universae.navegacion.views.HomeView
import com.universae.navegacion.views.Login
import com.universae.navegacion.views.ReproductorPodcast
import com.universae.navegacion.views.SplashScreen

/**
 * Este es un componente Composable que maneja la navegación en la aplicación.
 * Utiliza un NavController para controlar la navegación entre diferentes composables.
 * El composable de inicio es "login".
 */
@Composable
fun NavManager() {
    // Crea un NavController que se recordará a través de las recomposiciones
    val navController = rememberNavController()

    // Crea un NavHost con el NavController y la ruta de inicio
    NavHost(navController = navController, startDestination = "login") {

        // Define una ruta composable para "Login"
        composable("Login") {
            Login(navController)
        }

        // Define una ruta composable para "Splash" que toma dos argumentos: "usuario" y "pass"
        composable("Splash/{usuario}/{pass}", arguments = listOf(
            navArgument("usuario") { type = NavType.StringType },
            navArgument("pass") { type = NavType.StringType }
        )) {
            val usuario = it.arguments?.getString("usuario") ?: "Null"
            val pass = it.arguments?.getString("pass") ?: "Empty"
            SplashScreen(navController, usuario, pass)
        }

        // Define una ruta composable para "Home" que toma un argumento: "id"
        composable("Home/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id") ?: -1
            HomeView(navController, id)
        }

        // Define una ruta composable para "Detail" que toma un argumento: "idAsignatura"
        composable(
            route = "Detail/{idAsignatura}",
            arguments = listOf(
                navArgument("idAsignatura") { type = NavType.IntType }
            )
        ) {
            val idAsignatura = it.arguments?.getInt("idAsignatura") ?: -1
            DetailView(navController, idAsignatura)
        }

        // Define una ruta composable para "Podcast" que toma un argumento: "idTema"
        composable(
            "Podcast/{idTema}", arguments = listOf(
                navArgument("idTema") { type = NavType.IntType }
            )
        ) {
            val idTema = it.arguments?.getInt("idTema") ?: -1
            ReproductorPodcast(navController, idTema)
        }
    }
}