package com.example.echoes.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddConcertScreen(
    viewModel: ConcertViewModel,
    onNavigateBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var bandName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    // Zmienna przechowujaca blad walidacji
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dodaj nowy koncert") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = bandName,
                onValueChange = { bandName = it; errorMessage = null }, // Reset bleddu przy pisaniu
                label = { Text("Nazwa artysty *") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = date,
                onValueChange = { date = it; errorMessage = null },
                label = { Text("Data (np. 12.05.2024)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it; errorMessage = null },
                label = { Text("Miejsce") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = rating,
                onValueChange = { rating = it; errorMessage = null },
                label = { Text("Ocena (1-5)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Twoje wspomnienia z koncertu...") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Wyswietlanie bledu, jesli jest
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    val parsedRating = rating.replace(",", ".").toFloatOrNull()

                    // Walidacja
                    if (bandName.isBlank()) {
                        errorMessage = "Nazwa artysty jest wymagana!"
                    } else if (location.isBlank()) {
                        errorMessage = "Miejsce jest wymagane!"
                    } else if (rating.isNotBlank() && (parsedRating == null || parsedRating < 1f || parsedRating > 5f)) {
                        errorMessage = "Ocena musi być liczbą z zakresu 1 do 5!"
                    } else {
                        // Jesli wszystko ok - zapisujemy
                        viewModel.saveConcert(
                            bandName = bandName,
                            date = date,
                            location = location,
                            notes = notes,
                            rating = parsedRating ?: 5f // Domyslnie 5, jesli pole oceny bylo puste
                        )
                        onNavigateBack() // Powrot na strone glowna po zapisie
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz koncert")
            }
        }
    }
}