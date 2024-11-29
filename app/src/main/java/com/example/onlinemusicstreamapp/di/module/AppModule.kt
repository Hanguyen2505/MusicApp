package com.example.onlinemusicstreamapp.di.module

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.remote.UserPlaylistDatabase
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(UnstableApi::class)
    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)

    @Singleton
    @Provides
    fun provideUserPlaylistDatabase() = UserPlaylistDatabase()

    @Singleton
    @Provides
    fun provideFireAuth() = FirebaseAuth.getInstance()


}