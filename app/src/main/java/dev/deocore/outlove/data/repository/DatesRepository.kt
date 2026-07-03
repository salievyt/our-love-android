package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatesRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getImportantDates(): Result<List<ImportantDateDto>> {
        return try {
            val response = api.getImportantDates()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки дат"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getUpcomingDates(): Result<List<ImportantDateDto>> {
        return try {
            val response = api.getUpcomingDates()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun createDate(title: String, date: String, emoji: String, isAnnually: Boolean): Result<ImportantDateDto> {
        return try {
            val response = api.createImportantDate(CreateDateRequest(title, date, emoji, isAnnually))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка создания"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deleteDate(id: String): Result<Unit> {
        return try {
            val response = api.deleteImportantDate(id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка удаления"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
