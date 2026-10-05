package com.appwise.mvicleanarch.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(): ProductResponseDto

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductResponseDto
}
