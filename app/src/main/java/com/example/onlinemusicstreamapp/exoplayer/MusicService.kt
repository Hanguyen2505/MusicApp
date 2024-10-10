package com.example.onlinemusicstreamapp.exoplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.State
import com.example.onlinemusicstreamapp.database.other.State.STATE_CREATED
import com.example.onlinemusicstreamapp.database.other.State.STATE_ENDED
import com.example.onlinemusicstreamapp.database.other.State.STATE_INITIALIZED
import com.example.onlinemusicstreamapp.database.other.State.STATE_INITIALIZING
import com.example.onlinemusicstreamapp.database.other.State.STATE_PAUSE
import com.example.onlinemusicstreamapp.database.other.State.STATE_READY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
object MusicService {
//    private var exoPlayer: ExoPlayer? = null
    private var currentSong: Song? = null
    private var state: State = STATE_CREATED

    @Inject
    lateinit var exoPlayer: ExoPlayer

    fun getCurrentSong(): Song? {
        return currentSong
    }

    fun getInstance(): ExoPlayer {
        return exoPlayer!!
    }

    fun getState(): State {
        return state
    }

    fun getTimeDuration(): Long {
        return exoPlayer.duration
    }

    fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }

    fun startPlaying(context: Context, song: Song) {
//        if (exoPlayer == null) {
//            exoPlayer = ExoPlayer.Builder(context).build()
//            state = STATE_READY
//            Log.d("State", "$state")
//        }
        if (currentSong != song) {
            currentSong = song
            currentSong?.songUrl?.apply {
                Log.d("currentSong", "$this")
                val mediaItem = MediaItem.fromUri(Uri.parse(this))
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.playWhenReady = true
                exoPlayer?.prepare()
                exoPlayer?.play()
                state = STATE_INITIALIZED
                Log.d("State", "$state")
            }

        }
    }

    fun musicSerViceListener() {
        if (exoPlayer!!.isPlaying) {
            state = STATE_PAUSE
            exoPlayer!!.pause()
        } else {
            state = STATE_INITIALIZING
            exoPlayer!!.play()
        }
    }
}