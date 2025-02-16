package com.agalvanmartin.pokedexapp.data.model

import com.agalvanmartin.pokedexapp.data.model.Pokemon

// Modelo para mapear la respuesta JSON de la PokeAPI
data class PokemonResponse(
    val results: List<Pokemon> // Lista de Pokémon obtenida de la API
)
