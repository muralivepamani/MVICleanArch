package com.appwise.mvicleanarch.domain.repo

import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.domain.model.Product

interface ProductRepo {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun searchProducts(query: String): Result<List<Product>>
}
