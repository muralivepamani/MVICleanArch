package com.appwise.mvicleanarch.domain.usecases

import com.appwise.mvicleanarch.domain.repository.ProductRepository
import javax.inject.Inject

class RefreshProductsUseCase @Inject constructor(private val repository: ProductRepository) {
   suspend operator fun invoke(): Result<Unit> = repository.refreshProduct()

}
