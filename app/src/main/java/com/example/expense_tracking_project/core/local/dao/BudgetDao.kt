package com.example.expense_tracking_project.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expense_tracking_project.core.local.entities.BudgetEntity

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("SELECT * FROM BudgetEntity WHERE isDeleted = 0")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM BudgetEntity WHERE budgetId = :id")
    suspend fun getBudgetById(id: Int): BudgetEntity?
}