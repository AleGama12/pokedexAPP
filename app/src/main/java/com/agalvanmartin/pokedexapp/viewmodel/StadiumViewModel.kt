package com.agalvanmartin.pokedexapp.viewmodel

import com.agalvanmartin.pokedexapp.viewmodel.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class StadiumState(
    val stadiums: List<Stadium> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Stadium(
    val id: String = "",
    val name: String = "",
    val capacity: String = "",
    val insignia: String = ""
)

class StadiumViewModel : BaseViewModel<StadiumState>() {
    private val db = FirebaseFirestore.getInstance()

    init {
        loadStadiums()
    }

    fun loadStadiums() {
        launchWithState {
            try {
                val result = db.collection("stadiums").get().await()
                val stadiums = result.documents.mapNotNull { it.toObject(Stadium::class.java) }
                    .filter { it.name.isNotEmpty() }
                    .sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
                StadiumState(stadiums = stadiums)
            } catch (e: Exception) {
                StadiumState(error = e.message)
            }
        }
    }

    suspend fun addStadium(stadium: Stadium): Result<Unit> {
        return try {
            db.collection("stadiums").add(stadium).await()
            loadStadiums()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStadium(stadium: Stadium): Result<Unit> {
        return try {
            val documents = db.collection("stadiums")
                .whereEqualTo("id", stadium.id)
                .get()
                .await()

            for (document in documents) {
                db.collection("stadiums").document(document.id)
                    .update(
                        mapOf(
                            "name" to stadium.name,
                            "capacity" to stadium.capacity,
                            "insignia" to stadium.insignia
                        )
                    )
                    .await()
            }
            loadStadiums()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteStadium(stadium: Stadium): Result<Unit> {
        return try {
            val documents = db.collection("stadiums")
                .whereEqualTo("id", stadium.id)
                .get()
                .await()

            for (document in documents) {
                db.collection("stadiums").document(document.id).delete().await()
            }
            loadStadiums()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 