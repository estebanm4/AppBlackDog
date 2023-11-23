package com.dadm.appblackdog.models

import com.google.firebase.Timestamp

data class Owner(
    val id: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val birthdate: Timestamp? = null,
    val hasPets: Boolean? = null,
) {
}