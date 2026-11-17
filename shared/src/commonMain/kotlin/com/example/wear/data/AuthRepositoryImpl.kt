package com.example.wear.data

import com.example.wear.core.security.PasswordHasher
import com.example.wear.domain.model.AuthError
import com.example.wear.domain.model.AuthResult
import com.example.wear.domain.repository.AuthRepository
import com.russhwolf.settings.Settings

/**
 * Локальні облікові записи в multiplatform-settings.
 * Для кожного користувача зберігаються: відображуване ім'я, сіль і хеш пароля (пароль не зберігається).
 */
class SettingsAuthRepository(
    private val settings: Settings,
    private val hasher: PasswordHasher
) : AuthRepository {

    override fun register(username: String, password: String): AuthResult {
        val name = username.trim()
        val id = idOf(name)
        if (settings.hasKey(saltKey(id))) return AuthResult.Failure(AuthError.UserAlreadyExists)

        val salt = hasher.newSalt()
        settings.putString(nameKey(id), name)
        settings.putString(saltKey(id), salt)
        settings.putString(hashKey(id), hasher.hash(password, salt))
        // Автоматичного входу немає: після реєстрації користувач входить окремо
        return AuthResult.Success(name)
    }

    override fun login(username: String, password: String): AuthResult {
        val id = idOf(username)
        val salt = settings.getStringOrNull(saltKey(id)) ?: return AuthResult.Failure(AuthError.InvalidCredentials)
        val expected = settings.getStringOrNull(hashKey(id)) ?: return AuthResult.Failure(AuthError.InvalidCredentials)
        if (hasher.hash(password, salt) != expected) return AuthResult.Failure(AuthError.InvalidCredentials)

        settings.putString(KEY_CURRENT_USER, id)
        return AuthResult.Success(settings.getString(nameKey(id), username.trim()))
    }

    override fun logout() {
        settings.remove(KEY_CURRENT_USER)
    }

    override fun currentUser(): String? {
        val id = settings.getStringOrNull(KEY_CURRENT_USER) ?: return null
        return settings.getStringOrNull(nameKey(id))
    }

    /** Імена користувачів не залежать від регістру. */
    private fun idOf(username: String) = username.trim().lowercase()

    private fun nameKey(id: String) = "auth.user.$id.name"
    private fun saltKey(id: String) = "auth.user.$id.salt"
    private fun hashKey(id: String) = "auth.user.$id.hash"

    private companion object {
        const val KEY_CURRENT_USER = "auth.current_user"
    }
}
