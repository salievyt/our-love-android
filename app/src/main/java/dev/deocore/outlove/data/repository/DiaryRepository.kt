package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getTodayEntry(): Result<DiaryEntryDto> {
        return try {
            val response = api.getTodayDiaryEntry()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Нет записи на сегодня"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getEntries(page: Int = 1): Result<PaginatedResponse<DiaryEntryDto>> {
        return try {
            val response = api.getDiaryEntries(page)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки записей"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun createEntry(date: String, note: String, mood: Int? = null): Result<DiaryEntryDto> {
        return try {
            val response = api.createDiaryEntry(CreateDiaryEntryRequest(date, note, mood))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка создания записи"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
