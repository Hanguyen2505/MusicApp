package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.FragmentMediaItemInUserPlaylistDialogBinding
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.AddToPlaylistBottomSheetDialogFragment
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaItemInUserPlaylistDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMediaItemInUserPlaylistDialogBinding

    private val args by navArgs<MediaItemInUserPlaylistDialogFragmentArgs>()

    private val mArtistViewModel: ArtistViewModel by viewModels()
    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
   ): View {
        binding = FragmentMediaItemInUserPlaylistDialogBinding.inflate(inflater, container, false)

        binding.songTitle.text = args.song.title
        binding.desc.text = args.song.artist.joinToString(", ")
        Glide.with(binding.songImage).load(args.song.imageUrl).into(binding.songImage)
        binding.moreBtn.visibility = View.GONE

        val navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_to_playlist -> {
//                    val action = MediaItemDialogFragmentDirections.actionMediaItemDialogFragmentToAddToPlaylistBottomSheetDialogFragment(args.song)
//                    requireParentFragment().findNavController().navigate(action)
                    dismiss()
                    val bottomSheet = AddToPlaylistBottomSheetDialogFragment.newInstance(args.song)
                    bottomSheet.show(parentFragmentManager, "AddToPlaylistBottomSheetDialogFragment")
                    true
                }
                R.id.delete_from_playlist -> {
                    mUserPlaylistViewModel.removeSongFromPlaylist(args.userPlaylist.id, args.song.mediaId)
                    dismiss()
                    true
                }

                R.id.view_artist -> {
                    mArtistViewModel.getArtistById(args.song.artist.joinToString(", ")).observe(viewLifecycleOwner) {
                        val action = MediaItemInUserPlaylistDialogFragmentDirections
                            .actionMediaItemInUserPlaylistDialogFragmentToAlbumFragment(it)
                        findNavController().navigate(action)
                    }
                    true
                }
                else -> {
                    false
                }
            }

        }

        return binding.root
    }

}