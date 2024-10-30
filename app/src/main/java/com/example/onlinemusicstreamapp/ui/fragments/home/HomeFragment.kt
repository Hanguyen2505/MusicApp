package com.example.onlinemusicstreamapp.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.ArtistAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.database.other.Constants.HOME_FRAGMENT
import com.example.onlinemusicstreamapp.databinding.FragmentHomeBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
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

    private val songAdapter = SongAdapter()

    private val artistAdapter = ArtistAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        subscribeToObserver()

        songAdapter.setOnItemClickListener { song ->
            Log.d("CurrenSongSelected", "$song")
            mPlayerControlViewModel.play(song)
        }

        artistAdapter.navFromFragment = HOME_FRAGMENT

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

    fun subscribeToObserver() {
        //*set up a view where will show a list of song/
        val songRecyclerView = binding.recyclerViewSong
        songRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        //*set up a view where will show a list of artist/
        val artistRecyclerView = binding.recyclerViewArtist
        artistRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        //*display all song in database/
        mSongViewModel.songs.observe(viewLifecycleOwner, Observer { songs ->

//            songAdapter = SongAdapter(songs)
            songAdapter.songs = songs
            songRecyclerView.adapter = songAdapter

        })
        //*will display all artist in database/
        mArtistViewModel.artist.observe(viewLifecycleOwner, Observer { artists ->
            artistAdapter.artist = artists
            artistRecyclerView.adapter = artistAdapter

        })


    }

}