package com.example.metube.data

import com.example.metube.domain.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {
    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int = 50,
        @Query("type") type: String = "video"
    ): VideoResponse
}
