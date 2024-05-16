package com.android.navegacion.navigation

import HomeView
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.navegacion.ui.player.ReproductorPodcast
import com.android.navegacion.views.DetailView
import com.android.navegacion.views.Login
import com.android.navegacion.views.SplashScreen

@Composable
fun NavManager(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"  ){
        composable("Login"){
            Login(navController)
        }
        composable("Splash/{usuario}/{pass}", arguments = listOf(
            navArgument("usuario"){ type = NavType.StringType},
            navArgument("pass"){ type = NavType.StringType}
        )){
            val usuario = it.arguments?.getString("usuario") ?: "Null"
            val pass = it.arguments?.getString("pass") ?: "Empty"
            SplashScreen(navController,usuario, pass)
        }
        composable("Home/{id}", arguments = listOf(
            navArgument("id"){ type = NavType.IntType}
        )){
            val id = it.arguments?.getInt("id") ?: -1
            HomeView(navController,id)
        }
        composable(
            route = "Detail/{idAsignatura}",
            arguments = listOf(
                navArgument("idAsignatura") { type = NavType.IntType }
            )
        ) {
            val idAsignatura = it.arguments?.getInt("idAsignatura") ?: -1
            DetailView(navController, idAsignatura)
        }
        composable("Podcast/{tituloTema}", arguments = listOf(
            navArgument("tituloTema"){type = NavType.StringType},)){
            val tituloTema = it.arguments?.getString("tituloTema") ?: "Null"
            ReproductorPodcast(navController, tituloTema)
        }
    }
}