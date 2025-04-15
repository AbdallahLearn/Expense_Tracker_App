package com.example.expense_tracking_project.utils

import android.os.Build

fun isAtLeastOreo(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}