package com.example.echoes.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echoes.R
import com.example.echoes.data.ConcertEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Lawendowe tlo z kolkami
val LavenderGradient = Brush.verticalGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFD8B4E2)))

@Composable
fun AestheticBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Kolka na gradiencie
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = LavenderGradient)
            drawCircle(color = Color.White.copy(alpha = 0.3f), radius = 400f, center = Offset(0f, 0f))
            drawCircle(color = Color.White.copy(alpha = 0.2f), radius = 600f, center = Offset(size.width, size.height * 0.3f))
            drawCircle(color = Color.White.copy(alpha = 0.25f), radius = 500f, center = Offset(size.width * 0.4f, size.height))
        }
        content() // Nakladamy interfejs na tlo
    }
}

// EKRAN GLOWNY
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ConcertViewModel, onNavigateToAdd: () -> Unit, onNavigateToDetail: (Int) -> Unit) {
    val concerts by viewModel.allConcerts.collectAsState()

    AestheticBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Echoes", style = MaterialTheme.typography.titleLarge, fontSize = 32.sp) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToAdd, containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, null) }, text = { Text("Dodaj koncert", color = Color(0xFFFFFFFF)) }
                )
            }
        ) { padding ->
            if (concerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pusto tutaj... Zapisz pierwszy koncert! 🎵", fontWeight = FontWeight.Bold, color = Color(0xFF5A189A))
                }
            } else {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                    items(concerts) { concert ->
                        ConcertItem(concert) { onNavigateToDetail(concert.id) }
                    }
                }
            }
        }
    }
}

// POJEDYNCZY WPIS NA LISCIE
@Composable
fun ConcertItem(concert: ConcertEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (concert.bandImageUrl != null) {
                AsyncImage(
                    model = concert.bandImageUrl, contentDescription = "Zdjecie artysty",
                    contentScale = ContentScale.Crop, modifier = Modifier.size(85.dp).clip(RoundedCornerShape(16.dp))
                )
            } else {
                Box(modifier = Modifier.size(85.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE0B0FF)), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(id = R.drawable.music_note), contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = concert.bandName, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.calendar_today), contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = concert.date, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = concert.location, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                }
            }
        }
    }
}

// EKRAN SZCZEGOLOW
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertDetailScreen(concert: ConcertEntity, viewModel: ConcertViewModel, onNavigateBack: () -> Unit, onEdit: () -> Unit) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Logika okienka potwierdzajacego usuniencie wpisu
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Usuwanie wpisu", style = MaterialTheme.typography.titleLarge) },
            text = { Text("Czy na pewno chcesz usunąć ten koncert z pamiętnika? Tej akcji nie można cofnąć.", style = MaterialTheme.typography.bodyLarge) },
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
                            viewModel.deleteConcert(concert) // Faktyczne usuniecie
                            onNavigateBack() // Powrot do listy
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD00000))
                    ) {
                        Text("Tak, usuń", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {},
            containerColor = Color.White
        )
    }

    AestheticBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { CenterAlignedTopAppBar(title = { Text(concert.bandName, style = MaterialTheme.typography.titleLarge) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)) }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

                if (concert.bandImageUrl != null) {
                    AsyncImage(model = concert.bandImageUrl, contentDescription = "Zdjecie artysty", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(260.dp).clip(RoundedCornerShape(20.dp)))
                } else {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(20.dp)).background(Color(0xFFE0B0FF)), contentAlignment = Alignment.Center) {
                        Icon(painter = painterResource(id = R.drawable.headphones), contentDescription = null, tint = Color.White, modifier = Modifier.size(70.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Wspomnienia z tego dnia", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row {
                            Text("📍 Miejsce: ", fontWeight = FontWeight.Bold)
                            Text(concert.location)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text("📅 Data: ", fontWeight = FontWeight.Bold)
                            Text(concert.date)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                            Text("⭐ Ocena: ", fontWeight = FontWeight.Bold)
                            if (concert.rating != null && concert.rating > 0) {
                                repeat(concert.rating.toInt()) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB703), modifier = Modifier.size(18.dp)) }
                            } else { Text("Brak oceny") }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        val notesToDisplay = if (concert.notes.isNotBlank()) concert.notes else "Brak opisu wrażeń."
                        Text(
                            text = notesToDisplay,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp),
                    shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))) {
                    Column(modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())) {
                        Text("Biografia", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Gatunki: \n${concert.bandTags}", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(concert.bandBio ?: "", style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp, color = Color.DarkGray, textAlign = TextAlign.Justify)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD00000), containerColor = Color.White),
                        modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Usuń")
                    }
                    Button(
                        onClick = onEdit, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Edytuj", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// EKRAN DODAWANIA / EDYCJI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditConcertScreen(viewModel: ConcertViewModel, concertToEdit: ConcertEntity?, onNavigateBack: () -> Unit) {
    var bandName by remember { mutableStateOf(concertToEdit?.bandName ?: "") }
    var location by remember { mutableStateOf(concertToEdit?.location ?: "") }
    var notes by remember { mutableStateOf(concertToEdit?.notes ?: "") }
    var rating by remember { mutableStateOf(concertToEdit?.rating?.toInt() ?: 0) }

    var dateText by remember { mutableStateOf(concertToEdit?.date ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateText = sdf.format(Date(millis))
                        errorMessage = null // Usuniecie bledu po wybraniu daty
                    }
                }) { Text("Wybierz") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    AestheticBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { CenterAlignedTopAppBar(title = { Text(if (concertToEdit == null) "Nowy wpis" else "Edytuj wpis", style = MaterialTheme.typography.titleLarge) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)) }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

                OutlinedTextField(
                    value = bandName, onValueChange = { bandName = it; errorMessage = null }, label = { Text("Artysta") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = dateText, onValueChange = {}, label = { Text("Data") }, readOnly = true,
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }, shape = RoundedCornerShape(16.dp), enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledContainerColor = Color.White, disabledBorderColor = Color.Gray)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = location, onValueChange = { location = it; errorMessage = null }, label = { Text("Miejsce") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text("Ocena (opcjonalna)", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge, fontSize = 18.sp)
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Default.Star, contentDescription = null,
                            tint = if (i <= rating) Color(0xFFFFBD21) else Color(0xFFFFFFFF),
                            modifier = Modifier.size(40.dp).clickable { rating = if (rating == i) 0 else i }.padding(4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = notes, onValueChange = { notes = it }, label = { Text("Wspomnienia z koncertu...") },
                    modifier = Modifier.fillMaxWidth().height(150.dp), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5252)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (bandName.isBlank()) {
                            errorMessage = "Nazwa artysty jest wymagana!"
                        } else if (dateText.isBlank()) {
                            errorMessage = "Data jest wymagana!"
                        } else if (location.isBlank()) {
                            errorMessage = "Miejsce jest wymagane!"
                        } else {
                            viewModel.saveConcert(
                                id = concertToEdit?.id ?: 0, bandName = bandName, date = dateText,
                                location = location, notes = notes, rating = if (rating > 0) rating.toFloat() else null
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Zapisz w pamiętniku", style = MaterialTheme.typography.titleLarge, color = Color.White, fontSize = 18.sp) }
            }
        }
    }
}