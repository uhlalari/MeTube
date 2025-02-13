package com.example.metube.data

import android.content.Context
import com.example.metube.domain.Video
import com.example.metube.domain.VideoRepository
import com.example.metube.BuildConfig

class VideoRepositoryImpl(private val api: YouTubeApi, private val context: Context) : VideoRepository {
    override suspend fun searchVideos(query: String): List<Video> {
        val apiKey = BuildConfig.YOUTUBE_API_KEY
        val response = api.searchVideos(query = query, apiKey = apiKey)
        return response.items.mapNotNull { item ->
            val videoId = item.id.videoId ?: return@mapNotNull null
            val title = item.snippet.title ?: "Sem título"
            val description = item.snippet.description ?: "Sem descrição"
            val thumbnailUrl = item.snippet.thumbnails.medium.url ?: ""

            Video(
                videoId = videoId,
                title = title,
                description = description,
                thumbnailUrl = thumbnailUrl
            )
        }
    }
}
