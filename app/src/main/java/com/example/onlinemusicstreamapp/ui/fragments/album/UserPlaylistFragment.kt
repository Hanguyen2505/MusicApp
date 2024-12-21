package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentUserPlaylistBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentUserPlaylistBinding

    private val args by navArgs<UserPlaylistFragmentArgs>()

    private val songAdapter = SongAdapter(false)

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPlaylistBinding.inflate(inflater, container, false)

        showData(
            args.userPlaylist.title,
            args.userPlaylist.userName,
            args.userPlaylist.userPhotoUrl,
            args.userPlaylist.coverUrl
        )

        updateDataBySubscribeToObserver()
        subscribeToObserver()

        checkIfThisPlaylistIsDeletedOrNot()

        songAdapter.setOnItemClickListener {
            mPlayerControlViewModel.play(it)
        }

        songAdapter.setOnMoreButtonClickListener {
            val action = UserPlaylistFragmentDirections
                .actionUserPlaylistFragmentToMediaItemInUserPlaylistDialogFragment(it, args.userPlaylist)
            findNavController().navigate(action)
        }

        binding.addToPlaylist.setOnClickListener {
            val action = UserPlaylistFragmentDirections
                .actionUserPlaylistFragmentToUserPlaylistBottomSheetDialogFragment(args.userPlaylist.id)
            findNavController().navigate(action)
        }

        //TODO set delete visible
        binding.moreBtn.setOnClickListener {
            val action = UserPlaylistFragmentDirections
                .actionUserPlaylistFragmentToMoreOptionBottomSheetDialogFragment(args.userPlaylist)
            findNavController().navigate(action)
        }

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun showData(title: String, userName: String, userPhotoUrl: String, coverUrl: String) {
        binding.title.text = title
        binding.userName.text = userName
        Glide.with(binding.userPhoto).load(userPhotoUrl).into(binding.userPhoto)
        if (coverUrl.isNotEmpty()) {
            Glide.with(binding.coverImg)
                .load(coverUrl)
                .centerCrop()
                .into(binding.coverImg)
        }
        else
            binding.coverImg.setImageResource(R.drawable.ic_simple_music_note)
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

    private fun updateDataBySubscribeToObserver() {
        mUserPlaylistViewModel.userPlaylists.observe(viewLifecycleOwner) { playlists ->
            val userPlaylist = playlists.find {
                it.id == args.userPlaylist.id
            }
            userPlaylist?.let { showData(it.title, userPlaylist.userName, userPlaylist.userPhotoUrl, userPlaylist.coverUrl) }
        }

    }

    private fun subscribeToObserver() {

        val recyclerView = binding.recyclerViewPlaylist
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mUserPlaylistViewModel.getSongsInUserPlaylist(
            args.userPlaylist.id
        ).observe(
            viewLifecycleOwner
        ) { songs ->
            songAdapter.updateData(songs)
            recyclerView.adapter = songAdapter
        }

    }

}