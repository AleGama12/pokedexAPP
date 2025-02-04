package com.agalvanmartin.pokedexapp.data.model

data class Pokemon(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.split("/").filter { it.isNotEmpty() }.last().toInt()
}

