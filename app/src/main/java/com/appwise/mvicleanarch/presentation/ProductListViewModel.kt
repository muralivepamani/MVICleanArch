package com.appwise.mvicleanarch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appwise.mvicleanarch.domain.usecases.GetProductListUseCase
import com.appwise.mvicleanarch.domain.usecases.RefreshProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductListUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProductListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeProducts()
        refresh()
    }

    fun onIntent(intent: ProductListIntent) {
        when (intent) {
            ProductListIntent.Refresh -> refresh()
            is ProductListIntent.ProductClicked ->
                viewModelScope.launch { _effect.send(ProductListEffect.NavigateToDetail(intent.id)) }
        }
    }

    private fun observeProducts() {
        getProducts()
            .onEach { products -> _state.update { it.copy(products = products) } }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            refreshProductsUseCase()
                .onSuccess { _state.update { it.copy(isLoading = false) } }
                .onFailure { e ->
                    val message = e.message ?: "Something went wrong"
                    _state.update { it.copy(isLoading = false, error = message) }
                    _effect.send(ProductListEffect.ShowMessage(message))
                }
        }
    }
}
