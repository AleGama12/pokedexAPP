package com.agalvanmartin.pokedexapp.data.repositories

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // URL base de la PokeAPI
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // Creamos la instancia de Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    // Método para obtener el servicio de API
    val apiService: PokeApiService by lazy {
        retrofit.create(PokeApiService::class.java)
    }
}
