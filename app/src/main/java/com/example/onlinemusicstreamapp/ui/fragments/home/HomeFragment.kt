package com.example.onlinemusicstreamapp.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.ArtistAdapter
import com.example.onlinemusicstreamapp.adapter.PlaylistAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentHomeBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mSongViewModel: SongViewModel by viewModels()

    private val mArtistViewModel: ArtistViewModel by viewModels()

    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    private val mPlaylistViewModel: PlaylistViewModel by viewModels()

    private val songAdapter = SongAdapter()

    private val playlistAdapter = PlaylistAdapter()

    private val artistAdapter = ArtistAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        subscribeToObserver()

        songAdapter.setOnItemClickListener { song ->
            Log.d("CurrentSongSelected", "$song")
            mPlayerControlViewModel.play(song)
        }

        artistAdapter.setOnItemClickListener { artist ->
            val action = HomeFragmentDirections.actionHomeFragmentToAlbumFragment(artist)
            findNavController().navigate(action)
        }

        playlistAdapter.setOnItemClickListener { playlist ->
            val action = HomeFragmentDirections.actionHomeFragmentToPlaylistFragment2(playlist)
            findNavController().navigate(action)
        }

        return _binding!!.root
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val itemId = p0.itemId

        when (itemId) {
            R.id.historyFragment -> {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }

        }
        return true
    }

    private fun subscribeToObserver() {

        //*set up a view where will show a list of artist/
        val artistRecyclerView = binding.recyclerViewArtist
        artistRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        //*set up a view where will show a list of playlist/
        val playlistRecyclerView = binding.recyclerViewPlaylist
        playlistRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        //*set up a view where will show a list of song/
        val songRecyclerView = binding.recyclerViewSong
        songRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        //*display all artist in database/
        mArtistViewModel.artist.observe(viewLifecycleOwner) { artists ->
            artistAdapter.artist = artists
            artistRecyclerView.adapter = artistAdapter
        }

        //*display all playlist in database/
        mPlaylistViewModel.playlist.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.playlist = playlists
            playlistRecyclerView.adapter = playlistAdapter

        }

        //*display all song in database/
        mSongViewModel.songs.observe(viewLifecycleOwner){ songs ->
//            songAdapter = SongAdapter(songs)
            songAdapter.songs = songs
            songRecyclerView.adapter = songAdapter
        }

    }

}