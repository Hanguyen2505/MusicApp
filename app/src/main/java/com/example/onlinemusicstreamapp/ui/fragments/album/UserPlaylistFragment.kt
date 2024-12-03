package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.databinding.FragmentUserPlaylistBinding
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.UserPlaylistBottomSheetDialogFragment


class UserPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentUserPlaylistBinding

    private val args by navArgs<UserPlaylistFragmentArgs>()

    val BOTTOM_SHEET_TAG = "UserPlaylistBottomSheetDialogFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPlaylistBinding.inflate(inflater, container, false)
        showPlaylistDetail()

        binding.addToPlaylist.setOnClickListener {
            val bottomSheet = UserPlaylistBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, BOTTOM_SHEET_TAG)
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

}