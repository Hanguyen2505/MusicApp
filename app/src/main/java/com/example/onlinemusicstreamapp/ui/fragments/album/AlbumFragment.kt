package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.database.data.entities.ItemData
import com.example.onlinemusicstreamapp.databinding.FragmentAlbumBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

//TODO display the song list in album in the first time create this fragment
@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

    //ViewModel
    private val mArtistViewModel: ArtistViewModel by viewModels()
    private val mPlaylistViewModel: PlaylistViewModel by viewModels()
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    //Adapter
    private val songAdapter = SongAdapter()

    //Safe Argument
    private val args by navArgs<AlbumFragmentArgs>()
    private val itemData = args.itemData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
//        showAlbumDetail()
//        subscribeToObserve()

//        mPlayerControlViewModel.sendArtistToMusicService(itemData.artist.name)

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        songAdapter.setOnItemClickListener { song ->
            Log.d("CurrenSongSelected", "$song")
            mPlayerControlViewModel.play(song)
        }

        return binding.root
    }

    private fun playAlbum() {

    }

    private fun showAlbumDetail() {

        when (itemData) {
            is ItemData.ArtistItem -> {
                val artist = itemData.artist
                binding.artistName.text = artist.name
                Glide.with(binding.prfImage).load(artist.imageUrl).into(binding.prfImage)
                Log.d("SongString", "${artist.name}")
            }
            is ItemData.PlaylistItem -> {
                val playlist = itemData.playlist
                binding.artistName.text = playlist.title
                Glide.with(binding.prfImage).load(playlist.coverImageUrl).into(binding.prfImage)
            }
        }


    }

    private fun subscribeToObserve() {

        val albumRecyclerView = binding.recyclerViewAlbum
        albumRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        when (itemData) {
            is ItemData.ArtistItem -> {
                val artist = itemData.artist
                Log.d("songlistLiveData", "${artist.name}")
                mArtistViewModel.getSong(artist.name).observe(viewLifecycleOwner){ songs ->
                    songAdapter.songs = songs
                    albumRecyclerView.adapter = songAdapter
                }
            }
            is ItemData.PlaylistItem -> {
                val playlist = itemData.playlist
                Log.d("songlistLiveData", "${playlist.title}")
                mPlaylistViewModel.getSongsInPlaylist(playlist.playlistId).observe(viewLifecycleOwner){ songs ->
                    Log.d("songlistLiveData", "${songs}")
                    songAdapter.songs = songs
                    albumRecyclerView.adapter = songAdapter
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}