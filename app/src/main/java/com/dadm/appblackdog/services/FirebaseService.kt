package com.dadm.appblackdog.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.dadm.appblackdog.objects.UiLogin
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class FirebaseService {

    private lateinit var auth: FirebaseAuth
    private val firebaseTag: String = "firebase"

    private fun init() {
        auth = Firebase.auth
    }

    suspend fun firebaseAuthentication(data: UiLogin, context: Context): Boolean {
        init()
        var isSuccess = false
        try {
            auth.signInWithEmailAndPassword(data.email, data.password)
                .addOnCompleteListener(context as Activity) { task ->
                    isSuccess = task.isSuccessful
                    if (task.isSuccessful) {
                        Log.d(firebaseTag, "signInWithEmail:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(firebaseTag, "signInWithEmail:failure", task.exception)
                    }
                }.await()
        }catch (e: Exception){
            Log.e(firebaseTag, "error $e")
        }
        return isSuccess
    }

}