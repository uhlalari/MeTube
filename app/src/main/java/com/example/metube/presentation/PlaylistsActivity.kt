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
    private lateinit var playlistVideos: List<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerPlaylist.layoutManager = LinearLayoutManager(this)

        playlistVideos = getPlaylistVideos()

        binding.recyclerPlaylist.adapter = VideoAdapter(this, playlistVideos) { selectedVideo ->
            openVideoPlayer(selectedVideo)
        }

        binding.btnPlayAll.setOnClickListener {
            if (playlistVideos.isNotEmpty()) {
                playPlaylist()
            }
        }
    }

    private fun getPlaylistVideos(): List<Video> {
        val sharedPreferences = getSharedPreferences("playlist", Context.MODE_PRIVATE)
        return sharedPreferences.all.map { entry ->
            Video(
                videoId = entry.key,
                title = entry.value.toString(),
                description = "Na Lista de Reprodução",
                thumbnailUrl = "https://img.youtube.com/vi/${entry.key}/mqdefault.jpg"
            )
        }
    }

    private fun openVideoPlayer(video: Video) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", video.videoId)
        startActivity(intent)
    }

    private fun playPlaylist() {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", playlistVideos[0].videoId)
        intent.putExtra("PLAYLIST", playlistVideos.map { it.videoId }.toTypedArray())
        startActivity(intent)
    }
}
