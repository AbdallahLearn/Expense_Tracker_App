package com.example.expense_tracking_project.core.local.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expense_tracking_project.core.local.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User WHERE userId = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM User ORDER BY created_at DESC")
    fun getAllUsers(): LiveData<List<User>>
}
