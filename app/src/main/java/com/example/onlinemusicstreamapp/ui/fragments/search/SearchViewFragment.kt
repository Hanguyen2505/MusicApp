package com.example.onlinemusicstreamapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.ArtistAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentSearchViewBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchViewFragment : Fragment(), SearchView.OnQueryTextListener {
    //Binding
    private var _binding: FragmentSearchViewBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val mArtistViewModel: ArtistViewModel by viewModels()
    private val mSongViewModel: SongViewModel by viewModels()
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    //Adapter
    private var songAdapter = SongAdapter(false)
    private var artistAdapter = ArtistAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchViewBinding.inflate(inflater, container, false)

        subscribeSearchView()
        binding.cancel.setOnClickListener {
            findNavController().navigate(R.id.action_searchViewFragment_to_searchFragment)
        }

        songAdapter.setOnItemClickListener { song ->
            mPlayerControlViewModel.play(song)
        }

        artistAdapter.setOnItemClickListener { artist ->
            val action = SearchViewFragmentDirections.actionSearchViewFragmentToAlbumFragment(artist)
            findNavController().navigate(action)
        }

        return _binding!!.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchDatabase(newText)
        }
        return true
    }

    private fun subscribeSearchView() {
        val searchView = binding.searchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    private fun searchDatabase(query: String) {
        val searchRecyclerView = binding.searchRecyclerView
        searchRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mArtistViewModel.filterSongsByArtist(query).observe(this) { list ->
            list.let {
                artistAdapter.updateData(it)
            }
        }

        mSongViewModel.searchMusic(query).observe(this) { list ->
            list.let {
                songAdapter.updateData(it)
            }
        }

        searchRecyclerView.adapter = ConcatAdapter(artistAdapter, songAdapter)
    }

}