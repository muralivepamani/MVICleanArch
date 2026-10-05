package com.appwise.mvicleanarch.presentation

import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.repo.ProductRepo
import com.appwise.mvicleanarch.domain.usecases.SearchProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    private val repo = FakeProductRepo()

    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel() = ListViewModel(SearchProductsUseCase(repo))

    @Test
    fun `starts loading then shows all products`() = runTest {
        val vm = viewModel()
        assertEquals(UIState.Loading, vm.uiState.value)

        advanceUntilIdle()

        assertEquals(UIState.Success(listOf(product(1))), vm.uiState.value)
    }

    @Test
    fun `error is shown and retry recovers`() = runTest {
        repo.allResult = Result.Error(RuntimeException("boom"), "boom")
        val vm = viewModel()
        advanceUntilIdle()
        assertEquals(UIState.Error("boom"), vm.uiState.value)

        repo.allResult = Result.Success(listOf(product(1)))
        vm.fetchProducts()
        advanceUntilIdle()

        assertEquals(UIState.Success(listOf(product(1))), vm.uiState.value)
    }

    @Test
    fun `typing quickly triggers only one search for the latest query`() = runTest {
        val vm = viewModel()
        advanceUntilIdle()
        repo.calls.clear()

        vm.onQueryChange("p")
        advanceTimeBy(200)
        vm.onQueryChange("ph")
        advanceTimeBy(200)
        vm.onQueryChange("pho")
        advanceUntilIdle()

        assertEquals(listOf("search:pho"), repo.calls)
        assertEquals("pho", vm.query.value)
        assertEquals(UIState.Success(listOf(product(2))), vm.uiState.value)
    }

    @Test
    fun `clearQuery empties the query and reloads all products immediately`() = runTest {
        val vm = viewModel()
        vm.onQueryChange("phone")
        advanceUntilIdle()
        repo.calls.clear()

        vm.clearQuery()
        advanceUntilIdle()

        assertEquals("", vm.query.value)
        assertEquals(listOf("getProducts"), repo.calls)
        assertEquals(UIState.Success(listOf(product(1))), vm.uiState.value)
    }

    private fun product(id: Int) = Product(id, "p$id", "", 1.0, 1.0, "", emptyList())

    private inner class FakeProductRepo : ProductRepo {
        val calls = mutableListOf<String>()
        var allResult: Result<List<Product>> = Result.Success(listOf(product(1)))

        override suspend fun getProducts(): Result<List<Product>> {
            calls += "getProducts"
            return allResult
        }

        override suspend fun searchProducts(query: String): Result<List<Product>> {
            calls += "search:$query"
            return Result.Success(listOf(product(2)))
        }
    }
}
