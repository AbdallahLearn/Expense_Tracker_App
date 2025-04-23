package com.example.expense_tracking_project.screens.expenseTracking.data.repositoryimpl

import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.BudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.RemoteBudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val localDataSource: BudgetDataSource,
    private val remoteDataSource: RemoteBudgetDataSource
) : BudgetRepository {

    override suspend fun insertbudget(budget: BudgetEntity) {
        val status = networkConnectivityObserver.observe().first()
        if (status == "Available") {
            remoteDataSource.insertBudget(budget)

            val updatedBudgets = remoteDataSource.getBudgets()
            localDataSource.clearAndInsert(updatedBudgets)
        } else {
            localDataSource.insertBudget(budget)
        }
    }

    override suspend fun getAllbudgets(): List<BudgetEntity> {
        return localDataSource.getBudgets()
    }

    override suspend fun updatebudget(budget: BudgetEntity) {
        localDataSource.updateBudget(budget)
    }
}
