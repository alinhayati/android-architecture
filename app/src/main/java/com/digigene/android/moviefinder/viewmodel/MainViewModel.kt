package com.digigene.android.moviefinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digigene.android.moviefinder.repository.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository) : ViewModel() {
    private val _list = MutableSharedFlow<List<ResultEntity>>(extraBufferCapacity = 1)
    val list: SharedFlow<List<ResultEntity>> = _list

    private val _showProgressBar = MutableStateFlow(false)
    val showProgressBar: StateFlow<Boolean> = _showProgressBar

    private val _error = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val error: SharedFlow<String> = _error

    fun fetchAddress(searchTerm: String) {
        viewModelScope.launch {
            _showProgressBar.tryEmit(true)
            try {
                repository.fetchAddress(searchTerm)?.let {
                    _list.tryEmit(it)
                }
            } catch (error: Exception) {
                _error.tryEmit(error.message ?: "an error occured")
            } finally {
                _showProgressBar.tryEmit(false)
            }
        }
    }
}

class ResultEntity(val title: String, val rating: String, val date: String, val year: String)