package com.agalvanmartin.pokedexapp.data.repositories

import com.agalvanmartin.pokedexapp.data.model.Pokemon
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class PokemonRepository {

    private val db = FirebaseFirestore.getInstance()

    // Función para agregar un Pokémon
    suspend fun addPokemon(pokemon: com.agalvanmartin.pokedexapp.ui.screen.Pokemon) {
        db.collection("pokemons")
            .document(pokemon.id.toString())  // Usamos el ID del Pokémon como el documento único
            .set(pokemon)
            .await()  // Espera a que se complete la operación
    }


    // Función para eliminar un Pokémon
    suspend fun deletePokemon(pokemonId: String) {
        db.collection("pokemons")
            .document(pokemonId)  // Usamos el ID del Pokémon para identificarlo
            .delete()
            .await()  // Espera a que se complete la operación
    }
}
