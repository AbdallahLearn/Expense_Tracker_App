package com.example.expense_tracking_project.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import java.util.Date

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

    @Query("SELECT * FROM BudgetEntity WHERE isSynced = 0 AND isDeleted = 0") // sync new items
    suspend fun getUnsyncedBudgets(): List<BudgetEntity>

    @Query("UPDATE BudgetEntity SET isSynced = 1 WHERE budgetId = :id") //
    suspend fun markBudgetAsSynced(id: Int)

    @Query("UPDATE BudgetEntity SET isDeleted = 1, updatedAt = :updatedAt, isSynced = 0 WHERE budgetId = :budgetId") //so we can sync the delete request to server
    suspend fun softDeleteBudget(budgetId: Int, updatedAt: Date)
}