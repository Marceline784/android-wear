package com.example.wear.domain.usecase

import com.example.wear.domain.model.AuthResult
import com.example.wear.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    operator fun invoke(username: String, password: String): AuthResult = repository.login(username, password)
}

/** Реєструє користувача. Вхід виконується окремо (LoginUseCase). */
class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(username: String, password: String): AuthResult = repository.register(username, password)
}

class LogoutUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.logout()
}

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): String? = repository.currentUser()
}
