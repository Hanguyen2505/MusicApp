package com.example.onlinemusicstreamapp.exoplayer.service.music


import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import android.util.Log
import androidx.annotation.OptIn
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_ARTIST_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_GENRE_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_ROOT_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.database.other.Constants.PLAYLIST
import com.example.onlinemusicstreamapp.database.other.Constants.SERVICE_TAG
import com.example.onlinemusicstreamapp.database.other.Constants.SONG_DURATION
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MusicQueueManager
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseArtistSource
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseGenreSource
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseMusicSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MusicService: MediaBrowserServiceCompat() {

    private var curPlayingSong: MediaMetadataCompat? = null

    private lateinit var mediaSession: MediaSessionCompat

    private var updateCurrentTimeJob: Job? = null

    private var songCurrentTime = 0L

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var mMusicQueueManager: MusicQueueManager

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    @Inject
    lateinit var firebaseArtistSource: FirebaseArtistSource

    @Inject
    lateinit var firebaseGenreSource: FirebaseGenreSource

    companion object {
        var songDuration = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Set up MediaSession
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setCallback(mediaSessionCallback)
            setSessionToken(sessionToken)
            isActive = true
        }

        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
            firebaseArtistSource.fetchArtistData()
            firebaseGenreSource.fetchMediaData()
            mMusicQueueManager.prepareQueue()
            mediaSession.setQueue(mMusicQueueManager.getQueue())
            mediaSession.setQueueTitle(PLAYLIST)
            Log.d("MUSIC_QUEUE_SONG", "${mMusicQueueManager.getQueue()}")
        }

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == STATE_PLAYING) {
                    songDuration = exoPlayer.duration
                    Log.d("songDuration", "Duration: $songDuration ms")
                }
            }
        })

        startUpdatingCurrentTime()
    }


    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)

            if (firebaseMusicSource.songs.isNotEmpty()) {
                val song = firebaseMusicSource.songs.find {
                    it.description.mediaId == mediaId
                }
                song?.let {
                    curPlayingSong = it
                    mMusicQueueManager.setQueueIndex(firebaseMusicSource.songs.indexOf(it))
                    Log.d("MUSIC_QUEUE_SONG_INDEX", "${mMusicQueueManager.getCurrentQueueItem()}")
                    startPlaying(it)
                }
                Log.d("currentPlayingSong", "${curPlayingSong?.description?.title}")

            }
        }

        override fun onPlay() {
            super.onPlay()
            exoPlayer.play()
            setPlaybackState(STATE_PLAYING)
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            mMusicQueueManager.getNextQueueItem()?.let {
                skipToOnQueueItem(it.description.mediaId.toString())
            }
        }

        override fun onPause() {
            exoPlayer.pause()
            setPlaybackState(STATE_PAUSED)
            Log.d("playbackCurrentState", "PLAYBACK STATE PAUSE: $")
            Log.d("MUSIC_QUEUE", "${mMusicQueueManager.getQueue()}")
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            mMusicQueueManager.getPreviousQueueItem()?.let {
                skipToOnQueueItem(it.description.mediaId.toString())
            }
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            exoPlayer.seekTo(pos)
        }

        override fun onStop() {
            exoPlayer.stop()
            setPlaybackState(STATE_STOPPED)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // Provide the root of the media tree for browsing
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    //*no data showing [firebaseMusicSource.asMediaItems()]
    /* 1.Ensure fetchMediaData() is called before onLoadChildren.
       2.Verify that the fetched songs are not empty.
       3.Log onLoadChildren calls to ensure the correct parentId is used.
       4.Use result.detach() and sendResult() after loading data asynchronously.
       5.Make sure MusicDatabase is returning the expected data.*/

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Timber.tag("onLoadChildren").d("parentId: %s", parentId)
        result.detach()
        when(parentId) {

            MEDIA_SONG_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseMusicSource.songs.isEmpty()) {
                        firebaseMusicSource.fetchMediaData()
                    }
                    val mediaItems = firebaseMusicSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }

            }

            MEDIA_ARTIST_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseArtistSource.artists.isEmpty()) {
                        firebaseArtistSource.fetchArtistData()
                    }
                    val mediaItems = firebaseArtistSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }
            }

            MEDIA_GENRE_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseGenreSource.genres.isEmpty()) {
                        firebaseGenreSource.fetchMediaData()
                    }
                    val mediaItems = firebaseGenreSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun startPlaying(mediaItem: MediaMetadataCompat) {
//        playSong(mediaItem)
        mediaSession.setMetadata(mediaItem)
        exoPlayer.apply {
            setMediaSource(getMediaSource(asMediaItemExo(mediaItem)))
            playWhenReady = true
            prepare()
            play()
        }
        setPlaybackState(STATE_PLAYING)
    }

    private fun asMediaItemExo(song: MediaMetadataCompat): MediaItem {
        return MediaItem.fromUri(song.description.mediaUri.toString())
    }

    private fun setPlaybackState(state: Int) {
        val playbackState = Builder()
            .setActions(
                ACTION_PLAY or
                ACTION_PAUSE or
                ACTION_REWIND or
                ACTION_SKIP_TO_NEXT or
                ACTION_SKIP_TO_PREVIOUS or
                ACTION_SKIP_TO_QUEUE_ITEM or
                ACTION_STOP
            )
            .setState(state, exoPlayer.currentPosition, 1f)
            .setExtras(Bundle().apply {
                putLong(SONG_DURATION, exoPlayer.duration) // Add song duration
            })
            .build()
        mediaSession.setPlaybackState(playbackState)
    }

    fun startUpdatingCurrentTime() {
        // Launch a coroutine to update the songCurrentTime every second
        updateCurrentTimeJob = serviceScope.launch {
            while (isActive) {
                songCurrentTime = exoPlayer.currentPosition
                songDuration = exoPlayer.duration
                setPlaybackState(
                    when {
                        exoPlayer.isPlaying -> STATE_PLAYING

                        exoPlayer.playbackState == Player.STATE_BUFFERING -> STATE_BUFFERING

                        exoPlayer.playbackState == Player.STATE_IDLE -> STATE_NONE

                        songCurrentTime < songDuration -> STATE_PAUSED

                        else -> STATE_STOPPED

                    }
                )
                Log.d("CurrentPosition", "Current time: ${songCurrentTime} ms")
                Log.d("songDurationService", "Current time: ${songDuration} ms")
                Log.d("play_when_ready", "${exoPlayer.playWhenReady}")
                Log.d("exoPlayer IsPlaying", "isPlaying : ${exoPlayer.isPlaying}")
                Log.d("ExoPlayer State", "isPlaying : ${exoPlayer.playbackState}")
                delay(1000L) // Update every second
            }
        }
    }

    private fun skipToOnQueueItem(mediaId: String) {
        val mediaMetadata = mMusicQueueManager.getMediaMetadataForId(mediaId)
        mediaSession.setMetadata(mediaMetadata)
        mediaMetadata?.let {
            startPlaying(it)
        }
    }

    @OptIn(UnstableApi::class)
    private fun getMediaSource(mediaItem: MediaItem): MediaSource {
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        return mediaSource
    }

}