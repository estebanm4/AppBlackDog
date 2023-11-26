package com.dadm.appblackdog.services

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

    fun init() {
        auth = Firebase.auth
    }

    suspend fun emailLogin(data: UiLogin): Boolean {
        return try {
            val snapshot = auth.signInWithEmailAndPassword(data.email, data.password)
            snapshot.await()
            if (snapshot.isSuccessful)
                Log.d(GENERIC_TAG, "auth result: success")
            else
                Log.w(GENERIC_TAG, "auth result: failure", snapshot.exception)
            snapshot.isSuccessful
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
            false
        }

    }

    suspend fun addNewUser(email: String, password: String): FirebaseUser? {
        return try {
            val snapshot = auth.createUserWithEmailAndPassword(email, password)
            val data = snapshot.await()
            if (snapshot.isSuccessful)
                Log.d(GENERIC_TAG, "createUserWithEmail:success")
            else
                Log.w(GENERIC_TAG, "createUserWithEmail:failure", snapshot.exception)
            data.user
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
            null
        }

    }

    fun userLogOut() {
        auth.signOut().let {
            Log.d(GENERIC_TAG, "User LogOut")
        }
    }

    suspend fun setData(reference: String, data: Any): Boolean {
        return try {
            val snapshot = db.collection(reference).document().set(data)
            snapshot.await()
            if (snapshot.isSuccessful)
                Log.d(GENERIC_TAG, "DocumentSnapshot successfully written!")
            else
                Log.w(GENERIC_TAG, "Error writing document", snapshot.exception)
            snapshot.isSuccessful
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
            false
        }
    }

    suspend fun updateData(reference: String, itemId: String, argument: String, value: Any) {
        try {
            val snapshot = db.collection(reference).document(itemId).update(argument, value)
            snapshot.await()
            if (snapshot.isSuccessful) {
                Log.d(GENERIC_TAG, "DocumentSnapshot successfully updated!")
            } else
                Log.w(GENERIC_TAG, "Error updating document", snapshot.exception)

        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
        }
    }

    suspend fun getData(reference: String): List<QueryDocumentSnapshot>? {
        return try {
            val snapshot = db.collection(reference).get()
            val data = snapshot.await()
            if (snapshot.isSuccessful && data.toList().isNotEmpty()) {
                data.map { Log.d(GENERIC_TAG, "id ${it?.id} => ${it?.data}") }
            } else
                Log.w(GENERIC_TAG, "Error getting documents: ", snapshot.exception)
            data.toList()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
            null
        }
    }

    suspend fun getDataByArgument(
        reference: String,
        argument: String,
        value: Any
    ): List<QueryDocumentSnapshot>? {
        return try {
            val snapshot = db.collection(reference).whereEqualTo(argument, value).get()
            val data = snapshot.await()
            if (snapshot.isSuccessful && data.toList().isNotEmpty()) {
                data.map { Log.d(GENERIC_TAG, "id ${it?.id} => ${it?.data}") }
            } else
                Log.w(GENERIC_TAG, "Error getting documents: ", snapshot.exception)
            data.toList()
        } catch (e: Exception) {
            Log.e(GENERIC_TAG, "fatal error $e")
            null
        }
    }

}