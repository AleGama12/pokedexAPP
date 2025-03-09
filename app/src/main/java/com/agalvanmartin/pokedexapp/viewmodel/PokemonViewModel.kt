package com.agalvanmartin.pokedexapp.viewmodel

import com.agalvanmartin.pokedexapp.viewmodel.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class PokemonState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Pokemon(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val abilities: String = ""
)

class PokemonViewModel : BaseViewModel<PokemonState>() {
    private val db = FirebaseFirestore.getInstance()

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        launchWithState {
            try {
                val result = db.collection("pokemons").get().await()
                val pokemons = result.documents.mapNotNull { it.toObject(Pokemon::class.java) }
                    .filter { it.name.isNotEmpty() }
                    .sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
                PokemonState(pokemons = pokemons)
            } catch (e: Exception) {
                PokemonState(error = e.message)
            }
        }
    }

    suspend fun addPokemon(pokemon: Pokemon): Result<Unit> {
        return try {
            db.collection("pokemons").add(pokemon).await()
            loadPokemons()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePokemon(pokemon: Pokemon): Result<Unit> {
        return try {
            val documents = db.collection("pokemons")
                .whereEqualTo("id", pokemon.id)
                .get()
                .await()

            for (document in documents) {
                db.collection("pokemons").document(document.id)
                    .update(
                        mapOf(
                            "name" to pokemon.name,
                            "type" to pokemon.type,
                            "abilities" to pokemon.abilities
                        )
                    )
                    .await()
            }
            loadPokemons()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePokemon(pokemon: Pokemon): Result<Unit> {
        return try {
            val documents = db.collection("pokemons")
                .whereEqualTo("id", pokemon.id)
                .get()
                .await()

            for (document in documents) {
                db.collection("pokemons").document(document.id).delete().await()
            }
            loadPokemons()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 