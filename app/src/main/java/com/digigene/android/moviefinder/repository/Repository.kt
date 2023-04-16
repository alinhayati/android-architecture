package com.digigene.android.moviefinder.repository

import com.digigene.android.moviefinder.viewmodel.NetworkService
import com.digigene.android.moviefinder.viewmodel.ResultEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val networkService: NetworkService) {

    suspend fun fetchAddress(searchTerm: String): List<ResultEntity>? {
        return withContext(Dispatchers.IO) { networkService.fetchAddress(searchTerm) }
    }
}