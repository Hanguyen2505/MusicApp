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
import androidx.lifecycle.Lifecycle
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LibraryFragment : Fragment(), MenuProvider {
    private val TAG = "dataSong"
    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getAllSongsAttributes() {
        val db = Firebase.firestore

        db.collection("playlist1")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.library_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add -> {
                Log.d("toolbar option menu", "add")
                val bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
                true
            }

            R.id.search -> {
                // Handle action for the second fragment's menu item
                true
            }

            else -> super.onOptionsItemSelected(menuItem)
        }
    }


}