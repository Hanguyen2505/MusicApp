package com.example.onlinemusicstreamapp.ui.fragments.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.UserPlaylistAdapter
import com.example.onlinemusicstreamapp.database.other.Constants.BOTTOM_SHEET_DIALOG
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBinding
import com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.library.LibraryBottomSheetFragment
import com.example.onlinemusicstreamapp.ui.viewmodel.PlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment(), MenuProvider {
    private val TAG = "dataSong"
    private lateinit var binding: FragmentLibraryBinding

    private val mPlaylistViewModel: PlaylistViewModel by viewModels()

    private val userPlaylistAdapter = UserPlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        subscribeToObserver()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.library_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add -> {
                Log.d("toolbar option menu", "add")
                val libraryBottomSheetFragment = LibraryBottomSheetFragment()
                libraryBottomSheetFragment.show(childFragmentManager, BOTTOM_SHEET_DIALOG)
                true
            }

            R.id.search -> {
                // Handle action for the second fragment's menu item
                true
            }

            else -> super.onOptionsItemSelected(menuItem)
        }
    }

    private fun subscribeToObserver() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)

        mPlaylistViewModel.userPlaylists.observe(viewLifecycleOwner) {
            userPlaylistAdapter.playlist = it
            recyclerView.adapter = userPlaylistAdapter
        }
    }
}