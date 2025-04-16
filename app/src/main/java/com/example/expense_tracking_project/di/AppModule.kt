
package com.example.expense_tracking_project.di

import android.content.Context
import androidx.room.Room
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.core.local.db.AppDatabase
import com.example.expense_tracking_project.screens.authentication.data.remote.FirebaseAuthDataSource
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import com.example.expense_tracking_project.screens.authentication.domain.usecase.ForgotPasswordUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.example.expense_tracking_project.screens.authentication.domain.usecase.SignUpUseCase
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.BudgetRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.TransactionRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.GetAllbudgetsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.InsertBudgetUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.UpdateBudgetUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetAllTransactionsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.InsertTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
// ROOM PROVIDERS

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "budget_db"
        ).build()
    }

    // PROVIDERS TransactionDao Database
    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    fun provideGetAllTransactionsUseCase(transactionRepository: TransactionRepository): GetAllTransactionsUseCase {
        return GetAllTransactionsUseCase(transactionRepository)
    }

    @Provides
    fun provideInsertTransactionUseCase(transactionRepository: TransactionRepository): InsertTransactionUseCase {
        return InsertTransactionUseCase(transactionRepository)
    }
    @Provides
    fun provideUpdateTransactionUseCasee(transactionRepository: TransactionRepository): UpdateTransactionUseCase {
        return UpdateTransactionUseCase(transactionRepository)
    }
    // PROVIDERS Budget Database
    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.BudgetDao()

    @Provides
    fun provideBudgetRepository(budgetDao: BudgetDao): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao)
    }

    @Provides
    fun provideGetAllbudgetsUseCase(budgetRepository: BudgetRepository): GetAllbudgetsUseCase {
        return GetAllbudgetsUseCase(budgetRepository)
    }

    @Provides
    fun provideInsertBudgetUseCase(budgetRepository: BudgetRepository): InsertBudgetUseCase {
        return InsertBudgetUseCase(budgetRepository)
    }
    @Provides
    fun provideUpdateBudgetUseCase(budgetRepository: BudgetRepository): UpdateBudgetUseCase {
        return UpdateBudgetUseCase(budgetRepository)
    }
}
