package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentUserPlaylistBottomSheetDialogBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPlaylistBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUserPlaylistBottomSheetDialogBinding

    private val mSongViewModel: SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPlaylistBottomSheetDialogBinding.inflate(inflater)
        subscribeToObserver()

        return binding.root
    }

    private fun subscribeToObserver() {

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mSongViewModel.songs.observe(viewLifecycleOwner) {
            val songAdapter = SongAdapter(true)
            songAdapter.updateData(it)
            recyclerView.adapter = songAdapter
        }
    }

}