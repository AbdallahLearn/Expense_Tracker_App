package com.example.expense_tracking_project.data.dataSource.User

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val created_at: Date,
    val updated_at: Date
)