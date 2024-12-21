package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.adapter.UserPlaylistSelectionAdapter
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.databinding.FragmentAddToPlaylistBottomSheetDialogBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddToPlaylistBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddToPlaylistBottomSheetDialogBinding

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val userPlaylistSelectionAdapter = UserPlaylistSelectionAdapter()

    companion object {
        private const val ARG_SONG = "song"

        fun newInstance(song: Song) = AddToPlaylistBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SONG, song)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToPlaylistBottomSheetDialogBinding.inflate(
            inflater
        )
        subscribeToObserver()

        val songArgs = requireArguments().getParcelable<Song>(ARG_SONG)

        mUserPlaylistViewModel.getUserPlaylistsBySongId(songArgs!!.mediaId).observe(viewLifecycleOwner) {
            it.forEach { playlist ->
                Log.d("playlistbysong", "Added ${playlist.title}")
            }

            userPlaylistSelectionAdapter.addPlaylist(it)
        }

        userPlaylistSelectionAdapter.setOnAddButtonClickListener {
            addSongToUserPlaylist(songArgs,it)
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

        mUserPlaylistViewModel.userPlaylists.observe(viewLifecycleOwner) {
            userPlaylistSelectionAdapter.updateData(it)
            recyclerView.adapter = userPlaylistSelectionAdapter
        }
    }

    private fun addSongToUserPlaylist(song: Song, userPlaylist: UserPlaylist) {
        Log.d("addSongToPlaylist", "Added ${song.title} to ${userPlaylist.title}")
        mUserPlaylistViewModel.addSongToPlaylist(userPlaylist.id, song.mediaId)
    }

}