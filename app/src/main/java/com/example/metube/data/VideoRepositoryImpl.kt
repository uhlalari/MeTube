package com.example.metube.data

import com.example.metube.domain.Video
import com.example.metube.domain.VideoRepository

class VideoRepositoryImpl(private val api: YouTubeApi) : VideoRepository {
    override suspend fun searchVideos(query: String): List<Video> {
        val response = api.searchVideos(query = query, apiKey = "AIzaSyCW_IMhP8Cqz79CQdZuXPCjGuA_9_NQY_o")
        return response.items.map {
            Video(
                videoId = it.id.videoId,
                title = it.snippet.title,
                description = it.snippet.description,
                thumbnailUrl = it.snippet.thumbnails.medium.url
            )
        }
    }
}
