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
        return sharedPreferences.all.map { entry ->
            Video(
                videoId = entry.key,
                title = entry.value.toString(),
                description = "Favorito",
                thumbnailUrl = "https://img.youtube.com/vi/${entry.key}/mqdefault.jpg"
            )
        }
    }

    private fun openVideoPlayer(video: Video) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", video.videoId)
        startActivity(intent)
    }
}
