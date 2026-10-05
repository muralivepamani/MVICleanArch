package com.appwise.mvicleanarch.data.repo

import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.data.mapper.toDomain
import com.appwise.mvicleanarch.data.remote.ApiService
import com.appwise.mvicleanarch.data.remote.ProductResponseDto
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repo.ProductRepo
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepo {

    override suspend fun getProducts(): Result<List<Product>> =
        safeCall { apiService.getProducts() }

    override suspend fun searchProducts(query: String): Result<List<Product>> =
        safeCall { apiService.searchProducts(query) }

    private suspend fun safeCall(call: suspend () -> ProductResponseDto): Result<List<Product>> =
        try {
            Result.Success(call().products.map { it.toDomain() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Something went wrong")
        }
}
