package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.onlinemusicstreamapp.ui.viewmodel.LibraryViewModel
import com.example.onlinemusicstreamapp.databinding.FragmentBottomSheetNamePlaylistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetNamePlaylist : BottomSheetDialogFragment() {
    private val TAG = "create collection Path"
    private lateinit var binding: FragmentBottomSheetNamePlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myLibraryViewmodel = ViewModelProvider(this)[LibraryViewModel::class.java]
        binding = FragmentBottomSheetNamePlaylistBinding.inflate(inflater)
        val playlist = hashMapOf(
            "title" to binding.namePlaylist.text.toString()
        )
        binding.createPlaylistBtn.setOnClickListener {
            myLibraryViewmodel.createPlaylist(playlist, binding.namePlaylist.text.toString())
        }
        return binding.root
    }

}