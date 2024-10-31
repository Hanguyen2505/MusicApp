package com.example.onlinemusicstreamapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.example.onlinemusicstreamapp.databinding.ActivityMainBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.PlayerControlViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel: PlayerControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setToolbar()

        setNavigationDrawer()

        showUserInNavigationView()

        replaceFragment()

        showCurrentSongPlaying()

        binding.playerView.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }

        binding.playerView

        binding.playPauseBtn.setOnClickListener {
            playerViewModel.playBackState.value.let { playbackState ->
                when(playbackState?.playbackState) {
                    PlaybackStateCompat.STATE_PLAYING -> {
                        Log.d("playbackStateInActivity", "${playbackState}")
                        binding.playPauseBtn.setImageResource(R.drawable.ic_pause_2)
                    }
                    PlaybackStateCompat.STATE_PAUSED -> {
                        Log.d("playbackStateInActivity", "$playbackState")
                        binding.playPauseBtn.setImageResource(R.drawable.ic_play_2)
                    }
                }
            }
            playerViewModel.togglePlayPause()
        }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun showCurrentSongPlaying() {
        playerViewModel.currentPlayingSong.observe(this) {
            if (it == null) return@observe
            binding.songTitle.text = it.description.title
            Glide.with(binding.songCover).load(it.description.iconUri).into(binding.songCover)
            binding.playerView.visibility = android.view.View.VISIBLE
        }
    }


    private fun setNavigationDrawer() {
        val drawerLayout = binding.main
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.historyFragment -> {
                    Log.d("item", "${item.itemId}")
                }
                R.id.settingFragment -> {
                    Log.d("item", "${item.itemId}")
                }
                R.id.logout -> {
                    UserAuthorization.signout()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun replaceFragment() {
        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?

        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            // Set up the BottomNavigationView with NavController
            setupWithNavController(bottomNavigationView, navController)
        } else {
            throw IllegalStateException("NavHostFragment not found")
        }
    }

    private fun showUserInNavigationView() {
        val header = binding.navView.getHeaderView(0)
        val profilePicture = header.findViewById<ImageView>(R.id.pf_picture)
        val name = header.findViewById<TextView>(R.id.user_name)

        val user = UserAuthorization.getCurrentUser()
        Glide.with(binding.imageButton).load(user?.photoUrl).into(binding.imageButton)

        Glide.with(profilePicture).load(user?.photoUrl).into(profilePicture)
        name.text = user?.displayName

    }


}
