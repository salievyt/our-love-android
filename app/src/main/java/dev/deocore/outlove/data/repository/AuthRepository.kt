package dev.deocore.outlove.data.repository

import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.local.TokenManager
import dev.deocore.outlove.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий аутентификации.
 *
 * Отвечает за:
 * - Вход (login) — получает JWT-токены и сохраняет их в TokenManager
 * - Регистрацию — создание нового аккаунта
 * - Выход (logout) — инвалидация токена и очистка хранилища
 * - Управление профилем и парой
 * - Генерацию кодов приглашения для второго партнёра
 *
 * Все сетевые операции оборачиваются в Result<T> для обработки ошибок на уровне UI.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    /**
     * Вход в систему по логину и паролю.
     * При успехе сохраняет JWT-токены и загружает профиль пользователя.
     * @return UserDto профиля авторизованного пользователя
     */
    suspend fun login(username: String, password: String): Result<UserDto> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                val auth = response.body()!!
                // Сохраняем оба токена для последующей авторизации запросов
                tokenManager.saveTokens(auth.access, auth.refresh)
                // Загружаем профиль текущего пользователя
                val meResponse = api.getMe()
                Result.success(meResponse.body()!!)
            } else {
                Result.failure(Exception("Ошибка входа"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Регистрация нового пользователя.
     */
    suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String = "",
        lastName: String = ""
    ): Result<UserDto> {
        return try {
            val response = api.register(RegisterRequest(username, email, password, firstName, lastName))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Выход из системы.
     * Отправляет refresh-токен на сервер для инвалидации, затем очищает локальное хранилище.
     * Ошибки сервера игнорируются — токены всё равно удаляются локально.
     */
    suspend fun logout() {
        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken != null) {
            try {
                api.logout(RefreshRequest(refreshToken))
            } catch (_: Exception) {}
        }
        tokenManager.clearTokens()
    }

    /** Загрузка профиля текущего пользователя. */
    suspend fun getProfile(): Result<UserDto> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Не удалось загрузить профиль"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Получение информации о паре (оба партнёра, дата начала, дни вместе). */
    suspend fun getRelationship(): Result<RelationshipDto> {
        return try {
            val response = api.getRelationship()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Нет пары"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Генерация кода приглашения для второго партнёра (требует авторизации). */
    suspend fun generateInviteCode(): Result<InviteCodeDto> {
        return try {
            val response = api.generateInviteCode()
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка генерации кода"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Проверка, авторизован ли пользователь (наличие access-токена). */
    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
