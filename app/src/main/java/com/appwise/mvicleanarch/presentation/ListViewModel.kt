package com.appwise.mvicleanarch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appwise.mvicleanarch.core.common.Result
import com.appwise.mvicleanarch.domain.model.Product
import com.appwise.mvicleanarch.domain.usecases.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val searchProducts: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<List<Product>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<Product>>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchProducts()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        fetch(debounceMs = SEARCH_DEBOUNCE_MS)
    }

    // Clearing is a single action, so reload immediately instead of debouncing.
    fun clearQuery() {
        _query.value = ""
        fetch(debounceMs = 0)
    }

    fun fetchProducts() = fetch(debounceMs = 0)

    // Cancelling the previous job means only the latest query's result is shown,
    // and the debounce delay skips requests while the user is still typing.
    private fun fetch(debounceMs: Long) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            delay(debounceMs)
            _uiState.value = UIState.Loading
            _uiState.value = when (val result = searchProducts(_query.value)) {
                is Result.Success -> UIState.Success(result.data)
                is Result.Error -> UIState.Error(result.message)
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 400L
    }
}
