package com.example.metube.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metube.data.RetrofitInstance
import com.example.metube.data.VideoRepositoryImpl
import com.example.metube.databinding.ActivityResultsBinding
import com.example.metube.domain.SearchVideosUseCase
import com.example.metube.domain.Video
import com.google.gson.Gson

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultsBinding

    private val videoRepository = VideoRepositoryImpl(RetrofitInstance.api)
    private val searchVideosUseCase = SearchVideosUseCase(videoRepository)

    private val viewModel: VideoViewModel by viewModels {
        VideoViewModelFactory(searchVideosUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerVideos.layoutManager = LinearLayoutManager(this)

        binding.btnTerms.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
        }

        val query = intent.getStringExtra("SEARCH_QUERY") ?: return
        viewModel.videos.observe(this) { videos ->
            binding.recyclerVideos.adapter = VideoAdapter(this, videos) { selectedVideo ->
                openVideoPlayer(selectedVideo, videos)
            }
        }
        viewModel.searchVideos(query)
    }

    private fun openVideoPlayer(video: Video, videoList: List<Video>) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("VIDEO_ID", video.videoId)
        intent.putExtra("VIDEO_LIST", Gson().toJson(videoList))
        startActivity(intent)
    }
}
