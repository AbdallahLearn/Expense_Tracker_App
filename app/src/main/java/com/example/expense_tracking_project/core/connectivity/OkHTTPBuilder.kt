package com.example.expense_tracking_project.core.connectivity

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OkHTTPBuilder @Inject constructor(
    private val authInterceptor: AuthInterceptor
) {
    fun getUnsafeOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
        return builder.build()
    }
}