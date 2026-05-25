package com.example.echoes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela bazy danych - laczy wpisy z danymi z internetu
@Entity(tableName = "concerts")
data class ConcertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bandName: String,
    val date: String,
    val location: String,
    val notes: String,
    val rating: Float?,
    val bandBio: String?,
    val bandTags: String?,
    val bandImageUrl: String?
)