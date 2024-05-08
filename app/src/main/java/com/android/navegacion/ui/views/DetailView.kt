package com.android.navegacion.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.android.navegacion.components.MainButton
import com.android.navegacion.components.MainIconButton
import com.android.navegacion.components.Space
import com.android.navegacion.components.TitleBar
import com.android.navegacion.components.TitleView

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailView(navController: NavController){
    Scaffold (
        topBar = {
            TopAppBar(
                title = { TitleBar(name = "Detail View")},
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Blue
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ){
        ContentDetailView(navController)
    }
}
@Composable
fun ContentDetailView(navController: NavController){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleView(name = "Detail View")
        Space()
        MainButton(name = "Return Home  View", backColor = Color.Blue , color =Color.White ) {
            navController.popBackStack()
        }
    }
}