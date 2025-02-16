package com.agalvanmartin.pokedexapp.data.repositories

import com.agalvanmartin.pokedexapp.data.model.PokemonDetail
import com.agalvanmartin.pokedexapp.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetail

    @GET("pokemon?limit=30&offset=0")
    suspend fun getPokemonList(): PokemonResponse
}
