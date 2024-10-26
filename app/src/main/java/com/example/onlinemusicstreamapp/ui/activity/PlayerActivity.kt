package com.example.onlinemusicstreamapp.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.ActivityPlayerBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private lateinit var mSongViewModel: SongViewModel

    private var shouldUpdateSeekbar = true

    private val handler = Handler(Looper.getMainLooper())

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
        mSongViewModel = ViewModelProvider(this)[SongViewModel::class.java]

        binding.backToHomeBtn.setOnClickListener {
            finish()
        }

        binding.playPauseBtn.setOnClickListener {
            togglePlayPause()
        }



    }

    private fun togglePlayPause() {
        TODO("Not yet implemented")
    }

}