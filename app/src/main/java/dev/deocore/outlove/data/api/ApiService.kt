package dev.deocore.outlove.data.api

import dev.deocore.outlove.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * REST API интерфейс для взаимодействия с сервером Love API (Django REST Framework).
 *
 * Base URL: https://dev.phantom-ink.online/api/v1/
 *
 * Авторизация: JWT (Bearer token), добавляется автоматически через AuthInterceptor.
 * Все ответы пагинированы (paginated) с полями: count, next, previous, results.
 *
 * Эндпоинты разделены по доменам: Auth, Albums, Tasks, Questions, Diary, Moods,
 * Places, Capsules, Dates.
 */
interface ApiService {

    // ========================================================================
    // AUTH — Аутентификация
    // ========================================================================

    /** Вход в систему по логину и паролю. Возвращает пару JWT-токенов. */
    @POST("auth/login/")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    /** Регистрация нового пользователя. Принимает код приглашения (invite_code). */
    @POST("auth/register/")
    suspend fun register(@Body body: RegisterRequest): Response<UserDto>

    /** Обновление access-токена через refresh-токен. */
    @POST("auth/refresh/")
    suspend fun refreshToken(@Body body: RefreshRequest): Response<AuthResponse>

    /** Выход из системы: инвалидация refresh-токена. */
    @POST("auth/logout/")
    suspend fun logout(@Body body: RefreshRequest): Response<Unit>

    /** Получение данных текущего пользователя. */
    @GET("auth/me/")
    suspend fun getMe(): Response<UserDto>

    /** Получение профиля пользователя. */
    @GET("auth/profile/")
    suspend fun getProfile(): Response<UserDto>

    /** Обновление профиля пользователя (частичное обновление через PATCH). */
    @PATCH("auth/profile/")
    suspend fun updateProfile(@Body body: Map<String, String>): Response<UserDto>

    /** Получение информации о паре (оба партнёра, дата начала, дни вместе). */
    @GET("auth/relationship/")
    suspend fun getRelationship(): Response<RelationshipDto>

    /** Генерация кода приглашения для второго партнёра (требует JWT). */
    @POST("auth/invite/generate/")
    suspend fun generateInviteCode(): Response<InviteCodeDto>

    // ========================================================================
    // ALBUMS — Фотоальбомы
    // ========================================================================

    /**
     * Получение фотографий с пагинацией.
     * @param yearTag фильтр по году (необязательно)
     * @param page номер страницы (по умолчанию 1)
     */
    @GET("albums/")
    suspend fun getPhotos(
        @Query("year_tag") yearTag: Int? = null,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<PhotoDto>>

    /**
     * Загрузка новой фотографии в альбом (multipart/form-data).
     * @param image файл изображения
     * @param caption подпись к фото
     */
    @Multipart
    @POST("albums/")
    suspend fun uploadPhoto(
        @Part image: MultipartBody.Part,
        @Part("caption") caption: RequestBody
    ): Response<PhotoDto>

    /** Удаление фотографии по ID. */
    @DELETE("albums/{id}/")
    suspend fun deletePhoto(@Path("id") id: String): Response<Unit>

    /** Получение всех коллажей. */
    @GET("albums/collages/")
    suspend fun getCollages(): Response<List<CollageDto>>

    /** Создание нового коллажа из выбранных фотографий. */
    @POST("albums/collages/")
    suspend fun createCollage(@Body body: CreateCollageRequest): Response<CollageDto>

    // ========================================================================
    // TASKS — Задания для пар
    // ========================================================================

    /** Получение задания на сегодня (одно на день). */
    @GET("tasks/today/")
    suspend fun getTodayTask(): Response<DailyTaskDto>

    /** Получение списка всех заданий с пагинацией. */
    @GET("tasks/")
    suspend fun getTasks(@Query("page") page: Int = 1): Response<PaginatedResponse<DailyTaskDto>>

    /** Генерация нового задания на сегодня (автоматически). */
    @POST("tasks/generate/")
    suspend fun generateTask(): Response<DailyTaskDto>

    /** Отметить задание как выполненное. */
    @POST("tasks/{id}/complete/")
    suspend fun completeTask(@Path("id") id: String): Response<DailyTaskDto>

    /**
     * Загрузить фото к выполненному заданию.
     * @param id ID задания
     * @param photo файл изображения
     */
    @Multipart
    @PATCH("tasks/{id}/")
    suspend fun updateTaskPhoto(
        @Path("id") id: String,
        @Part photo: MultipartBody.Part
    ): Response<DailyTaskDto>

    // ========================================================================
    // QUESTIONS — Вопросы для пар
    // ========================================================================

    /** Получение вопроса на сегодня (один на день для обоих партнёров). */
    @GET("questions/today/")
    suspend fun getTodayQuestion(): Response<QuestionDto>

    /** Получение списка всех вопросов с пагинацией. */
    @GET("questions/")
    suspend fun getQuestions(@Query("page") page: Int = 1): Response<PaginatedResponse<QuestionDto>>

    /** Генерация нового вопроса на сегодня (автоматически). */
    @POST("questions/generate/")
    suspend fun generateQuestion(): Response<QuestionDto>

    /**
     * Отправить ответ на вопрос.
     * @param questionId ID вопроса
     * @param body текст ответа
     */
    @POST("questions/{questionId}/answers/create/")
    suspend fun answerQuestion(
        @Path("questionId") questionId: String,
        @Body body: CreateAnswerRequest
    ): Response<AnswerDto>

    /** Получение всех ответов на конкретный вопрос. */
    @GET("questions/{questionId}/answers/")
    suspend fun getAnswers(@Path("questionId") questionId: String): Response<List<AnswerDto>>

    // ========================================================================
    // DIARY — Дневник
    // ========================================================================

    /** Получение записи дневника за сегодня. */
    @GET("diary/today/")
    suspend fun getTodayDiaryEntry(): Response<DiaryEntryDto>

    /** Получение всех записей дневника с пагинацией. */
    @GET("diary/")
    suspend fun getDiaryEntries(@Query("page") page: Int = 1): Response<PaginatedResponse<DiaryEntryDto>>

    /** Создание новой записи в дневнике. */
    @POST("diary/")
    suspend fun createDiaryEntry(@Body body: CreateDiaryEntryRequest): Response<DiaryEntryDto>

    /**
     * Обновление записи дневника (текст и/или фото).
     * @param id ID записи
     * @param photo новое фото (необязательно)
     * @param note новый текст (необязательно)
     */
    @Multipart
    @PATCH("diary/{id}/")
    suspend fun updateDiaryEntry(
        @Path("id") id: String,
        @Part photo: MultipartBody.Part? = null,
        @Part("note") note: RequestBody? = null
    ): Response<DiaryEntryDto>

    // ========================================================================
    // MOODS — Настроение
    // ========================================================================

    /** Получение настроения на сегодня. */
    @GET("moods/today/")
    suspend fun getTodayMood(): Response<MoodEntryDto>

    /** Обновление настроения на сегодня. */
    @PATCH("moods/today/")
    suspend fun updateTodayMood(@Body body: CreateMoodRequest): Response<MoodEntryDto>

    /** Получение всех записей настроения с пагинацией. */
    @GET("moods/")
    suspend fun getMoods(@Query("page") page: Int = 1): Response<PaginatedResponse<MoodEntryDto>>

    /** Создание новой записи настроения. */
    @POST("moods/")
    suspend fun createMood(@Body body: CreateMoodRequest): Response<MoodEntryDto>

    /** Получение статистики настроения (среднее, кол-во хороших/плохих дней). */
    @GET("moods/stats/")
    suspend fun getMoodStats(): Response<MoodStatsDto>

    // ========================================================================
    // PLACES — Значимые места
    // ========================================================================

    /** Получение всех значимых мест пары. */
    @GET("places/")
    suspend fun getPlaces(): Response<List<PlaceDto>>

    /** Создание нового значимого места. */
    @POST("places/")
    suspend fun createPlace(@Body body: CreatePlaceRequest): Response<PlaceDto>

    /** Удаление значимого места по ID. */
    @DELETE("places/{id}/")
    suspend fun deletePlace(@Path("id") id: String): Response<Unit>

    // ========================================================================
    // CAPSULES — Капсулы времени
    // ========================================================================

    /** Получение всех капсул времени. */
    @GET("capsules/")
    suspend fun getCapsules(): Response<List<TimeCapsuleDto>>

    /** Получение активных (ещё не открытых) капсул. */
    @GET("capsules/active/")
    suspend fun getActiveCapsules(): Response<List<TimeCapsuleDto>>

    /** Получение уже открытых капсул. */
    @GET("capsules/opened/")
    suspend fun getOpenedCapsules(): Response<List<TimeCapsuleDto>>

    /** Создание новой капсулы времени с посланием. */
    @POST("capsules/")
    suspend fun createCapsule(@Body body: CreateCapsuleRequest): Response<TimeCapsuleDto>

    /** Открыть капсулу (когда дата открытия наступила). */
    @POST("capsules/{id}/open/")
    suspend fun openCapsule(@Path("id") id: String): Response<TimeCapsuleDto>

    // ========================================================================
    // DATES — Важные даты
    // ========================================================================

    /** Получение всех сохранённых важных дат. */
    @GET("dates/")
    suspend fun getImportantDates(): Response<List<ImportantDateDto>>

    /** Получение ближайших upcoming-дат (сортировка по дате). */
    @GET("dates/upcoming/")
    suspend fun getUpcomingDates(): Response<List<ImportantDateDto>>

    /** Создание новой важной даты (годовщина, праздник). */
    @POST("dates/")
    suspend fun createImportantDate(@Body body: CreateDateRequest): Response<ImportantDateDto>

    /** Удаление важной даты по ID. */
    @DELETE("dates/{id}/")
    suspend fun deleteImportantDate(@Path("id") id: String): Response<Unit>
}
