package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodsRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getTodayMood(): Result<MoodEntryDto> {
        return try {
            val response = api.getTodayMood()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Нет настроения на сегодня"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun createMood(mood: Int, date: String, note: String = ""): Result<MoodEntryDto> {
        return try {
            val response = api.createMood(CreateMoodRequest(mood, note, date))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка сохранения"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun updateTodayMood(mood: Int, date: String, note: String = ""): Result<MoodEntryDto> {
        return try {
            val response = api.updateTodayMood(CreateMoodRequest(mood, note, date))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка обновления"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getMoods(page: Int = 1): Result<PaginatedResponse<MoodEntryDto>> {
        return try {
            val response = api.getMoods(page)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getMoodStats(): Result<MoodStatsDto> {
        return try {
            val response = api.getMoodStats()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка статистики"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
