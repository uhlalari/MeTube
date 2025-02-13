package com.example.metube.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.metube.databinding.ActivityVideoPlayerBinding
import com.example.metube.domain.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    // Lista de vídeos (playlist ou recomendados)
    private var videoList: MutableList<Video> = mutableListOf()

    // Dados do vídeo atual
    private var videoId: String? = null
    private var youTubePlayer: YouTubePlayer? = null

    // Controle de modos
    private var isPlaylistMode = false
    private var isFavoritesMode = false

    // Duração do vídeo (para detectar o final)
    private var videoDuration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Recebe dados do Intent
        videoId = intent.getStringExtra("VIDEO_ID")
        val videoTitleFromIntent = intent.getStringExtra("VIDEO_TITLE")

        // 2) Se vier JSON de lista (Recomendados), desserializa
        val videoListJson = intent.getStringExtra("VIDEO_LIST")
        if (!videoListJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Video>>() {}.type
            videoList = Gson().fromJson<List<Video>>(videoListJson, type)?.toMutableList()
                ?: mutableListOf()
        }

        // 3) Verifica se é Playlist ou Favoritos
        isPlaylistMode = intent.hasExtra("PLAYLIST")
        isFavoritesMode = intent.hasExtra("FAVORITES")

        // 4) Se for Playlist, monta lista na ordem informada
        if (isPlaylistMode) {
            val playlistVideos = intent.getStringArrayExtra("PLAYLIST") ?: emptyArray()
            val playlistTitles = intent.getStringArrayExtra("PLAYLIST_TITLES") ?: emptyArray()

            // Cria a lista final
            videoList = playlistVideos.mapIndexed { index, id ->
                val title = playlistTitles.getOrNull(index) ?: "Sem título"
                Video(
                    videoId = id,
                    title = title,
                    description = "",
                    thumbnailUrl = "https://img.youtube.com/vi/$id/mqdefault.jpg"
                )
            }.toMutableList()
        }

        // 5) Configura RecyclerView (vídeos recomendados ou playlist)
        setupRecyclerView()

        // 6) Configura YouTubePlayer
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                if (videoId == null) {
                    finish()
                    return
                }
                playVideo(videoId!!)
            }

            override fun onVideoDuration(player: YouTubePlayer, duration: Float) {
                // Captura a duração do vídeo para detectar o final
                videoDuration = duration
            }

            override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
                super.onCurrentSecond(player, second)
                // Se estivermos quase no final do vídeo, chama o próximo
                if (videoDuration > 0 && second >= videoDuration - 1) {
                    playNextVideo()
                }
            }
        })

        // 7) Ajusta título inicial (Favoritos ou fallback da lista)
        val fallbackTitle = videoList.find { it.videoId == videoId }?.title ?: videoTitleFromIntent ?: "Título não disponível"
        binding.txtVideoTitle.text = fallbackTitle

        // Ajusta o texto acima da lista (Próximos vídeos, Vídeos parecidos ou nada)
        updateRecommendedText()
    }

    /**
     * Configura a RecyclerView para mostrar os vídeos abaixo
     */
    private fun setupRecyclerView() {
        binding.recyclerRecommended.apply {
            layoutManager = LinearLayoutManager(this@VideoPlayerActivity)
            adapter = SmallVideoAdapter(this@VideoPlayerActivity, videoList) { selectedVideo ->
                // Ao clicar em um vídeo, vamos reproduzi-lo
                playVideo(selectedVideo.videoId)
            }
        }
    }

    /**
     * Reproduz um vídeo específico
     * - Se for playlist, remove primeiro o item para não aparecer na lista
     * - Carrega no Player
     * - Ajusta título
     */
    private fun playVideo(newVideoId: String) {
        var currentVideo: Video? = null

        if (isPlaylistMode) {
            // Acha o item na lista
            val index = videoList.indexOfFirst { it.videoId == newVideoId }
            if (index >= 0) {
                currentVideo = videoList[index]
                // Remove antes de tocar, assim não fica na lista
                videoList.removeAt(index)
                binding.recyclerRecommended.adapter?.notifyDataSetChanged()
            }
        } else {
            // Em modo normal ou favoritos, só busca o vídeo
            currentVideo = videoList.find { it.videoId == newVideoId }
        }

        // Se ainda não encontramos o vídeo e estamos no modo Favoritos,
        // tentamos usar o título enviado pelo Intent
        val fallbackTitle = currentVideo?.title ?: intent.getStringExtra("VIDEO_TITLE") ?: "Título não disponível"

        // Carrega o vídeo no player
        youTubePlayer?.loadVideo(newVideoId, 0f)

        // Para shimmer e mostra o player
        stopShimmerAndShowPlayer()

        // Ajusta título corretamente
        binding.txtVideoTitle.text = fallbackTitle
    }

    /**
     * Quando o vídeo atual termina,
     * se ainda houver vídeos na playlist, toca o primeiro da lista
     */
    private fun playNextVideo() {
        if (isPlaylistMode && videoList.isNotEmpty()) {
            val nextVideoId = videoList[0].videoId
            playVideo(nextVideoId)
        } else {
            // Se não há mais vídeos, encerra
            finish()
        }
    }

    /**
     * Para o Shimmer e exibe o container do player
     */
    private fun stopShimmerAndShowPlayer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.playerContainer.visibility = View.VISIBLE
    }

    /**
     * Define o texto acima do Recycler
     * - Playlist: "Próximos vídeos"
     * - Favoritos: Invisível
     * - Geral: "Vídeos parecidos"
     */
    private fun updateRecommendedText() {
        when {
            isPlaylistMode -> {
                binding.recomendedText.text = "Próximos vídeos"
                binding.recomendedText.visibility = View.VISIBLE
            }
            isFavoritesMode -> {
                binding.recomendedText.visibility = View.GONE
            }
            else -> {
                binding.recomendedText.text = "Vídeos parecidos"
                binding.recomendedText.visibility = View.VISIBLE
            }
        }
    }
}
