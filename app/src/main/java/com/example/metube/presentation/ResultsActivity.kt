package com.example.metube.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metube.data.RetrofitInstance
import com.example.metube.data.VideoRepositoryImpl
import com.example.metube.databinding.ActivityResultsBinding
import com.example.metube.domain.SearchVideosUseCase
import com.example.metube.domain.Video
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private lateinit var shimmerLayout: ShimmerFrameLayout

    private val videoRepository by lazy { VideoRepositoryImpl(RetrofitInstance.api, this) }
    private val searchVideosUseCase = SearchVideosUseCase(videoRepository)

    private val viewModel: VideoViewModel by viewModels {
        VideoViewModelFactory(searchVideosUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shimmerLayout = binding.shimmerLayout

        binding.recyclerVideos.layoutManager = LinearLayoutManager(this)

        viewModel.videos.observe(this) { videos ->
            if (videos.isNullOrEmpty()) {
                Toast.makeText(this, "Nenhum vídeo encontrado.", Toast.LENGTH_SHORT).show()
                hideShimmer()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    hideShimmer()
                    binding.recyclerVideos.adapter = VideoAdapter(this, videos) { selectedVideo ->
                        openVideoPlayer(selectedVideo, videos)
                    }
                }, 1000)
            }
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotBlank()) {
                searchVideos(query)
            } else {
                Toast.makeText(this, "Digite uma pesquisa válida.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMusicasEmAlta.setOnClickListener {
            searchVideos("Músicas em alta")
        }
        binding.btnTecnologia.setOnClickListener {
            searchVideos("mercado tech")
        }
        binding.btnNoticias.setOnClickListener {
            searchVideos("acontecendo hoje")
        }
        binding.btnMPB.setOnClickListener {
            searchVideos("MPB")
        }

        binding.ivFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
        binding.ivPlaylist.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
        binding.ivTerms.setOnClickListener {
            openTermsPage()
        }

        searchVideos("Músicas em alta")
    }


    private fun searchVideos(query: String) {
        showShimmer()
        viewModel.searchVideos(query)
    }


    private fun showShimmer() {
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()
        binding.recyclerVideos.visibility = View.GONE
    }


    private fun hideShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        binding.recyclerVideos.visibility = View.VISIBLE
    }

    private fun openVideoPlayer(video: Video, videoList: List<Video>) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("VIDEO_ID", video.videoId)
            putExtra("VIDEO_LIST", Gson().toJson(videoList))
        }
        startActivity(intent)
    }


    private fun openTermsPage() {
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)
    }
}
