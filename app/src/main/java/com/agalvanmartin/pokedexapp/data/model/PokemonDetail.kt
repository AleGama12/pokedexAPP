package com.agalvanmartin.pokedexapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("types") val types: List<PokemonType>,
    @SerializedName("abilities") val abilities: List<PokemonAbility>
)

data class PokemonType(
    @SerializedName("type") val type: TypeName
)

data class TypeName(
    @SerializedName("name") val name: String
)

data class PokemonAbility(
    @SerializedName("ability") val ability: AbilityName
)

data class AbilityName(
    @SerializedName("name") val name: String
)
