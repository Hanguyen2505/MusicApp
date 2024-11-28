package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.song

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlinemusicstreamapp.R

import com.example.onlinemusicstreamapp.databinding.FragmentSongBottomSheetDialogBinding

class MediaItemDialogFragment : BottomSheetDialogFragment() {

private var _binding: FragmentSongBottomSheetDialogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentSongBottomSheetDialogBinding.inflate(inflater, container, false)

        val navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_to_playlist -> {
                    // Handle Add to Playlist item click
                    true
                }
                R.id.remove_from_playlist -> {
                    // Handle Remove from Playlist item click
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