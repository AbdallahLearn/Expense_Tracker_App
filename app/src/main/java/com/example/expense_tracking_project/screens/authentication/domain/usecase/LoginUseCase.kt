package com.example.expense_tracking_project.screens.authentication.domain.usecase

import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return authRepository.login(email, password)
    }
}


