package com.example.echoes.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Modele dla Last.fm (biografia i tagi)
data class LastFmResponse(val artist: ArtistDto?)
data class ArtistDto(val name: String?, val bio: BioDto?, val tags: TagsDto?)
data class BioDto(val summary: String?)
data class TagsDto(val tag: List<TagDto>?)
data class TagDto(val name: String?)

// Modele dla Deezer API (zdjecia)
data class DeezerResponse(val data: List<DeezerArtist>?)
data class DeezerArtist(val picture_xl: String?)

interface LastFmApiService {
    @GET("2.0/?method=artist.getinfo&format=json")
    suspend fun getArtistInfo(@Query("artist") artistName: String, @Query("api_key") apiKey: String): LastFmResponse
}

interface DeezerApiService {
    @GET("search/artist")
    suspend fun searchArtist(@Query("q") artistName: String): DeezerResponse
}

// Obiekt zarzadzajacy polaczeniami do obu API
object RetrofitInstance {
    val api: LastFmApiService by lazy {
        Retrofit.Builder().baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(LastFmApiService::class.java)
    }

    val deezerApi: DeezerApiService by lazy {
        Retrofit.Builder().baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(DeezerApiService::class.java)
    }
}