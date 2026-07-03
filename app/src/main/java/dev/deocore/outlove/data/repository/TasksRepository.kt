package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.DailyTaskDto
import dev.deocore.outlove.data.model.PaginatedResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий заданий для пар.
 *
 * Отвечает за:
 * - Получение задания на сегодня (одно на день)
 * - Генерацию нового задания (автоматически, один раз в день)
 * - Отметку задания как выполненного
 * - Получение истории всех заданий с пагинацией
 */
@Singleton
class TasksRepository @Inject constructor(
    private val api: ApiService
) {

    /** Получение задания на сегодня. */
    suspend fun getTodayTask(): Result<DailyTaskDto> {
        return try {
            val response = api.getTodayTask()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки задания"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Получение списка всех заданий с пагинацией. */
    suspend fun getTasks(page: Int = 1): Result<PaginatedResponse<DailyTaskDto>> {
        return try {
            val response = api.getTasks(page)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки заданий"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Генерация нового задания на сегодня. */
    suspend fun generateTask(): Result<DailyTaskDto> {
        return try {
            val response = api.generateTask()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Задание на сегодня уже существует"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Отметить задание как выполненное. */
    suspend fun completeTask(id: String): Result<DailyTaskDto> {
        return try {
            val response = api.completeTask(id)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
