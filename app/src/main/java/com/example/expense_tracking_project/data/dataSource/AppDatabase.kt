package com.example.expense_tracking_project.data.dataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expense_tracking_project.data.dataSource.Budget.BudgetDao
import com.example.expense_tracking_project.data.dataSource.Category.Category
import com.example.expense_tracking_project.data.dataSource.Budget.BudgetEntity
import com.example.expense_tracking_project.data.dataSource.Category.CategoryDao
import com.example.expense_tracking_project.data.dataSource.Transaction.DateConverter
import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.data.dataSource.Transaction.TransactionDao
import com.example.expense_tracking_project.data.dataSource.User.User
import com.example.expense_tracking_project.data.dataSource.User.UserDao

@Database(entities = [Transaction::class, BudgetEntity::class, Category::class, User::class], version = 6, exportSchema = false)
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