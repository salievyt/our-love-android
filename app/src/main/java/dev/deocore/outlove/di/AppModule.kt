package dev.deocore.outlove.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.deocore.outlove.BuildConfig
import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.api.AuthInterceptor
import dev.deocore.outlove.data.local.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Модуль внедрения зависимостей (DI) через Hilt.
 *
 * Предоставляет синглтон-экземпляры для:
 * - TokenManager (хранение JWT-токенов)
 * - AuthInterceptor (добавление Authorization заголовка к запросам)
 * - OkHttpClient (HTTP-клиент с таймаутами и логированием)
 * - Retrofit (REST-клиент для API)
 * - ApiService (интерфейс API-методов)
 *
 * JSON-парсер настроен на игнорирование неизвестных полей и мягкую десериализацию.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Глобальный JSON-парсер для kotlinx.serialization.
     * ignoreUnknownKeys — не падать при лишних полях от сервера.
     * coerceInputValues — приводить null к значениям по умолчанию.
     * isLenient — более терпимая парсинг-стратегия.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * Предоставляет TokenManager для хранения и чтения JWT-токенов.
     * Использует DataStore Preferences для персистентного хранения.
     */
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    /**
     * Предоставляет AuthInterceptor для автоматического добавления
     * Bearer-токена к каждому HTTP-запросу.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    /**
     * Предоставляет настроенный OkHttpClient.
     *
     * Включает:
     * - AuthInterceptor для авторизации
     * - HttpLoggingInterceptor (BODY-лог в debug, выключен в release)
     * - Таймауты: подключение/чтение — 30с, запись — 60с
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        // Логирование HTTP-запросов/ответов только в debug-сборке
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)     // Авторизация: добавляет Bearer-токен
            .addInterceptor(logging)             // Логирование (только debug)
            .connectTimeout(30, TimeUnit.SECONDS) // Таймаут на установление соединения
            .readTimeout(30, TimeUnit.SECONDS)    // Таймаут на чтение ответа
            .writeTimeout(60, TimeUnit.SECONDS)   // Таймаут на отправку запроса
            .build()
    }

    /**
     * Предоставляет Retrofit-клиент с настроенным JSON-конвертером
     * для kotlinx.serialization.
     *
     * Базовый URL берётся из BuildConfig.BASE_URL (указан в build.gradle.kts).
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /**
     * Предоставляет экземпляр ApiService — интерфейс для вызовов REST API.
     * Создается на основе настроенного Retrofit-клиента.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
