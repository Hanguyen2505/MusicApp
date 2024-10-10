package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.User
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val userData = MutableLiveData<User>()

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user.password == user.confirmPassword)
                UserAuthorization.createUserAccount(user.email, user.password)
            else
                Log.w("login", "wrong confirmPasscode")

        }

    }

    fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString())
            .requestEmail()
            .build()

    }
}