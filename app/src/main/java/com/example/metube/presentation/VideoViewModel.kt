package com.example.metube.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metube.domain.SearchVideosUseCase
import com.example.metube.domain.Video
import kotlinx.coroutines.launch

class VideoViewModel(private val searchVideosUseCase: SearchVideosUseCase) : ViewModel() {
    private val _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>> get() = _videos

    fun searchVideos(query: String) {
        viewModelScope.launch {
            val result = searchVideosUseCase.execute(query)
            _videos.postValue(result)
        }
    }
}
