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
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.UserPlaylistBottomSheetDialogFragment
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentUserPlaylistBinding

    private val args by navArgs<UserPlaylistFragmentArgs>()

    val BOTTOM_SHEET_TAG = "UserPlaylistBottomSheetDialogFragment"

    private val songAdapter = SongAdapter(false)

    private val mPlaylistViewModel: PlaylistViewModel by viewModels()

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

        binding.addToPlaylist.setOnClickListener {
//            val playlistId = args.userPlaylist.id
//            val bottomSheet = UserPlaylistBottomSheetDialogFragment()
//            bottomSheet.show(parentFragmentManager, BOTTOM_SHEET_TAG)

            val action = UserPlaylistFragmentDirections.actionUserPlaylistFragmentToUserPlaylistBottomSheetDialogFragment2(args.userPlaylist.id)
            findNavController().navigate(action)
        }

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun showPlaylistDetail() {
        binding.title.text = args.userPlaylist.title
        binding.userName.text = args.userPlaylist.userName
        Glide.with(binding.userPhoto).load(args.userPlaylist.userPhotoUrl).into(binding.userPhoto)
        Glide.with(binding.coverImg).load(args.userPlaylist.coverUrl).into(binding.coverImg)
    }

    private fun subscribeToObserver() {

        val recyclerView = binding.recyclerViewPlaylist
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mPlaylistViewModel.getSongsInUserPlaylist(args.userPlaylist.id).observe(viewLifecycleOwner) { songs ->
            songAdapter.updateData(songs)
            recyclerView.adapter = songAdapter
        }

    }

}