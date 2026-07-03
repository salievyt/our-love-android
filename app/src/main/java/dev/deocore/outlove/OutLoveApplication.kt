package dev.deocore.outlove

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Приложение OutLove — мобильное приложение для пар.
 * Отслеживает дни вместе, управляет общими задачами, дневником,
 * вопросами для пар, настроением, местами, капсулами времени и важными датами.
 *
 * Использует Hilt для внедрения зависимостей (DI).
 */
@HiltAndroidApp
class OutLoveApplication : Application()
