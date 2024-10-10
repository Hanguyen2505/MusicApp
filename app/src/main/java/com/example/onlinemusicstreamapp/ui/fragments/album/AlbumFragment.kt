package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentAlbumBinding
import com.example.onlinemusicstreamapp.exoplayer.MusicService
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//TODO display the song list in album in the first time create this fragment
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var mArtistViewModel: ArtistViewModel

    private val args by navArgs<AlbumFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        mArtistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
        showAlbumDetail()
        subscribeToObserve()

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.followButton.setOnClickListener {

        }

        binding.playBtn.setOnClickListener {
            playAlbum()
        }

        return binding.root
    }

    private fun playAlbum() {

    }

    private fun showAlbumDetail() {
        binding.artistName.text = args.artist.name
        Glide.with(binding.prfImage).load(args.artist.imageUrl).into(binding.prfImage)
        Log.d("SongString", "${args.artist.name}")
    }

    private fun subscribeToObserve() {
        val albumRecyclerView = binding.recyclerViewAlbum
        albumRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        Log.d("songlistLiveData", "${args.artist.name}")
        mArtistViewModel.getSong(args.artist.name).observe(viewLifecycleOwner, Observer { songs ->
            val songAdapter = SongAdapter(songs)
            albumRecyclerView.adapter = songAdapter
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}