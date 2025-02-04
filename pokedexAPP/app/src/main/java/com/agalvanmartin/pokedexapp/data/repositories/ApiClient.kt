package com.agalvanmartin.pokedexapp.data.repositories

import com.agalvanmartin.pokedexapp.data.model.PokemonDetail
import com.agalvanmartin.pokedexapp.data.model.PokemonResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    fun getPokeApiService(): PokeApiService {
        return retrofit.create(PokeApiService::class.java)
    }

    suspend fun getPokemonList(): PokemonResponse {
        return getPokeApiService().getPokemonList()
    }

    suspend fun getPokemonDetails(id: Int): PokemonDetail {
        return getPokeApiService().getPokemonDetails(id)
    }
}
