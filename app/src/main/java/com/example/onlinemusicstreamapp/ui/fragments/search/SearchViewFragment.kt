package com.example.onlinemusicstreamapp.ui.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.ArtistAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentSearchViewBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel


class SearchViewFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentSearchViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var mArtistViewModel: ArtistViewModel
    private lateinit var mSongViewModel: SongViewModel
    private var songAdapter = SongAdapter(emptyList())
    private var artistAdapter = ArtistAdapter(emptyList(), "SearchViewFragment")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchViewBinding.inflate(inflater, container, false)

        //ViewModel Provider
        mSongViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        mArtistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]

        subscribeSearchView()
        binding.cancel.setOnClickListener {
            findNavController().navigate(R.id.action_searchViewFragment_to_searchFragment)
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

        mArtistViewModel.searchArtist(query).observe(this) {list ->
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