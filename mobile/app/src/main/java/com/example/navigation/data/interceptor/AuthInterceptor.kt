package com.example.navigation.data.interceptor

import android.util.Log
import com.example.navigation.data.auth.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        TokenProvider.token?.let { jwt ->
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $jwt"
            )
        }
        return chain.proceed(requestBuilder.build())
    }
}
