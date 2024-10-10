package com.example.onlinemusicstreamapp.database.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object UserAuthorization {
    private val instance = FirebaseAuth.getInstance()

    fun getFirebaseAuthInstance(): FirebaseAuth {
        return instance
    }

    fun getCurrentUser(): FirebaseUser? {
        return instance.currentUser
    }

    fun createUserAccount(email: String, password: String) {
        instance.createUserWithEmailAndPassword(email, password)
    }

    fun signout() {
        instance.signOut()
    }

}