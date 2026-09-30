package com.powerlift.sbdlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.powerlift.sbdlog.data.AppDatabase
import com.powerlift.sbdlog.ui.AddLogScreen
import com.powerlift.sbdlog.ui.HistoryScreen
import com.powerlift.sbdlog.ui.HomeScreen
import com.powerlift.sbdlog.ui.theme.SBDLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SBDLogTheme {
                SBDLogApp()
            }
        }
    }
}

@Composable
fun SBDLogApp() {
    val navController = rememberNavController()
    val db = AppDatabase.get(androidx.compose.ui.platform.LocalContext.current)
    val viewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModel.Factory(db.workoutLogDao())
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { navController.navigate("add") },
                    onNavigateToHistory = { navController.navigate("history") }
                )
            }
            composable("add") {
                AddLogScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("history") {
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
