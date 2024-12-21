package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.FragmentMoreOptionBottomSheetBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreOptionBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMoreOptionBottomSheetBinding

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val args by navArgs<MoreOptionBottomSheetDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreOptionBottomSheetBinding.inflate(inflater)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.add_to_playlist -> {
                    val action = MoreOptionBottomSheetDialogFragmentDirections
                        .actionMoreOptionBottomSheetDialogFragmentToUserPlaylistBottomSheetDialogFragment(
                            args.userPlaylist.id
                        )
                    findNavController().navigate(action)

                    true
                }
                R.id.remove_from_playlist -> {
                    deleteSongFromPlaylist()
                    true
                }
                R.id.edit -> {
                    dismiss()
                    val bottomSheet = EditPlaylistBottomSheetDialog.newInstance(args.userPlaylist)
                    bottomSheet.show(parentFragmentManager, "EditPlaylistBottomSheetDialog")
                    true
                }
                else -> false
                }
        }

        return binding.root
    }

    private fun deleteSongFromPlaylist() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            val playlistId = args.userPlaylist.id
            mUserPlaylistViewModel.deleteUserPlaylist(playlistId)
            this.dismiss()

        }
        builder.setNegativeButton("No") { _, _ ->
        }

        builder.setTitle("Do you want to delete ${args.userPlaylist.title}")
        builder.setMessage("Are you sure to DELETE ${args.userPlaylist.title}")
        builder.show()

    }

}