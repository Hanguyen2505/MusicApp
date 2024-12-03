package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentPlaylistBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    //ViewModel
    private val mPlaylistViewModel: PlaylistViewModel by viewModels()
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    //Adapter
    private val songAdapter = SongAdapter(false)

    //Safe Argument
    private val args by navArgs<PlaylistFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        showPlaylistDetail()
        subscribeToObserve()

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        songAdapter.setOnItemClickListener { song ->
            Log.d("CurrenSongSelected", "$song")
            mPlayerControlViewModel.play(song)
        }

        return binding.root
    }

    private fun subscribeToObserve() {
        val playlistRecyclerView = binding.recyclerViewPlaylist
        playlistRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mPlaylistViewModel.getSongsInPlaylist(args.playlist.playlistId).observe(viewLifecycleOwner) { songs ->
            songAdapter.updateData(songs)
            playlistRecyclerView.adapter = songAdapter
        }

    }

    private fun showPlaylistDetail() {
        binding.title.text = args.playlist.title
        Glide.with(binding.coverImg).load(args.playlist.coverImageUrl).into(binding.coverImg)
    }


}