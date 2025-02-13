package com.example.metube.domain

class SearchVideosUseCase(private val repository: VideoRepository) {
    suspend fun execute(query: String) = repository.searchVideos(query)
}
