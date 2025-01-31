package com.example.metube.domain

data class VideoResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(val videoId: String)

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnail
)

data class Thumbnail(val medium: ThumbnailInfo)
data class ThumbnailInfo(val url: String)
