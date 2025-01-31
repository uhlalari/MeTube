package com.example.metube.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.metube.domain.SearchVideosUseCase

class VideoViewModelFactory(private val searchVideosUseCase: SearchVideosUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoViewModel(searchVideosUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
