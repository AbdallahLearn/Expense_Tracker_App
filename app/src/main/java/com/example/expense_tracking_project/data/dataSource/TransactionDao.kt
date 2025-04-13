package com.example.expense_tracking_project.data.dataSource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Insert
import androidx.room.Update


@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)
}

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class) // Add if Date needs type conversion
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}