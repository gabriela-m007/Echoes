package com.example.echoes.data

import kotlinx.coroutines.flow.Flow

// Posrednik miedzy baza danych a interfejsem uzytkownika
class ConcertRepository(private val concertDao: ConcertDao) {
    val allConcerts: Flow<List<ConcertEntity>> = concertDao.getAllConcerts()
    suspend fun insert(concert: ConcertEntity) = concertDao.insertConcert(concert)
    suspend fun update(concert: ConcertEntity) = concertDao.updateConcert(concert)
    suspend fun delete(concert: ConcertEntity) = concertDao.deleteConcert(concert)
}