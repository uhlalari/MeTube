package com.example.metube.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.metube.databinding.ActivityVideoPlayerBinding
import com.example.metube.domain.Video
import com.example.metube.utils.NotificationHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var youTubePlayer: YouTubePlayer
    private var videoDuration = 0f
    private var videoList: List<Video> = emptyList()
    private var hasSentNotification = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoId = intent.getStringExtra("VIDEO_ID") ?: return

        val videoListJson = intent.getStringExtra("VIDEO_LIST")
        if (videoListJson != null) {
            val type = object : TypeToken<List<Video>>() {}.type
            videoList = Gson().fromJson(videoListJson, type)
        }

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                playVideo(videoId)
            }

            override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
                super.onCurrentSecond(player, second)

                if (!hasSentNotification && second > 3) {
                    hasSentNotification = true
                    NotificationHelper.sendVideoNotification(this@VideoPlayerActivity)
                }

                if (videoDuration > 0 && second >= videoDuration - 1) {
                    playNextVideo()
                }
            }

            override fun onVideoDuration(player: YouTubePlayer, duration: Float) {
                videoDuration = duration
            }
        })

        binding.recyclerRecommended.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerRecommended.adapter = SmallVideoAdapter(this, videoList) { video ->
            playVideo(video.videoId)
        }
    }

    private fun playVideo(videoId: String) {
        youTubePlayer.loadVideo(videoId, 0f)
    }

    private fun playNextVideo() {
        val currentIndex = videoList.indexOfFirst { it.videoId == intent.getStringExtra("VIDEO_ID") }
        if (currentIndex != -1 && currentIndex < videoList.size - 1) {
            val nextVideo = videoList[currentIndex + 1]
            playVideo(nextVideo.videoId)
        }
    }
}
