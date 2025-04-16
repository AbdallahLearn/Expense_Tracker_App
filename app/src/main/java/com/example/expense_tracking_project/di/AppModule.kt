
package com.example.expense_tracking_project.di

import com.example.expense_tracking_project.screens.authentication.data.remote.FirebaseAuthDataSource
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import com.example.expense_tracking_project.screens.authentication.domain.usecase.ForgotPasswordUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAuthDataSource(
        firebaseAuth: FirebaseAuth
    ): FirebaseAuthDataSource = FirebaseAuthDataSource(firebaseAuth)

    @Provides
    fun provideAuthRepository( //AuthRepository
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): AuthRepository = AuthRepositoryImpl(firebaseAuthDataSource)


    @Provides
    fun provideLoginUseCase( // Sign In
        authRepository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideSignUpUseCase( // Sign Up
        authRepository: AuthRepository
    ): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

    @Provides
    fun provideForgotPasswordUseCase( // Forgot Password
        authRepository: AuthRepository
    ): ForgotPasswordUseCase {
        return ForgotPasswordUseCase(authRepository)
    }

}