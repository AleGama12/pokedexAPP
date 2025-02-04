package com.agalvanmartin.pokedexapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agalvanmartin.pokedexapp.data.repositories.ApiClient
import com.agalvanmartin.pokedexapp.data.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> get() = _pokemonList

    init {
        loadPokemon()
    }

    private fun loadPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiClient.getPokeApiService()
                val response = apiService.getPokemonList()
                _pokemonList.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
