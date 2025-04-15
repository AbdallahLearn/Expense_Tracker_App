package com.example.expense_tracking_project.screens.authentication.domain.usecase

import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository

class ForgotPasswordUseCase (private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String) {
        authRepository.sendPasswordResetEmail(email)
    }
}
