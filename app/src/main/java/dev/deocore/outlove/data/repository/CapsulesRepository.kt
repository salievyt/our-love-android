package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapsulesRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getCapsules(): Result<List<TimeCapsuleDto>> {
        return try {
            val response = api.getCapsules()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки капсул"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getActiveCapsules(): Result<List<TimeCapsuleDto>> {
        return try {
            val response = api.getActiveCapsules()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getOpenedCapsules(): Result<List<TimeCapsuleDto>> {
        return try {
            val response = api.getOpenedCapsules()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun createCapsule(title: String, message: String, openDate: String): Result<TimeCapsuleDto> {
        return try {
            val response = api.createCapsule(CreateCapsuleRequest(title, message, openDate))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка создания"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun openCapsule(id: String): Result<TimeCapsuleDto> {
        return try {
            val response = api.openCapsule(id)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка открытия"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
