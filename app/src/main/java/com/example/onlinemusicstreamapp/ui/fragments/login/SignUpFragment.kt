package com.example.onlinemusicstreamapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.databinding.FragmentSignUpBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.onlinemusicstreamapp.database.data.entities.User

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.done.setOnClickListener {
            register()
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }



        return _binding!!.root
    }

    fun register() {
        val email = _binding?.email?.text.toString()
        val password = _binding?.password?.text.toString()
        val confirmPassword = _binding?.confirmPassword?.text.toString()
        val user = User(email, password, confirmPassword)
        if (checkInput(user))
            mLoginViewModel.addUser(user)
        else
            Toast.makeText(requireContext(), "Fill out all the fields", Toast.LENGTH_SHORT).show()
    }

    fun checkInput(user: User): Boolean {
        return user.email.isNotEmpty() && user.password.isNotEmpty() && user.confirmPassword.isNotEmpty()
    }

}