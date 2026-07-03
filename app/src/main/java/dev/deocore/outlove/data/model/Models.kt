package dev.deocore.outlove.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================================================
// AUTH — Аутентификация и регистрация
// ============================================================================

/** Запрос на вход: логин и пароль */
@Serializable
data class LoginRequest(val username: String, val password: String)

/** Запрос на регистрацию пользователя */
@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerialName("first_name") val firstName: String = "",
    @SerialName("last_name") val lastName: String = ""
)

/** Запрос на обновление access-токена через refresh-токен */
@Serializable
data class RefreshRequest(@SerialName("refresh") val refreshToken: String)

/** Ответ после успешной аутентификации: пара JWT-токенов */
@Serializable
data class AuthResponse(
    val access: String,    // Access-токен (короткоживущий)
    val refresh: String    // Refresh-токен (долгоживущий)
)

// ============================================================================
// USER — Данные пользователя
// ============================================================================

/** Профиль пользователя, полученный с сервера */
@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    @SerialName("first_name") val firstName: String = "",
    @SerialName("last_name") val lastName: String = "",
    val avatar: String? = null,
    val phone: String = ""
)

// ============================================================================
// RELATIONSHIP — Данные о паре
// ============================================================================

/** Информация о паре: оба партнёра, дата начала отношений, количество дней вместе */
@Serializable
data class RelationshipDto(
    val id: String,
    val partner1: UserDto,
    val partner2: UserDto? = null,
    @SerialName("partner1_name") val partner1Name: String,
    @SerialName("partner2_name") val partner2Name: String,
    @SerialName("partner1_emoji") val partner1Emoji: String = "💙",
    @SerialName("partner2_emoji") val partner2Emoji: String = "💖",
    @SerialName("relationship_start_date") val relationshipStartDate: String,
    @SerialName("days_together") val daysTogether: Int = 0
)

// ============================================================================
// INVITE — Коды приглашения
// ============================================================================

/** Код приглашения для регистрации второго партнёра */
@Serializable
data class InviteCodeDto(
    val id: String,
    val code: String,
    @SerialName("is_used") val isUsed: Boolean,
    @SerialName("expires_at") val expiresAt: String
)

// ============================================================================
// ALBUMS — Фотоальбомы и коллажи
// ============================================================================

/** Отдельная фотография в альбоме */
@Serializable
data class PhotoDto(
    val id: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val caption: String = "",
    @SerialName("created_at") val createdAt: String,
    @SerialName("year_tag") val yearTag: Int  // Год привязки фото
)

/** Коллаж — подборка фотографий с заголовком */
@Serializable
data class CollageDto(
    val id: String,
    val title: String,
    val image: String? = null,
    @SerialName("photo_count") val photoCount: Int = 0,
    @SerialName("created_at") val createdAt: String
)

/** Запрос на создание коллажа */
@Serializable
data class CreateCollageRequest(
    val title: String,
    val photos: List<String> = emptyList()
)

// ============================================================================
// TASKS — Задания для пар
// ============================================================================

/** Задание на день: описание, эмодзи, статус выполнения, дата назначения */
@Serializable
data class DailyTaskDto(
    val id: String,
    val title: String,
    val description: String = "",
    val emoji: String = "🎯",
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("completed_at") val completedAt: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("date_assigned") val dateAssigned: String
)

// ============================================================================
// QUESTIONS — Вопросы для пар
// ============================================================================

/** Ежедневный вопрос для пары с ответами обоих партнёров */
@Serializable
data class QuestionDto(
    val id: String,
    val question: String,
    @SerialName("date_assigned") val dateAssigned: String,
    val answers: List<AnswerDto> = emptyList(),
    @SerialName("both_answered") val bothAnswered: Boolean = false  // Ответили ли оба
)

/** Ответ на вопрос от одного из партнёров */
@Serializable
data class AnswerDto(
    val id: String,
    val user: String,
    val text: String,
    @SerialName("created_at") val createdAt: String
)

/** Запрос на отправку ответа */
@Serializable
data class CreateAnswerRequest(val text: String)

// ============================================================================
// DIARY — Дневник пары
// ============================================================================

/** Запись в дневнике: дата, текст, настроение, фото */
@Serializable
data class DiaryEntryDto(
    val id: String,
    val date: String,
    val note: String = "",
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("mood_partner1") val moodPartner1: Int? = null,  // Настроение партнёра 1 (1-5)
    @SerialName("mood_partner2") val moodPartner2: Int? = null,  // Настроение партнёра 2 (1-5)
    @SerialName("created_at") val createdAt: String
)

/** Запрос на создание записи в дневнике */
@Serializable
data class CreateDiaryEntryRequest(
    val date: String,
    val note: String = "",
    @SerialName("mood_partner1") val moodPartner1: Int? = null
)

// ============================================================================
// MOODS — Отслеживание настроения
// ============================================================================

/** Запись настроения: уровень (1-5), заметка, дата */
@Serializable
data class MoodEntryDto(
    val id: String,
    val user: String? = null,
    val mood: Int,              // Уровень настроения 1-5
    val note: String = "",
    val date: String,
    @SerialName("created_at") val createdAt: String? = null
)

/** Запрос на создание/обновление записи настроения */
@Serializable
data class CreateMoodRequest(
    val mood: Int,
    val note: String = "",
    val date: String
)

/** Статистика настроения: общее кол-во, среднее, кол-во хороших/плохих дней */
@Serializable
data class MoodStatsDto(
    val total: Int = 0,
    val average: Double = 0.0,
    @SerialName("good_days") val goodDays: Int = 0,
    @SerialName("bad_days") val badDays: Int = 0,
    @SerialName("by_mood") val byMood: Map<String, Int> = emptyMap()
)

// ============================================================================
// PLACES — Карта значимых мест
// ============================================================================

/** Значимое место пары: название, описание, координаты, эмодзи */
@Serializable
data class PlaceDto(
    val id: String,
    val title: String,
    val description: String = "",
    val emoji: String = "❤️",
    val latitude: Double,
    val longitude: Double,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("created_at") val createdAt: String
)

/** Запрос на создание нового значимого места */
@Serializable
data class CreatePlaceRequest(
    val title: String,
    val description: String = "",
    val emoji: String = "❤️",
    val latitude: Double,
    val longitude: Double
)

// ============================================================================
// TIME CAPSULES — Капсулы времени
// ============================================================================

/** Капсула времени: послание, которое открывается в определённую дату */
@Serializable
data class TimeCapsuleDto(
    val id: String,
    val title: String,
    val message: String,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("open_date") val openDate: String,
    @SerialName("is_opened") val isOpened: Boolean = false,
    @SerialName("is_ready_to_open") val isReadyToOpen: Boolean = false,
    @SerialName("days_until_open") val daysUntilOpen: Int = 0,
    @SerialName("created_at") val createdAt: String
)

/** Запрос на создание капсулы времени */
@Serializable
data class CreateCapsuleRequest(
    val title: String,
    val message: String,
    @SerialName("open_date") val openDate: String
)

// ============================================================================
// IMPORTANT DATES — Важные даты
// ============================================================================

/** Важная дата: годовщины, праздники пары */
@Serializable
data class ImportantDateDto(
    val id: String,
    val title: String,
    val date: String,
    val emoji: String = "❤️",
    @SerialName("is_annually") val isAnnually: Boolean = true,  // Повторяется ежегодно
    @SerialName("days_until") val daysUntil: Int = 0  // Дней до события
)

/** Запрос на создание важной даты */
@Serializable
data class CreateDateRequest(
    val title: String,
    val date: String,
    val emoji: String = "❤️",
    @SerialName("is_annually") val isAnnually: Boolean = true
)

// ============================================================================
// COMMON — Общие типы
// ============================================================================

/** Пагинированный ответ от сервера: метаданные + список результатов */
@Serializable
data class PaginatedResponse<T>(
    val count: Int,           // Общее количество элементов
    val next: String? = null, // URL следующей страницы (null если конец)
    val previous: String? = null, // URL предыдущей страницы
    val results: List<T> = emptyList()  // Список данных
)
