package com.appwise.mvicleanarch.domain.repository

import com.appwise.mvicleanarch.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    suspend fun refreshProduct(): Result<Unit>

}
