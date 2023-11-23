package com.dadm.appblackdog.models

import com.google.firebase.Timestamp

data class Pet(
    val id: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val ownerId: String? = null,
    val ageRangeId: String? = null,
    val description: String? = null,
    val raceId: String? = null,
    val weight: Float? = null,
    val measureUnitId: String? = null,
    val birthdate: Timestamp? = null,
) {
}
