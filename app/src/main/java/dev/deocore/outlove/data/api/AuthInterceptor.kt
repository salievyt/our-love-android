package dev.deocore.outlove.data.api

import dev.deocore.outlove.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * HTTP-интерцептор для автоматической авторизации запросов.
 *
 * Добавляет заголовок "Authorization: Bearer <token>" ко всем исходящим
 * HTTP-запросам, если access-токен сохранён. Если токена нет — запрос
 * проходит без авторизации (для публичных эндпоинтов, таких как login/register).
 *
 * Использует runBlocking для синхронного получения токена из coroutine-метода,
 * т.к. OkHttp работает в синхронном потоке.
 */
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Исходный HTTP-запрос от Retrofit
        val originalRequest = chain.request()

        // Получаем access-токен (синхронно через runBlocking)
        val token = runBlocking {
            tokenManager.getAccessToken()
        }

        // Если токена нет — пропускаем запрос без авторизации
        // (нужно для публичных эндпоинтов: login, register, refresh)
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        // Создаём авторизованную копию запроса с Bearer-токеном
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
