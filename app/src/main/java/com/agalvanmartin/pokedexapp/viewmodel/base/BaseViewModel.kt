package com.agalvanmartin.pokedexapp.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState> : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<UiState>>(ViewState.Loading)
    val uiState: StateFlow<ViewState<UiState>> = _uiState

    protected fun updateState(state: UiState) {
        _uiState.value = ViewState.Success(state)
    }

    protected fun showError(error: Throwable) {
        _uiState.value = ViewState.Error(error)
    }

    protected fun showLoading() {
        _uiState.value = ViewState.Loading
    }

    protected fun launchWithState(block: suspend () -> UiState) {
        viewModelScope.launch {
            try {
                showLoading()
                val result = block()
                updateState(result)
            } catch (e: Exception) {
                showError(e)
            }
        }
    }
}

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val error: Throwable) : ViewState<Nothing>()
} 