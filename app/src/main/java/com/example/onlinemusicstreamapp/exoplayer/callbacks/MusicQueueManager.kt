package com.example.onlinemusicstreamapp.exoplayer.callbacks

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseMusicSource
import javax.inject.Inject

class MusicQueueManager @Inject constructor(
    private val firebaseMusicSource: FirebaseMusicSource
) {

    var currentQueueIndex: Int = 0

    private val queueItems = mutableListOf<MediaSessionCompat.QueueItem>()

    suspend fun prepareQueue() {
        firebaseMusicSource.fetchMediaData()
        queueItems.clear()
        queueItems.addAll(
            firebaseMusicSource.songs.mapIndexed { index, song ->
                MediaSessionCompat.QueueItem(song.description, index.toLong())
            }
        )
        Log.d("MUSIC_QUEUE_SONG_QUEUE", "${getQueue()}")
    }

    fun getPreviousQueueItem(): MediaSessionCompat.QueueItem? {
        currentQueueIndex = if (currentQueueIndex > 0) currentQueueIndex - 1 else queueItems.size - 1
        return queueItems.getOrNull(currentQueueIndex)
    }


    fun getCurrentQueueItem(): MediaSessionCompat.QueueItem? {
        return queueItems.getOrNull(currentQueueIndex)
    }


    fun getNextQueueItem(): MediaSessionCompat.QueueItem? {
        currentQueueIndex = (currentQueueIndex + 1) % queueItems.size
        return queueItems.getOrNull(currentQueueIndex)
    }

    //set the queue index to a specific position
    fun setQueueIndex(index: Int) {
        if (index in queueItems.indices) {
            currentQueueIndex = index
        }
    }

    fun getMediaMetadataForId(mediaId: String?): MediaMetadataCompat? {
        // This would be where you retrieve the metadata from your source, like Firebase
        return firebaseMusicSource.songs.find { it.description.mediaId == mediaId }
    }

    fun getQueueItemSize(): Int {
        return queueItems.size
    }

    //return the full queue list
    fun getQueue(): List<MediaSessionCompat.QueueItem> = queueItems
}