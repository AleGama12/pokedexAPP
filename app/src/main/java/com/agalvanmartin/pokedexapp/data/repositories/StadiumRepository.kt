package com.agalvanmartin.pokedexapp.data.repositories

import com.agalvanmartin.pokedexapp.ui.screen.Stadium
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StadiumRepository {

    private val db = FirebaseFirestore.getInstance()

    // Función para agregar un Estadio
    suspend fun addStadium(stadium: Stadium) {
        db.collection("estadios")
            .document(stadium.id.toString())  // Usamos el ID del Estadio como el documento único
            .set(stadium)
            .await()  // Espera a que se complete la operación
    }

    // Función para eliminar un Estadio
    suspend fun deleteStadium(stadiumId: String) {
        db.collection("estadios")
            .document(stadiumId)  // Usamos el ID del Estadio para identificarlo
            .delete()
            .await()  // Espera a que se complete la operación
    }

    // Función para modificar un Estadio
    suspend fun updateStadium(stadium: Stadium) {
        db.collection("estadios")
            .document(stadium.id.toString())  // Usamos el ID del Estadio para actualizarlo
            .set(stadium)  // Reemplaza los datos existentes del Estadio
            .await()  // Espera a que se complete la operación
    }

    // Función para obtener todos los Estadios
    suspend fun getAllStadiums(): List<Stadium> {
        val stadiums = mutableListOf<Stadium>()
        val snapshot = db.collection("estadios").get().await()

        for (document in snapshot) {
            val stadium = document.toObject(Stadium::class.java)
            stadiums.add(stadium)
        }

        return stadiums
    }
}
