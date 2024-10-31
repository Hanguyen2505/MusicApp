package com.example.onlinemusicstreamapp.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.ActivityPlayerBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val mSongViewModel: SongViewModel by viewModels()
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        displaySongInfo()

        binding.backToHomeBtn.setOnClickListener {
            finish()
        }

        binding.playPauseBtn.setOnClickListener {
            mPlayerControlViewModel.togglePlayPause()

        }

    }

    private fun displaySongInfo() {
        mPlayerControlViewModel.currentPlayingSong.observe(this) {
            if (it == null) return@observe
            binding.songTitle1.text = it.description.title
            binding.songTitle2.text = it.description.title
            binding.artistName.text = it.description.subtitle
            Glide.with(binding.songCover).load(it.description.iconUri).into(binding.songCover)
        }
    }
}