package com.example.onlinemusicstreamapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.ArtistAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentHomeBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSongViewModel: SongViewModel
    private lateinit var mArtistViewModel: ArtistViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        mSongViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        mArtistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
        subscribeToObserver()
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
            val songAdapter = SongAdapter(songs)
            songRecyclerView.adapter = songAdapter

        })
        //*will display all artist in database/
        mArtistViewModel.artist.observe(viewLifecycleOwner, Observer { artists ->
            val artistAdapter = ArtistAdapter(artists, "HomeFragment")
            artistRecyclerView.adapter = artistAdapter

        })
    }

}