package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LibraryBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLibraryBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBottomSheetBinding.inflate(inflater, container, false)
        binding.createPlaylist.setOnClickListener {
            val bottomSheetFragment = BottomSheetNamePlaylist()
            bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
        }
        return binding.root
    }

}