package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getPlaces(): Result<List<PlaceDto>> {
        return try {
            val response = api.getPlaces()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки мест"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun createPlace(title: String, description: String, emoji: String, lat: Double, lng: Double): Result<PlaceDto> {
        return try {
            val response = api.createPlace(CreatePlaceRequest(title, description, emoji, lat, lng))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка создания места"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deletePlace(id: String): Result<Unit> {
        return try {
            val response = api.deletePlace(id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка удаления"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
