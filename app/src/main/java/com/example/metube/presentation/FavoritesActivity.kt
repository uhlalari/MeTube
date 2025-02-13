package com.example.metube.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metube.databinding.ActivityFavoritesBinding
import com.example.metube.domain.Video

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFavorites.layoutManager = LinearLayoutManager(this)

        val favoriteVideos = getFavoriteVideos()

        binding.recyclerFavorites.adapter = VideoAdapter(this, favoriteVideos) { selectedVideo ->
            openVideoPlayer(selectedVideo)
        }
    }

    private fun getFavoriteVideos(): List<Video> {
        val sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        return sharedPreferences.all.mapNotNull { entry ->
            val title = entry.value?.toString() ?: "Título não disponível"
            Video(
                videoId = entry.key,
                title = title,
                description = "",
                thumbnailUrl = "https://img.youtube.com/vi/${entry.key}/mqdefault.jpg"
            )
        }
    }

    private fun openVideoPlayer(video: Video) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", video.videoId)
        intent.putExtra("VIDEO_TITLE", video.title)
        intent.putExtra("FAVORITES", true)
        startActivity(intent)
    }
}
