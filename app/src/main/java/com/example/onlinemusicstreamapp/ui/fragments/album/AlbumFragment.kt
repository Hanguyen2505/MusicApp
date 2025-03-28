package com.example.onlinemusicstreamapp.ui.fragments.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.adapter.SongAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentAlbumBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.ArtistViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

    //ViewModel
    private val mArtistViewModel: ArtistViewModel by viewModels()
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    //Adapter
    private val songAdapter = SongAdapter(false)

    //Safe Argument
    private val args by navArgs<AlbumFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        showAlbumDetail()
        subscribeToObserve()

        mPlayerControlViewModel.sendArtistToMusicService(args.artist.name)

        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        songAdapter.setOnItemClickListener { song ->
            Log.d("CurrenSongSelected", "$song")
            mPlayerControlViewModel.play(song)
        }

        return binding.root
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
            songAdapter.updateData(songs)
            albumRecyclerView.adapter = songAdapter
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}