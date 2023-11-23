package com.dadm.appblackdog.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.dadm.appblackdog.models.UiLogin
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


const val FIREBASE_TAG: String = "firebase"

class FirebaseService {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private fun init() {
        auth = Firebase.auth
    }

    suspend fun emailLogin(data: UiLogin, context: Context): Boolean {
        init()
        var isSuccess = false
        try {
            auth.signInWithEmailAndPassword(data.email, data.password)
                .addOnCompleteListener(context as Activity) { task ->
                    isSuccess = task.isSuccessful
                    if (task.isSuccessful) {
                        Log.d(FIREBASE_TAG, "signInWithEmail:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(FIREBASE_TAG, "signInWithEmail:failure", task.exception)
                    }
                }.await()
        } catch (e: Exception) {
            Log.e(FIREBASE_TAG, "fatal error $e")
        }
        return isSuccess
    }

    fun userLogOut(){
        auth.signOut().let {
            Log.d(FIREBASE_TAG, "User LogOut")
        }
    }

    suspend fun setData(reference: String, data: Any) {
        try {
            db.collection(reference).document()
                .set(data)
                .addOnSuccessListener {
                    Log.d(FIREBASE_TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(
                        FIREBASE_TAG,
                        "Error writing document",
                        e
                    )
                }.await()
        } catch (e: Exception) {
            Log.e(FIREBASE_TAG, "fatal error $e")
        }

    }

    suspend fun updateData(reference: String, itemId: String, argument: String, value: Any) {

        try {
            db.collection(reference)
                .document(itemId).update(argument, value)
                .addOnSuccessListener {
                    Log.d(
                        FIREBASE_TAG,
                        "DocumentSnapshot successfully updated!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(FIREBASE_TAG, "Error updating document", e)
                }.await()
        } catch (e: Exception) {
            Log.e(FIREBASE_TAG, "fatal error $e")
        }
    }

    suspend fun getData(reference: String) {
        Log.d(FIREBASE_TAG, "inicio de query $reference...")
        try {
            db.collection(reference)
                .get()
                .addOnSuccessListener { result ->

                    if (result != null)
                        for (document in result) {
                            Log.d(FIREBASE_TAG, "id ${document?.id} => ${document?.data}")
                        }
                    else Log.e(FIREBASE_TAG, "data from server is null")

                }
                .addOnFailureListener { exception ->
                    Log.d(FIREBASE_TAG, "Error getting documents: ", exception)
                }.await()
        } catch (e: Exception) {
            Log.e(FIREBASE_TAG, "fatal error $e")
        }
    }

    suspend fun getDataByArgument(reference: String, argument: String, value: Any) {
        try {
            db.collection(reference)
                .whereEqualTo(argument, value)
                .get()
                .addOnSuccessListener { result ->
                    if (result != null)
                        for (document in result) {
                            Log.d(FIREBASE_TAG, "id ${document?.id} => ${document?.data}")
                        }
                    else Log.e(FIREBASE_TAG, "data from server is null")
                }
                .addOnFailureListener { exception ->
                    Log.w(FIREBASE_TAG, "Error getting documents: ", exception)
                }.await()
        } catch (e: Exception) {
            Log.e(FIREBASE_TAG, "fatal error $e")
        }
    }

}