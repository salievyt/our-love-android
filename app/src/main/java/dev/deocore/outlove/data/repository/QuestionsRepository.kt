package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий вопросов для пар.
 *
 * Отвечает за:
 * - Получение ежедневного вопроса (один на день для обоих партнёров)
 * - Генерацию нового вопроса
 * - Отправку ответов на вопросы
 * - Получение ответов на конкретный вопрос
 * - Получение истории всех вопросов с пагинацией
 */
@Singleton
class QuestionsRepository @Inject constructor(
    private val api: ApiService
) {

    /** Получение вопроса на сегодня. */
    suspend fun getTodayQuestion(): Result<QuestionDto> {
        return try {
            val response = api.getTodayQuestion()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Нет вопроса на сегодня"))
        } catch (e: Exception) { Result.failure(e) }
    }

    /** Получение списка всех вопросов с пагинацией. */
    suspend fun getQuestions(page: Int = 1): Result<PaginatedResponse<QuestionDto>> {
        return try {
            val response = api.getQuestions(page)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки вопросов"))
        } catch (e: Exception) { Result.failure(e) }
    }

    /** Генерация нового вопроса на сегодня. */
    suspend fun generateQuestion(): Result<QuestionDto> {
        return try {
            val response = api.generateQuestion()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Вопрос на сегодня уже существует"))
        } catch (e: Exception) { Result.failure(e) }
    }

    /** Отправка ответа на вопрос. */
    suspend fun answerQuestion(questionId: String, text: String): Result<AnswerDto> {
        return try {
            val response = api.answerQuestion(questionId, CreateAnswerRequest(text))
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка отправки ответа"))
        } catch (e: Exception) { Result.failure(e) }
    }

    /** Получение всех ответов на конкретный вопрос. */
    suspend fun getAnswers(questionId: String): Result<List<AnswerDto>> {
        return try {
            val response = api.getAnswers(questionId)
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки ответов"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
