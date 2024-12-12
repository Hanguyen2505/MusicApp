package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentUserPlaylistBottomSheetDialogBinding
import com.example.onlinemusicstreamapp.ui.fragments.album.UserPlaylistFragmentArgs
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPlaylistBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUserPlaylistBottomSheetDialogBinding

    private val mSongViewModel: SongViewModel by viewModels()
    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val songAdapter = SongAdapter(true)

    private val args by navArgs<UserPlaylistBottomSheetDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPlaylistBottomSheetDialogBinding.inflate(inflater)
        subscribeToObserver()

        mUserPlaylistViewModel.getSongsInUserPlaylist(args.playlistId).observe(viewLifecycleOwner) {
            songAdapter.updateAddedSongs(it)
        }


        songAdapter.setOnMoreButtonClickListener { song ->
            mUserPlaylistViewModel.addSongToPlaylist(args.playlistId, song.mediaId)
        }

        return binding.root
    }

    private fun subscribeToObserver() {

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mSongViewModel.songs.observe(viewLifecycleOwner) {
            songAdapter.updateData(it)
            recyclerView.adapter = songAdapter
        }
    }

}