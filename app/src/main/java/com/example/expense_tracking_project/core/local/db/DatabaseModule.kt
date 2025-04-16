package com.example.expense_tracking_project.core.local.db

import android.content.Context
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.TransactionRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.InsertTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    fun provideInsertTransactionUseCase(repository: TransactionRepository): InsertTransactionUseCase {
        return InsertTransactionUseCase(repository)
    }

    // Add other use cases here if needed
}
