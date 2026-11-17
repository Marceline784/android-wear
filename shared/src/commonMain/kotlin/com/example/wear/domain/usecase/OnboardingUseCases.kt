package com.example.wear.domain.usecase

import com.example.wear.domain.repository.OnboardingRepository

/** Зберігає результат проходження онбордингу. */
class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setCompleted(true)
}

/** Перевіряє, чи користувач уже пройшов онбординг. */
class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isCompleted()
}

/** Скидає результат онбордингу (щоб пройти його ще раз). */
class ResetOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setCompleted(false)
}
