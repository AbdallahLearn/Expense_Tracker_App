package com.example.expense_tracking_project.di

import android.content.Context
import com.example.expense_tracking_project.core.TokenProvider
import com.example.expense_tracking_project.core.connectivity.AuthInterceptor
import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.core.connectivity.OkHTTPBuilder
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.screens.authentication.data.remote.FirebaseAuthDataSource
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import com.example.expense_tracking_project.screens.authentication.domain.usecase.ForgotPasswordUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.SignUpUseCase
import com.example.expense_tracking_project.screens.dataSynchronization.data.SyncCategoryRepositoryImpl
import com.example.expense_tracking_project.screens.dataSynchronization.data.SyncRepositoryImpl
import com.example.expense_tracking_project.screens.dataSynchronization.data.SyncTransactionRepositoryImpl
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncRepository
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncTransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.BudgetApi
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.TransactionApi
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryApi
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
    fun provideTokenProvider(
        firebaseAuth: FirebaseAuth
    ): TokenProvider = TokenProvider(firebaseAuth)

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

    @Provides
    fun provideSyncRepository(
        budgetDao: BudgetDao,
        budgetApi: BudgetApi
    ): SyncRepository {
        return SyncRepositoryImpl(budgetDao, budgetApi)
    }

    @Provides
    fun provideSyncCategoryRepository(
        categoryDao: CategoryDao,
        categoryApi: CategoryApi
    ): SyncCategoryRepository {
        return SyncCategoryRepositoryImpl(categoryDao, categoryApi)
    }

    @Provides
    fun provideSyncTransactionRepository(
        transactionDao: TransactionDao,
        transactionApi: TransactionApi
    ): SyncTransactionRepository {
        return SyncTransactionRepositoryImpl(transactionDao, transactionApi)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenProvider: TokenProvider
    ): AuthInterceptor = AuthInterceptor(tokenProvider)

    @Provides
    @Singleton
    fun provideOkHTTPBuilder(
        authInterceptor: AuthInterceptor
    ): OkHTTPBuilder = OkHTTPBuilder(authInterceptor)

    @Provides
    fun provideRetrofit(okHTTPBuilder: OkHTTPBuilder): Retrofit {
        return Retrofit.Builder()
            .client(okHTTPBuilder.getUnsafeOkHttpClient())
            .baseUrl("https://project-1-admissions3.replit.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideBudgetApi(retrofit: Retrofit): BudgetApi {
        return retrofit.create(BudgetApi::class.java)
    }

    @Provides
    fun provideNetworkObserver( @ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    } // using ApplicationContext to allow the observer to work across the whole app lifecycle

    @Provides
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    fun provideTransactionApi(retrofit: Retrofit): TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

}