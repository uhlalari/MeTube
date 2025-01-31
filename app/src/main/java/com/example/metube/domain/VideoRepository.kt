package com.example.metube.domain

interface VideoRepository {
    suspend fun searchVideos(query: String): List<Video>
}
