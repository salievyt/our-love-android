package dev.deocore.outlove.data.repository

import android.content.Context
import android.net.Uri
import dev.deocore.outlove.data.api.ApiService
import dev.deocore.outlove.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий работы с фотоальбомами.
 *
 * Отвечает за:
 * - Загрузку фотографий с пагинацией и фильтрацией по году
 * - Загрузку новых фотографий (multipart)
 * - Удаление фотографий
 * - Получение и создание коллажей
 */
@Singleton
class AlbumRepository @Inject constructor(
    private val api: ApiService
) {

    /**
     * Загрузка фотографий с пагинацией.
     * @param yearTag фильтр по году (необязательно)
     * @param page номер страницы
     */
    suspend fun getPhotos(yearTag: Int? = null, page: Int = 1): Result<PaginatedResponse<PhotoDto>> {
        return try {
            val response = api.getPhotos(yearTag, page)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки фото"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Загрузка новой фотографии на сервер.
     * Читает файл по Uri, конвертирует в байты и отправляет как multipart-часть.
     */
    suspend fun uploadPhoto(context: Context, uri: Uri, caption: String): Result<PhotoDto> {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
            val bytes = inputStream.readBytes()
            val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", "photo.jpg", requestBody)
            val captionBody = caption.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = api.uploadPhoto(imagePart, captionBody)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Удаление фотографии по ID. */
    suspend fun deletePhoto(id: String): Result<Unit> {
        return try {
            val response = api.deletePhoto(id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка удаления"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Получение всех коллажей. */
    suspend fun getCollages(): Result<List<CollageDto>> {
        return try {
            val response = api.getCollages()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка загрузки коллажей"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
