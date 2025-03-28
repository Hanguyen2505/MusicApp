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
import com.example.onlinemusicstreamapp.databinding.FragmentSongBottomSheetDialogBinding
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.AddToPlaylistBottomSheetDialogFragment
import com.example.onlinemusicstreamapp.ui.fragments.home.HomeFragment
import com.example.onlinemusicstreamapp.ui.fragments.home.HomeFragmentDirections
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaItemDialogFragment : BottomSheetDialogFragment() {

private var _binding: FragmentSongBottomSheetDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<MediaItemDialogFragmentArgs>()

    private val mArtistViewModel: ArtistViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentSongBottomSheetDialogBinding.inflate(inflater, container, false)

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
                //TODO try to use MediaItemDialogFragmentDirections instead
                R.id.view_artist -> {
                    mArtistViewModel.getArtistById(args.song.artist.joinToString(", ")).observe(viewLifecycleOwner) {
                        val action = MediaItemDialogFragmentDirections.actionMediaItemDialogFragmentToAlbumFragment(it)
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



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}