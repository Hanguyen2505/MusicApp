package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.databinding.CardviewUserPlaylistSelectionBinding

class UserPlaylistSelectionAdapter: RecyclerView.Adapter<UserPlaylistSelectionAdapter.MyViewHolder>() {

    private lateinit var playlist: List<UserPlaylist>

    private val addedPlaylist = mutableListOf<UserPlaylist>()

    private var onAddButtonClickListener: ((UserPlaylist) -> Unit)? = null

    fun setOnAddButtonClickListener(listener: (UserPlaylist) -> Unit) {
        onAddButtonClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewUserPlaylistSelectionBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(userPlaylist: UserPlaylist) {
            binding.playlistTitle.text = userPlaylist.title
            Glide.with(binding.playlistImage).load(userPlaylist.coverUrl).into(binding.playlistImage)
        }

        fun onAddClickButton(onAddButtonClick: () -> Unit) {
            binding.addBtn.setOnClickListener {
                onAddButtonClick()
                binding.addBtn.setImageResource(
                    R.drawable.ic_add
                )
            }

        }

        fun modifyIfIsAdded(addedUserPlaylist: List<UserPlaylist>, userPlaylist: UserPlaylist) {
            if (addedUserPlaylist.contains(userPlaylist)) {
                binding.addBtn.setImageResource(
                    R.drawable.ic_done_ring
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewUserPlaylistSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = playlist[position]
        holder.bindData(currentItem)

        holder.onAddClickButton {
            onAddButtonClickListener?.invoke(currentItem)
        }

        holder.modifyIfIsAdded(addedPlaylist, currentItem)
    }

    fun updateData(newData: List<UserPlaylist>) {
        playlist = newData
        notifyDataSetChanged()
    }

    fun addPlaylist(playlist: List<UserPlaylist>) {
        addedPlaylist.clear()
        addedPlaylist.addAll(playlist)
        println(addedPlaylist)
        notifyDataSetChanged()
    }
}