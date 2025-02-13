package com.example.metube.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metube.R
import com.example.metube.databinding.ItemVideoBinding
import com.example.metube.domain.Video

class VideoAdapter(
    private val context: Context,
    private val videos: List<Video>,
    private val onVideoClick: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val playlistPreferences = context.getSharedPreferences("playlist", Context.MODE_PRIVATE)

    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]

        holder.binding.txtTitle.text = video.title
        Glide.with(holder.binding.imgThumbnail).load(video.thumbnailUrl).into(holder.binding.imgThumbnail)

        updateFavoriteIcon(holder, video)
        updatePlaylistIcon(holder, video)

        holder.binding.imgThumbnail.setOnClickListener {
            onVideoClick(video)
        }

        holder.binding.btnFavorite.setOnClickListener {
            toggleFavorite(video)
            updateFavoriteIcon(holder, video)
        }

        holder.binding.btnFavorite.setOnLongClickListener {
            val intent = Intent(context, FavoritesActivity::class.java)
            context.startActivity(intent)
            true
        }

        holder.binding.btnAddToList.setOnClickListener {
            togglePlaylist(video)
            updatePlaylistIcon(holder, video)
        }

        holder.binding.btnAddToList.setOnLongClickListener {
            try {
                val intent = Intent(context, PlaylistActivity::class.java)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao abrir a Playlist", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun getItemCount() = videos.size

    private fun isFavorite(video: Video): Boolean {
        return sharedPreferences.contains(video.videoId)
    }

    private fun updateFavoriteIcon(holder: ViewHolder, video: Video) {
        if (isFavorite(video)) {
            holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun toggleFavorite(video: Video) {
        val editor = sharedPreferences.edit()
        if (isFavorite(video)) {
            editor.remove(video.videoId)
            Toast.makeText(context, "Removido dos Favoritos", Toast.LENGTH_SHORT).show()
        } else {
            editor.putString(video.videoId, video.title)
            Toast.makeText(context, "Adicionado aos Favoritos", Toast.LENGTH_SHORT).show()
        }
        editor.apply()
    }

    private fun isInPlaylist(video: Video): Boolean {
        return playlistPreferences.contains(video.videoId)
    }

    private fun updatePlaylistIcon(holder: ViewHolder, video: Video) {
        if (isInPlaylist(video)) {
            holder.binding.btnAddToList.setImageResource(R.drawable.ic_playlist)
        } else {
            holder.binding.btnAddToList.setImageResource(R.drawable.ic_playlist)
        }
    }

    private fun togglePlaylist(video: Video) {
        val editor = playlistPreferences.edit()
        if (isInPlaylist(video)) {
            editor.remove(video.videoId)
            Toast.makeText(context, "Removido da Lista", Toast.LENGTH_SHORT).show()
        } else {
            editor.putString(video.videoId, video.title)
            Toast.makeText(context, "Adicionado à Lista", Toast.LENGTH_SHORT).show()
        }
        editor.apply()
    }
}
