package com.example.expense_tracking_project.core.connectivity

import com.example.expense_tracking_project.core.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenProvider.getToken()
        }

        var request = chain.request()
        request = request.newBuilder()
            .addHeader("authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}