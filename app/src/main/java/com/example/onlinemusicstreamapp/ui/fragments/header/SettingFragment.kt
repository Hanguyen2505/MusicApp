package com.example.onlinemusicstreamapp.ui.fragments.header

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.FragmentSettingBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SettingFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.navigationView.setNavigationItemSelectedListener(this)

//        binding.navigationView.setNavigationItemSelectedListener { p0 ->
//            val itemId = p0.itemId
//
//            when (itemId) {
//                R.id.logout -> {
//                    findNavController().navigate(R.id.action_settingFragment_to_signInActivity)
//                    Log.d("itemMenu", "$itemId")
//                    true
//                }
//                else -> false
//            }
//        }

        return binding.root
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val itemId = p0.itemId

        when (itemId) {
            R.id.logout -> {
                firebaseAuth.signOut()
                findNavController().navigate(R.id.action_settingFragment_to_signInActivity)
                Log.d("itemMenu", "$itemId")
            }
        }
        return true
    }

}