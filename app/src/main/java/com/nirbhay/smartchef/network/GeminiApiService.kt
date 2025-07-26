package com.nirbhay.smartchef.api

import com.nirbhay.smartchef.network.GeminiRequest
import com.nirbhay.smartchef.network.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent")
    suspend fun generateRecipe(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}