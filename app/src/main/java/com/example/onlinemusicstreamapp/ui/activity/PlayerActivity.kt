package com.example.onlinemusicstreamapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.other.Constants.SONG_DURATION
import com.example.onlinemusicstreamapp.databinding.ActivityPlayerBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import com.example.onlinemusicstreamapp.ui.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private var updateSeekBar = true

    //ViewModel
    private val mPlayerControlViewModel: PlayerControlViewModel by viewModels()

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private lateinit var animator: Animator

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
        updateSeekBar()

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurrentTime(progress.toLong())
                    mPlayerControlViewModel.seekTo(progress.toLong())
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateSeekBar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateSeekBar = true
            }
        })

        binding.backToHomeBtn.setOnClickListener {
            finish()
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//            startActivity(intent)

        }

        binding.playPauseBtn.setOnClickListener {
            mPlayerControlViewModel.togglePlayPause()
            updateSeekBar()

        }

    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            
        }
    }

    override fun onResume() {
        super.onResume()
        updateSeekBar()
    }

    private fun displaySongInfo() {
        mPlayerControlViewModel.currentPlayingSong.observe(this) {
            if (it == null) return@observe
            binding.songTitle1.text = it.description.title
            binding.songTitle2.text = it.description.title
            binding.artistName.text = it.description.subtitle
            Glide.with(binding.songCover).load(it.description.iconUri).into(binding.songCover)
        }

        mPlayerControlViewModel.playBackState.observe(this) {
            if (it!!.state == STATE_PLAYING) {

                binding.playPauseBtn.setBackgroundResource(R.drawable.ic_pause)
            }
            else {
                binding.playPauseBtn.setBackgroundResource(R.drawable.ic_play)
            }
        }
    }

    private fun updateSeekBar() {
        mPlayerControlViewModel.playBackState.observe(this) {
            if (updateSeekBar) {
                setCurrentTime(it!!.position)
                binding.seekBar.progress = it.position.toInt()
                Log.d("currentPlaybackPosition", "${it.position}")
            }
        }
        mPlayerControlViewModel.playBackState.observe(this) {
            val duration = it?.extras?.getLong(SONG_DURATION)
            binding.tvSongDurationLeft.text = dateFormat.format(duration)
            binding.seekBar.max = duration!!.toInt()
        }
    }

    private fun setCurrentTime(ms: Long) {
        binding.tvSongCurrentTime.text = dateFormat.format(ms)
    }
}