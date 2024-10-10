package com.example.onlinemusicstreamapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.other.State.STATE_INITIALIZED
import com.example.onlinemusicstreamapp.database.other.State.STATE_INITIALIZING
import com.example.onlinemusicstreamapp.database.other.State.STATE_PAUSE
import com.example.onlinemusicstreamapp.exoplayer.MusicService
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

    private val updateSeekBarRunnable = object: Runnable {
        override fun run() {
            if (shouldUpdateSeekbar) {
                val currentPosition = MusicService.getInstance()!!.currentPosition
                Log.d("currentPosition", "${dateFormat.format(currentPosition)}")
                val timeDuration = MusicService.getTimeDuration()
                binding.seekBar.progress = currentPosition.toInt()
                binding.tvSongCurrentTime.text = dateFormat.format(currentPosition)
                binding.tvSongDurationLeft.text = "-" + dateFormat.format(
                    timeDuration - currentPosition
                )
            }
            handler.postDelayed(this, 1000) // Update every second
        }
    }
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

        showPlayerView()
        updateTimeDuration()
        setUpSeekBar()

        binding.backToHomeBtn.setOnClickListener {
            finish()
        }

        binding.playPauseBtn.setOnClickListener {
            togglePlayPause()
        }


    }

    //this function only show the change when you try to interact with the seek bar like tapping or dragging the thumb
    private fun setUpSeekBar() {

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicService.getInstance()!!.seekTo(progress.toLong())
                    binding.tvSongCurrentTime.text = dateFormat.format(progress.toLong())
                    binding.tvSongDurationLeft.text = "-" + dateFormat.format(
                        MusicService.getTimeDuration() - progress.toLong()
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = true

            }
        })

    }

    private fun showPlayerView() {
        MusicService.getCurrentSong()?.apply {
            binding.songTitle1.text = this.title
            binding.songTitle2.text = this.title
            binding.artistName.text = this.artist.toString()
            Glide.with(binding.songCover).load(this.imageUrl).into(binding.songCover)
        }
    }

    private fun togglePlayPause() {
        Log.d("player", "${MusicService!!.getState()}")
        binding.playPauseBtn.setImageResource(
            if (MusicService.getState() == STATE_INITIALIZED || MusicService.getState() == STATE_INITIALIZING)
                R.drawable.ic_play
            else if (MusicService.getState() == STATE_PAUSE)
                R.drawable.ic_pause
            else -1
        )

        MusicService.musicSerViceListener()
    }

    private fun updateTimeDuration() {
        MusicService.getInstance().addListener(object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding.tvSongDurationLeft.text = dateFormat.format(MusicService.getTimeDuration())
                    binding.seekBar.max = MusicService.getTimeDuration().toInt()
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    handler.post(updateSeekBarRunnable)
                    Log.d("shouldUpdateSeekbar", "$shouldUpdateSeekbar")
                }
                else
                    handler.removeCallbacks(updateSeekBarRunnable)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        shouldUpdateSeekbar = true
        handler.post(updateSeekBarRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

}