package com.example.metube.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metube.databinding.ItemVideoSmallBinding
import com.example.metube.domain.Video

class SmallVideoAdapter(
    private val context: Context,
    private val videos: List<Video>,
    private val onClick: (Video) -> Unit
) : RecyclerView.Adapter<SmallVideoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemVideoSmallBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]

        holder.binding.txtTitle.text = video.title
        Glide.with(holder.binding.imgThumbnail).load(video.thumbnailUrl).into(holder.binding.imgThumbnail)

        holder.itemView.setOnClickListener {
            onClick(video)
        }
    }

    override fun getItemCount() = videos.size
}
