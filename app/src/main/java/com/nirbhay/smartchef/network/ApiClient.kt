package com.nirbhay.smartchef.network

import com.nirbhay.smartchef.BuildConfig
import com.nirbhay.smartchef.api.GeminiApiService
import com.nirbhay.smartchef.api.PexelsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
    private const val PEXELS_BASE_URL = "https://api.pexels.com/"

    val geminiApi: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }

    val pexelsApi: PexelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(PEXELS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }

    fun getGeminiApiKey(): String = BuildConfig.GEMINI_API_KEY
    fun getPexelsApiKey(): String = "Bearer ${BuildConfig.PEXELS_API_KEY}"
}
