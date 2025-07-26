package com.nirbhay.smartchef.api

import com.nirbhay.smartchef.network.PexelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1,
        @Header("Authorization") authorization: String
    ): Response<PexelsResponse>
}
