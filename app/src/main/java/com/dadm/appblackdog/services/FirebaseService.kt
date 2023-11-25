package com.dadm.appblackdog.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.dadm.appblackdog.models.UiLogin
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


const val GENERIC_TAG: String = "blackdog"

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
                        Log.d(GENERIC_TAG, "auth result: success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(GENERIC_TAG, "auth result: failure", task.exception)
                    }
                }.await()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }
        return isSuccess
    }

    suspend fun addNewUser(email: String, password: String, context: Context): FirebaseUser? {
        var user: FirebaseUser? = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(GENERIC_TAG, "createUserWithEmail:success")
                    user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(GENERIC_TAG, "createUserWithEmail:failure", task.exception)
                }
            }
        return user
    }

    fun userLogOut() {
        auth.signOut().let {
            Log.d(GENERIC_TAG, "User LogOut")
        }
    }

    suspend fun setData(reference: String, data: Any) {
        try {
            db.collection(reference).document()
                .set(data)
                .addOnSuccessListener {
                    Log.d(GENERIC_TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(
                        GENERIC_TAG,
                        "Error writing document",
                        e
                    )
                }.await()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }

    }

    suspend fun updateData(reference: String, itemId: String, argument: String, value: Any) {

        try {
            db.collection(reference)
                .document(itemId).update(argument, value)
                .addOnSuccessListener {
                    Log.d(
                        GENERIC_TAG,
                        "DocumentSnapshot successfully updated!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(GENERIC_TAG, "Error updating document", e)
                }.await()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }
    }

    suspend fun getData(reference: String): List<QueryDocumentSnapshot> {
        var data: List<QueryDocumentSnapshot> = listOf()
        Log.d(GENERIC_TAG, "inicio de query $reference...")
        try {
            db.collection(reference)
                .get()
                .addOnSuccessListener { result ->
                    data = result.toList()
                    if (result != null)
                        result.map {
                            Log.d(GENERIC_TAG, "id ${it?.id} => ${it?.data}")
                        }
                    else Log.e(GENERIC_TAG, "data from server is null")

                }
                .addOnFailureListener { exception ->
                    Log.d(GENERIC_TAG, "Error getting documents: ", exception)
                }.await()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }
        return data
    }

    suspend fun getDataByArgument(reference: String, argument: String, value: Any) {
        try {
            db.collection(reference)
                .whereEqualTo(argument, value)
                .get()
                .addOnSuccessListener { result ->
                    if (result != null)
                        for (document in result) {
                            Log.d(GENERIC_TAG, "id ${document?.id} => ${document?.data}")
                        }
                    else Log.e(GENERIC_TAG, "data from server is null")
                }
                .addOnFailureListener { exception ->
                    Log.w(GENERIC_TAG, "Error getting documents: ", exception)
                }.await()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }
    }

}