package com.example.expense_tracking_project.core.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expense_tracking_project.core.local.entities.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM Categories WHERE isDeleted = 0")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Categories WHERE categoryId = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Query("SELECT * FROM Categories WHERE categoryName = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): Category?

    @Query("SELECT * FROM Categories WHERE isSynced = 0 AND isDeleted = 0") // sync new items
    suspend fun getUnsyncedCategories(): List<Category>

    @Query("UPDATE Categories SET isSynced = 1 WHERE categoryId = :id")
    suspend fun markCategoriesAsSynced(id: Int)

    @Query("UPDATE Categories SET isDeleted = 1, updatedAt = :updatedAt WHERE categoryId = :categoryId")
    suspend fun softDeleteCategory(categoryId: Int, updatedAt: java.util.Date)

    @Query("DELETE FROM Categories")
    suspend fun clearCategories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCategory(category: Category)

    @Query("SELECT * FROM Categories WHERE categoryServerId = :serverId LIMIT 1")
    suspend fun getCategoryByServerId(serverId: String): Category?

    @Query("DELETE FROM Categories WHERE categoryName = :name AND categoryServerId IS NULL")
    suspend fun deleteDuplicateUnSynced(name: String)

}