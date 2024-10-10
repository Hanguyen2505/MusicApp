package com.example.onlinemusicstreamapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.User
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.example.onlinemusicstreamapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.createOne.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.done.setOnClickListener {
            singIn()

        }

        return _binding!!.root
    }

    fun singIn() {
        val email = _binding?.email?.text.toString()
        val password = _binding?.password?.text.toString()
        if (checkInput(email, password)) {

            UserAuthorization.getFirebaseAuthInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    binding.errorText.text = "*" + it.message
                }.addOnSuccessListener {
                    findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
                }
        }
        else
            binding.errorText.text = "*Fill out all the Fields"

    }

    fun checkInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

}