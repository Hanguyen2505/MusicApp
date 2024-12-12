package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.databinding.FragmentBottomSheetNamePlaylistBinding
import com.example.onlinemusicstreamapp.ui.fragments.library.LibraryFragmentDirections
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetNamePlaylist : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetNamePlaylistBinding

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val mUserViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetNamePlaylistBinding.inflate(inflater)

        binding.createPlaylistBtn.setOnClickListener {
            val title = binding.namePlaylist.text.toString().trim()
            val desc = binding.description.text.toString().trim()

            val newDocumentId = mUserViewModel.getCurrentUserId() + (0..999999).random().toString()

            val userPlaylist = UserPlaylist(
                newDocumentId,
                title,
                desc,
                "",
                listOf(),
                mUserViewModel.getCurrentUserId(),
                mUserViewModel.getCurrentUserDisplayName(),
                mUserViewModel.getCurrentUserPhotoUrl()
            )
            mUserPlaylistViewModel.createPlaylist(userPlaylist)

            val action = LibraryFragmentDirections.actionLibraryFragmentToUserPlaylistFragment(userPlaylist)
            findNavController().navigate(action)

            dismiss()
        }
        return binding.root
    }

}