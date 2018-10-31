package com.digigene.android.moviefinder.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.digigene.android.moviefinder.SchedulersWrapper

class MainViewModelFactory(val schedulersWrapper: SchedulersWrapper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(schedulersWrapper) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}