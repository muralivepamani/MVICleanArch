package com.appwise.mvicleanarch.domain.usecases

import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repo.ProductRepo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchProductsUseCaseTest {

    private val repo = FakeProductRepo()
    private val useCase = SearchProductsUseCase(repo)

    @Test
    fun `blank query returns all products`() = runTest {
        val result = useCase("   ")

        assertEquals(listOf("getProducts"), repo.calls)
        assertEquals(Result.Success(listOf(product(1))), result)
    }

    @Test
    fun `non blank query is trimmed and searched`() = runTest {
        val result = useCase("  phone ")

        assertEquals(listOf("search:phone"), repo.calls)
        assertEquals(Result.Success(listOf(product(2))), result)
    }

    @Test
    fun `repo error is passed through`() = runTest {
        val error = Result.Error(RuntimeException("boom"), "boom")
        repo.searchResult = error

        assertEquals(error, useCase("phone"))
    }

    private fun product(id: Int) = Product(id, "p$id", "", 1.0, 1.0, "", emptyList())

    private inner class FakeProductRepo : ProductRepo {
        val calls = mutableListOf<String>()
        var searchResult: Result<List<Product>> = Result.Success(listOf(product(2)))

        override suspend fun getProducts(): Result<List<Product>> {
            calls += "getProducts"
            return Result.Success(listOf(product(1)))
        }

        override suspend fun searchProducts(query: String): Result<List<Product>> {
            calls += "search:$query"
            return searchResult
        }
    }
}
