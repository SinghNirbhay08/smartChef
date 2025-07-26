package com.nirbhay.smartchef.ui




import com.nirbhay.smartchef.network.GeminiResponse
import com.nirbhay.smartchef.network.PexelsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Gemini API call (POST)
    @POST("v1/generateText")
    suspend fun generateRecipe(
        @Body prompt: String,
        @Header("Authorization") authHeader: String = "Bearer AIzaSyDzEouuM4O0PMTWgzIPBsIyesd1F4ZV8wo"
    ): GeminiResponse

    // Pexels API call (GET)
    @GET("v1/search")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1,
        @Header("Authorization") authHeader: String = "Bearer 99ZdETxYFV5ZqE1siXtTcNlsVXUxXaW16PyMxHYmPpwEJnAZCkbNogZ9"
    ): PexelsResponse
}
