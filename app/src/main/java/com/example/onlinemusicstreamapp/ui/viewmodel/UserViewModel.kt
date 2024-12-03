package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.entities.User
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userAuthorization: UserAuthorization
) : ViewModel() {
    private val _userData = MutableLiveData<FirebaseUser>()
    val userData: LiveData<FirebaseUser> = _userData

    init {
        getUserData()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user.password == user.confirmPassword)
                userAuthorization.createUserAccount(user.email, user.password)
            else
                Log.w("login", "wrong confirmPasscode")

        }

    }

    private fun getUserData()  {
        viewModelScope.launch(Dispatchers.IO) {
            _userData.postValue(userAuthorization.getCurrentUser())
        }
    }

    fun getCurrentUserId(): String {
        return userAuthorization.getCurrentUserId().toString()
    }

    fun getCurrentUserDisplayName(): String {
        return userAuthorization.getCurrentUser()?.displayName.toString()

    }

    fun getCurrentUserPhotoUrl(): String {
        return userAuthorization.getCurrentUser()?.photoUrl.toString()

    }

}