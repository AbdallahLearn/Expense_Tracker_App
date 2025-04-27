package com.example.expense_tracking_project.core.local.db

import android.content.Context
import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.BudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.DataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.RemoteBudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.local.LocalBudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.remote.RemoteBudgetDataSourceImpl
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.BudgetApi
import com.example.expense_tracking_project.screens.expenseTracking.data.repositoryimpl.BudgetRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.CategoryRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.TransactionRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.GetAllbudgetsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.InsertTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.InsertBudgetUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.UpdateBudgetUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.InsertCategoryUseCase
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

    // --- BUDGET TRANSACTION ---
    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideTransactionRepository(
        transactionDao: TransactionDao
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    fun provideInsertTransactionUseCase(repository: TransactionRepository): InsertTransactionUseCase {
        return InsertTransactionUseCase(repository)
    }

    // --- BUDGET PROVIDERS ---
    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.BudgetDao()

    @Provides
    fun provideBudgetRepository(
        networkConnectivityObserver: NetworkConnectivityObserver,
        localDataSource: BudgetDataSource,
        remoteDataSource: RemoteBudgetDataSource
    ): BudgetRepository {
        return BudgetRepositoryImpl(networkConnectivityObserver, localDataSource, remoteDataSource)
    }

    @Provides
    fun provideGetAllBudgetsUseCase(budgetRepository: BudgetRepository): GetAllbudgetsUseCase {
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

    @Provides
    fun provideInsertCategoryUseCase(categoryRepository: CategoryRepository): InsertCategoryUseCase {
        return InsertCategoryUseCase(categoryRepository)
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.CategoryDao()

    @Provides
    fun provideCategoryRepository(
        networkConnectivityObserver: NetworkConnectivityObserver,
        localDataSource: DataSource,
        remoteDataSource: DataSource,
        syncCategoryRepository: SyncCategoryRepository
    ): CategoryRepository {
        return CategoryRepositoryImpl(
            networkConnectivityObserver,
            localDataSource,
            remoteDataSource,
            syncCategoryRepository
        )
    }

    @Provides
    fun provideLocalBudgetDataSource(budgetDao: BudgetDao): BudgetDataSource {
        return LocalBudgetDataSource(budgetDao)
    }

    @Provides
    fun provideRemoteBudgetDataSource(budgetApi: BudgetApi): RemoteBudgetDataSource {
        return RemoteBudgetDataSourceImpl(budgetApi)
    }

}
