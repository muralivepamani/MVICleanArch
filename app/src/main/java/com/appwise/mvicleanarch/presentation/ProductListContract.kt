package com.appwise.mvicleanarch.presentation

import com.appwise.mvicleanarch.domain.model.Product


data class ProductListState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ProductListIntent {
    data object Refresh : ProductListIntent
    data class ProductClicked(val id: Int) : ProductListIntent
}

sealed interface ProductListEffect {
    data class ShowMessage(val message: String) : ProductListEffect
    data class NavigateToDetail(val id: Int) : ProductListEffect
}