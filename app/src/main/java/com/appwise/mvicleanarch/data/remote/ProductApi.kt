package com.appwise.mvicleanarch.data.remote

import retrofit2.http.GET

interface ProductApi {
    @GET("products.json")
    suspend fun getProducts():List<ProductDto>

}
