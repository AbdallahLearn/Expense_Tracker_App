package com.example.expense_tracking_project.core.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.core.local.entities.User
import com.example.expense_tracking_project.core.local.dao.UserDao

@Database(entities = [Transaction::class, BudgetEntity::class, Category::class, User::class], version = 13, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun BudgetDao(): BudgetDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}