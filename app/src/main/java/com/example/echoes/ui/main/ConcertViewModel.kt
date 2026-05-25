package com.example.echoes.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.echoes.data.ConcertEntity
import com.example.echoes.data.ConcertRepository
import com.example.echoes.network.RetrofitInstance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Pobiera dane, przetwarza je i zapisuje w bazie
class ConcertViewModel(private val repository: ConcertRepository) : ViewModel() {

    val allConcerts: StateFlow<List<ConcertEntity>> = repository.allConcerts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveConcert(id: Int = 0, bandName: String, date: String, location: String, notes: String, rating: Float?) {
        viewModelScope.launch {
            var bio: String? = null
            var tags: String? = null
            var imageUrl: String? = null

            // 1. Proba pobrania zdjecia artysty z API Deezer (najwieksze)
            try {
                val deezerResponse = RetrofitInstance.deezerApi.searchArtist(bandName)
                imageUrl = deezerResponse.data?.firstOrNull()?.picture_xl
            } catch (e: Exception) { }

            // 2. Proba pobrania tekstow (bio, tagi) z API Last.fm
            try {
                val apiKey = "a8bb822999692be9103c0074403d8eb8"
                val lastFmResponse = RetrofitInstance.api.getArtistInfo(bandName, apiKey)

                val rawBio = lastFmResponse.artist?.bio?.summary
                // Jeśli API zwrocilo tekst i zawiera on link (oznacza to, ze tekst zostal uciety)
                bio = if (rawBio != null && rawBio.contains("<a href")) {
                    rawBio.substringBefore("<a href").trim() + "...\npo więcej informacji odwiedź stronę artysty na Last.fm"
                } else {
                    rawBio?.trim()
                }

                tags = lastFmResponse.artist?.tags?.tag?.joinToString(", ") { it.name ?: "" }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 3. Polaczenie wszystkiego w jeden obiekt bazy danych
            val concert = ConcertEntity(
                id = id, bandName = bandName, date = date, location = location,
                notes = notes, rating = rating,
                bandBio = bio ?: "Brak biografii artysty w bazie Last.fm.",
                bandTags = tags ?: "Brak danych",
                bandImageUrl = imageUrl
            )

            // 4. Zapis do bazy (nowy wpis = insert, stary = update)
            if (id == 0) repository.insert(concert) else repository.update(concert)
        }
    }

    fun deleteConcert(concert: ConcertEntity) {
        viewModelScope.launch { repository.delete(concert) }
    }
}

class ConcertViewModelFactory(private val repository: ConcertRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConcertViewModel::class.java)) return ConcertViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}