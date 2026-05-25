package com.example.echoes.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echoes.data.ConcertEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertDetailScreen(
    concert: ConcertEntity,
    viewModel: ConcertViewModel,
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(concert.bandName) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Dane lokalne
            Text("Twoje wpisy (Room)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Data: ${concert.date}")
            Text("Miejsce: ${concert.location}")
            Text("Ocena: ${concert.rating} / 5")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Wspomnienia:", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Text(concert.notes)

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))

            // Dane z API
            Text("Informacje o artyście", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Text("Gatunki: ${concert.bandTags}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Biografia:", fontWeight = FontWeight.SemiBold)
            Text(concert.bandBio ?: "Brak danych z API", fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Usuń z pamiętnika", color = Color.White)
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Usunięcie wpisu") },
                text = { Text(text = "Czy na pewno chcesz usunąć ten koncert z pamiętnika? Tego działania nie można cofnąć.") },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { showDeleteDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Anuluj", color = Color.White)
                        }
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                viewModel.deleteConcert(concert)
                                onNavigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Tak, usuń", color = Color.White)
                        }
                    }
                }
            )
        }
    }
}