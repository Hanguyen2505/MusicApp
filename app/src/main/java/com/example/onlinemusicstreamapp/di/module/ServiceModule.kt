package com.example.onlinemusicstreamapp.di.module

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMusicDatabase() = MusicDatabase()

    @ServiceScoped
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ) = ExoPlayer.Builder(context).build()


}