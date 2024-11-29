package com.example.onlinemusicstreamapp.database.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserAuthorization @Inject constructor(
    private val instance: FirebaseAuth
) {

    fun getFirebaseAuthInstance(): FirebaseAuth {
        return instance
    }

    fun getCurrentUser(): FirebaseUser? {
        return instance.currentUser
    }

    fun getCurrentUserId(): String? {
        return instance.currentUser?.uid
    }
    
    fun createUserAccount(email: String, password: String) {
        instance.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Log.d("create user", "success")
        }.addOnFailureListener {
            Log.d("create user", "failed")
        }
    }

    fun signout() {
        instance.signOut()
    }

}