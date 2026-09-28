package com.appwise.mvicleanarch.domain.usecases

import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> = repository.observeProducts()

}
