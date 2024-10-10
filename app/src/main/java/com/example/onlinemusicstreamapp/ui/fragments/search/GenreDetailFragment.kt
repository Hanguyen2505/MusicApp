package com.example.onlinemusicstreamapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.GenreAdapter
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentGenreDetailBinding
import com.example.onlinemusicstreamapp.ui.fragments.album.AlbumFragmentArgs
import com.example.onlinemusicstreamapp.ui.viewmodel.GenreViewModel


class GenreDetailFragment : Fragment() {

    private var _binding: FragmentGenreDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGenreViewModel: GenreViewModel

    private val args by navArgs<GenreDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreDetailBinding.inflate(inflater, container, false)
        mGenreViewModel = ViewModelProvider(this)[GenreViewModel::class.java]
        binding.backToSearchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_genreDetailFragment_to_searchFragment)
        }

        Log.d("NameTAG", "${args.genre.name}")

        subscribeToObsever()

        return binding.root
    }

    private fun subscribeToObsever() {

        //*a view where will show a list of song base on its genre
        val songRecyclerView = binding.recyclerViewSong
        songRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        //*display all songs
        mGenreViewModel.searchSong(args.genre.name).observe(viewLifecycleOwner, Observer { song ->
            song.forEach {
                Log.d("genre", "${it.title}")
            }
            song.let {
                songRecyclerView.adapter = SongAdapter(it)
            }

        })
    }

}