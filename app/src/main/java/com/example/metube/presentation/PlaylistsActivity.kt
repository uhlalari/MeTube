package com.example.metube.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metube.databinding.ActivityPlaylistBinding
import com.example.metube.domain.Video

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private var playlistVideos: List<Video> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerPlaylist.layoutManager = LinearLayoutManager(this)

        playlistVideos = getPlaylistVideos()

        if (playlistVideos.isNotEmpty()) {
            binding.recyclerPlaylist.adapter = VideoAdapter(this, playlistVideos) { selectedVideo ->
                openVideoPlayer(selectedVideo)
            }
        }

        binding.btnPlayAll.setOnClickListener {
            if (playlistVideos.isNotEmpty()) {
                playPlaylist()
            }
        }
    }

    private fun getPlaylistVideos(): List<Video> {
        val sharedPreferences = getSharedPreferences("playlist", Context.MODE_PRIVATE)
        return sharedPreferences.all.mapNotNull { entry ->
            val title = entry.value?.toString() ?: "Sem título"
            Video(
                videoId = entry.key,
                title = title,
                description = "Na Lista de Reprodução",
                thumbnailUrl = "https://img.youtube.com/vi/${entry.key}/mqdefault.jpg"
            )
        }
    }

    private fun openVideoPlayer(video: Video) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", video.videoId)
        intent.putExtra("VIDEO_TITLE", video.title)
        intent.putExtra("PLAYLIST", playlistVideos.map { it.videoId }.toTypedArray())
        intent.putExtra("PLAYLIST_TITLES", playlistVideos.map { it.title }.toTypedArray())
        startActivity(intent)
    }

    private fun playPlaylist() {
        if (playlistVideos.isNotEmpty()) {
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("VIDEO_ID", playlistVideos.first().videoId)
            intent.putExtra("PLAYLIST", playlistVideos.map { it.videoId }.toTypedArray())
            intent.putExtra("PLAYLIST_TITLES", playlistVideos.map { it.title }.toTypedArray())
            startActivity(intent)
        }
    }
}
