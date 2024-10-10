package com.example.onlinemusicstreamapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.adapter.GenreAdapter
import com.example.onlinemusicstreamapp.databinding.FragmentSearchBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.GenreViewModel

class SearchFragment : Fragment(), MenuProvider {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGenreViewModel: GenreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        mGenreViewModel = ViewModelProvider(this)[GenreViewModel::class.java]

        binding.searchLinearLayout.setOnClickListener {
            goToSearchActivity()
        }

        //Menu Host
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        subscribeToObsever()

        return _binding!!.root
    }

    private fun subscribeToObsever() {
        //*a view where will show a list of genre
        val genreRecyclerView = binding.recyclerViewGenre
        genreRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )

        //*display all genres
        mGenreViewModel.genre.observe(viewLifecycleOwner, Observer { genre ->
            genre.forEach {
                Log.d("genre", "${it.name}")
            }

            val genreAdapter = GenreAdapter(genre)
            genreRecyclerView.adapter = genreAdapter
        })
    }

    private fun goToSearchActivity() {
//        val serachActivity = Intent(binding.root.context, SearchActivity::class.java)
//        startActivity(serachActivity)
        findNavController().navigate(R.id.action_searchFragment_to_searchViewFragment)

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.camera -> {

            }
            else -> {
            }
        }
        return false
    }

}