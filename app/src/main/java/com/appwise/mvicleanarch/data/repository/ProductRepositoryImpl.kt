package com.appwise.mvicleanarch.data.repository

import android.util.Log
import com.appwise.mvicleanarch.data.local.ProductDao
import com.appwise.mvicleanarch.data.mapper.toDomain
import com.appwise.mvicleanarch.data.mapper.toEntity
import com.appwise.mvicleanarch.data.remote.ProductApi
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repository.ProductRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshProduct(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val remote = api.getProducts()
            dao.replaceAll(remote.map { it.toEntity() })
            Log.d("ProductRepositoryImpl", "refreshProduct: $remote")
            Result.success(Unit)
        } catch (e: CancellationException) {
            Log.d("ProductRepositoryImpl", "refreshProduct: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.d("ProductRepositoryImpl", "refreshProduct: ${e.message}")

            Result.failure(e)
        }
    }
}