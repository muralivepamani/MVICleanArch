package com.appwise.mvicleanarch.domain.usecases

import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repo.ProductRepo
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepo: ProductRepo
) {
    // Business rule: a blank query means "show everything".
    suspend operator fun invoke(query: String): Result<List<Product>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) productRepo.getProducts()
        else productRepo.searchProducts(trimmed)
    }
}
