package com.example.onlinemusicstreamapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.other.Constants.HOME_FRAGMENT
import com.example.onlinemusicstreamapp.database.other.Constants.SEARCH_VIEW_FRAGMENT
import com.example.onlinemusicstreamapp.database.repository.ArtistRepository
import com.example.onlinemusicstreamapp.databinding.CardviewArtistBinding
import com.example.onlinemusicstreamapp.ui.fragments.home.HomeFragmentDirections
import com.example.onlinemusicstreamapp.ui.fragments.search.SearchViewFragmentDirections


class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.MyViewHolder>() {

    var artist = emptyList<Artist>()

    var navFromFragment = String()

    class MyViewHolder(
        private val binding: CardviewArtistBinding, private val navFromFragment: String
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(artist: Artist) {
            binding.artistName.text = artist.name
            Glide.with(binding.prfImage).load(artist.imageUrl).into(binding.prfImage)

        }

        fun artistListener(artist: Artist) {
            binding.root.setOnClickListener {
                when (navFromFragment) {
                    HOME_FRAGMENT -> {
                        // Navigate from HomeFragment
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToAlbumFragment(artist)
                        itemView.findNavController().navigate(action)
                    }

                    SEARCH_VIEW_FRAGMENT -> {
                        // Navigate from SearchFragment
                        val action =
                            SearchViewFragmentDirections.actionSearchViewFragmentToAlbumFragment(
                                artist
                            )
                        itemView.findNavController().navigate(action)
                    }

                    else -> {
                        // Handle default or unexpected cases
                        Log.e("MyViewHolder", "Unknown fragment for navigation")
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewArtistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding, navFromFragment)
    }

    override fun getItemCount(): Int {
        return artist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = artist[position]
        ArtistRepository.getData(currentItem)
        holder.bindData(currentItem)
        holder.artistListener(currentItem)

    }

    fun updateData(newArtist: List<Artist>) {
        artist = newArtist
        notifyDataSetChanged()
    }

}