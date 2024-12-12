package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
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
import com.example.onlinemusicstreamapp.databinding.FragmentUserPlaylistBinding
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.MoreOptionBottomSheetDialogFragment
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentUserPlaylistBinding

    private val args by navArgs<UserPlaylistFragmentArgs>()

    val BOTTOM_SHEET_TAG = "UserPlaylistBottomSheetDialogFragment"

    private val songAdapter = SongAdapter(false)

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPlaylistBinding.inflate(inflater, container, false)
        showPlaylistDetail()
        subscribeToObserver()

        checkIfThisPlaylistIsDeletedOrNot()

        binding.addToPlaylist.setOnClickListener {
            val action = UserPlaylistFragmentDirections.actionUserPlaylistFragmentToUserPlaylistBottomSheetDialogFragment(args.userPlaylist.id)
            findNavController().navigate(action)
        }

        binding.moreBtn.setOnClickListener {
            val action = UserPlaylistFragmentDirections.actionUserPlaylistFragmentToMoreOptionBottomSheetDialogFragment(args.userPlaylist)
            findNavController().navigate(action)
        }

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun checkIfThisPlaylistIsDeletedOrNot() {
        mUserPlaylistViewModel.userPlaylists.observe(viewLifecycleOwner) { playlists ->
            val playlist = playlists.find { playlist ->
                playlist.id == args.userPlaylist.id
            }
            if (playlist == null) {
                findNavController().popBackStack()
            }
        }
    }

    private fun showPlaylistDetail() {
        mUserPlaylistViewModel.userPlaylists.observe(viewLifecycleOwner) { playlists ->
            val userPlaylist = playlists.find {
                it.id == args.userPlaylist.id
            }
            binding.title.text = userPlaylist?.title
            binding.userName.text = userPlaylist?.userName
            Glide.with(binding.userPhoto).load(args.userPlaylist.userPhotoUrl).into(binding.userPhoto)
            Glide.with(binding.coverImg).load(args.userPlaylist.coverUrl).into(binding.coverImg)
        }

    }

    private fun subscribeToObserver() {

        val recyclerView = binding.recyclerViewPlaylist
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mUserPlaylistViewModel.getSongsInUserPlaylist(args.userPlaylist.id).observe(viewLifecycleOwner) { songs ->
            songAdapter.updateData(songs)
            recyclerView.adapter = songAdapter
        }

    }

}