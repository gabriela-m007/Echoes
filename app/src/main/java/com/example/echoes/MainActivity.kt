package com.example.echoes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echoes.data.AppDatabase
import com.example.echoes.data.ConcertRepository
import com.example.echoes.ui.main.AddEditConcertScreen
import com.example.echoes.ui.main.ConcertDetailScreen
import com.example.echoes.ui.main.ConcertViewModel
import com.example.echoes.ui.main.ConcertViewModelFactory
import com.example.echoes.ui.main.MainScreen
import com.example.echoes.ui.main.EchoesTheme

// Glowna aktywnosc zarządzajaca zyciem aplikacji i przelaczaniem ekranow (Nawigacja)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Uruchomienie bazy danych przy starcie aplikacji
        val database = AppDatabase.getDatabase(this)
        val repository = ConcertRepository(database.concertDao())
        val factory = ConcertViewModelFactory(repository)

        setContent {
            EchoesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    // Kontroler systemu nawigacji (Jetpack Navigation Compose)
                    val navController = rememberNavController()
                    val viewModel: ConcertViewModel = viewModel(factory = factory)

                    NavHost(navController = navController, startDestination = "main") {

                        composable("main") {
                            MainScreen(viewModel, { navController.navigate("addEdit") }, { id -> navController.navigate("detail/$id") })
                        }

                        composable("addEdit") {
                            AddEditConcertScreen(viewModel, null, { navController.popBackStack() })
                        }

                        composable("addEdit/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            val concerts by viewModel.allConcerts.collectAsState()
                            val concertToEdit = concerts.find { it.id == id }
                            AddEditConcertScreen(viewModel, concertToEdit, { navController.popBackStack() })
                        }

                        composable("detail/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            val concerts by viewModel.allConcerts.collectAsState()
                            val selectedConcert = concerts.find { it.id == id }

                            if (selectedConcert != null) {
                                ConcertDetailScreen(selectedConcert, viewModel, { navController.popBackStack() }, { navController.navigate("addEdit/${selectedConcert.id}") })
                            }
                        }
                    }
                }
            }
        }
    }
}