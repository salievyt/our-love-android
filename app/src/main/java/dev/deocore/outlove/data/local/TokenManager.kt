package dev.deocore.outlove.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * DataStore для хранения аутентификационных данных.
 * Имя файла: "auth_prefs"
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

/**
 * Менеджер хранения и управления JWT-токенами (access и refresh).
 *
 * Использует Android DataStore Preferences для персистентного хранения.
 * Поддерживает как suspend-методы (для coroutine-контекста), так и
 * синхронные методы через runBlocking (для UI-потока).
 *
 * Токены:
 * - access_token — короткий-lived токен для авторизации запросов (Bearer).
 * - refresh_token — длинный-lived токен для обновления access_token.
 */
class TokenManager(private val context: Context) {

    companion object {
        // Ключи для хранения токенов в DataStore
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    // Flow для реактивного отслеживания access-токена
    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN_KEY]
    }

    // Flow для реактивного отслеживания refresh-токена
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN_KEY]
    }

    /**
     * Сохраняет оба токена (access и refresh) в DataStore.
     * Вызывается после успешной авторизации или обновления токенов.
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    /**
     * Асинхронно получает access-токен.
     * Возвращает null, если токен не сохранён.
     */
    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    /**
     * Асинхронно получает refresh-токен.
     * Возвращает null, если токен не сохранён.
     */
    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    /**
     * Полностью очищает сохранённые токены (выход из системы).
     */
    suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    /**
     * Проверяет, авторизован ли пользователь.
     * Синхронный метод (использует runBlocking), подходит для вызова из UI.
     */
    fun isLoggedIn(): Boolean {
        return runBlocking {
            getAccessToken() != null
        }
    }
}
