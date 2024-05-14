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
        composable("Splash/{id}/{pass}", arguments = listOf(
            navArgument("id"){ type = NavType.StringType},
            navArgument("pass"){ type = NavType.StringType}
        )){
            val id = it.arguments?.getString("id") ?: "Null"
            val pass = it.arguments?.getString("pass") ?: "Empty"
            SplashScreen(navController,id, pass)
        }
        composable("Home/{id}/{Pass}", arguments = listOf(
            navArgument("id"){ type = NavType.StringType},
            navArgument("Pass"){ type = NavType.StringType}
        )){
            val id = it.arguments?.getString("id") ?: "Null"
            val pass = it.arguments?.getString("Pass") ?: "Empty"

            HomeView(navController,id, pass)
        }
        composable("Detail/{idCard}", arguments = listOf(
            navArgument("idCard"){ type = NavType.StringType})){
            val idCard = it.arguments?.getString("idCard") ?: "Null"
            DetailView(navController,idCard)
        }
        composable("Podcast/{tituloTema}", arguments = listOf(
            navArgument("tituloTema"){type = NavType.StringType},)){
            val tituloTema = it.arguments?.getString("tituloTema") ?: "Null"
            ReproductorPodcast(navController, tituloTema)
        }
    }
}